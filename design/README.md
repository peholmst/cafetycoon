# The Design of Cafe Tycoon

In this little README, I'm going to try to explain the design of the UI layer of the Cafe Tycoon application. The domain layer is less
interesting in this case so I'm just going to summarize it really quickly before we move on. It consists of a set of repositories and services that can be used to query different information and also perform some business operations (mainly restock). The business events (sales, status changes, etc.) are published on an event bus (Guava) which is global to the application. There is a set of background threads that randomly generate business events as long as the application is running.

## Break down into components

The starting point for the entire design was a single mockup of the Dashboard view. The first thing I did was to break it down into
smaller components. I also wanted to try out the new Vaadin Designer, which had some impact on the granularity of the components.

This is what I ended up with (this only looks at the individual Dashboard view, not the surrounding header and hidden main menu): 

![](breakdown.png)

The `Dashboard` contains a split panel that has a `SalesOverview` on the left and a `CafeMap` on the right. On the far right, there is
a `CafeOverview` overlay which in turn contains a custom component, `MessageBoard` for the employees and popup messages. The `CafeMap` and `MessageBoard` components are coded in Java, the rest are created using Vaadin Designer.

## Introducing models

Since the entire application is basically about updating different data views (tables, maps, graphs) in response to business events,
I decided to opt for a model centric design where smart *application models* are listening to the domain event bus and then notifying
the observing UI components.

Since there is no point in hiding Vaadin from the application models, they are aware of the Vaadin data model and expose their properties
as `Container`s and `ObjectProperty` instances. This makes it easy to plug them into the UI.

Also, since the application models are updated in response to business events generated in background threads, the models need to be aware
of the Vaadin server push pattern. As a result, the application models know about which UI they belong to and uses the `UI.access()` method whenever any of the bound properties or containers are updated. However, to make it possible to write unit tests for the application models, they are also fully functional without the presence of a UI instance.

Deciding what models to write was not entirely obvious. You don't want to create too big models since they become harder to maintain and reuse. You also don't want to create too small models because of the overhead. 

The simplest model needed to keep track of the currently selected cafe since this information is used by many of the Dashboard component.
This task is handled by the `CafeSelectionModel` which exposes a single `ObjectProperty` containing the cafe.

Next up was the `SalesOverview` component. Since it and the `CafeMap` basically show the same data in slightly different ways, they both
share the same model, which is `SalesOverviewModel`. This model knows about all the cafes, their current status, how much income they have generated and also keeps track of the total income and balance. This was a fairly straight forward model to implement and use.

The model or models for the `CafeOverview` were more challenging. The view shows more data from different sources, so using many smaller model would have been a valid alternative. I ended up using two models; the `CafeOverviewModel` and the `PersonnelModel`.

The `CafeOverviewModel` exposes three properties: the status of the current cafe, the sales statistics of the current cafe and the bean stock of the current cafe. Of these three, only the sales statistics and the bean stock are related: whenever there is a sale, the bean stock is reduced,
so both properties need to be updated in reaction to the same event. This is one argument for keeping them in the same model. However, the status property is completely independent of these two. The primary reason for putting it in the same model was convenience, but I did also consider putting it into a separate model or even creating a separate component for only showing the status.

One downside with this example application is that it showcases very little user input. There are basically only buttons for restocking a cafe and that's it. I initially had the implementation for invoking the backendservice directly in the UI, but later decided to move it to the `CafeOverviewModel`. The reasons for this were the following:

* Restocking is an operation that needs the current cafe as a parameter. This information is already available in the `CafeOverviewModel`.
* The backend service that provides the restock method is already used by `CafeOverviewModel`.
* The model keeps track of the bean stock changes so it logically makes sense to put the restock method implementation there as well since there is no presenter that would take care of this in an MVP design.

Finally, there is `PersonnelModel`. I originally considered incorporating it into the `CafeOverviewModel` since the employees and messages are shown inside the `CafeOverview` component. However, this would have made the model less cohesive and more difficult to extend and reuse, so I decided to go for a separate model.

The `PersonnelModel` (and its corresponding UI component) is the least developed component of the application in terms of usability. You can only see the message that was last received and only if you happened to look at the cafe from which the message was sent at that time. Clearly there is a lot of room for improvement if this was a real application, which in turn is a good reason to keep the personnel model and UI as separate components.

Exposing the latest message was only a matter of defining an `ObjectProperty`. Exposing the list of employees and their pictures was slightly more difficult. I first attempted to use a Vaadin Container for this, but this turned out to require quite a lot of boilerplate coding on both the model side and the UI side, since I was not planning to use any ready made Vaadin components that read data from containers. Instead, I opted for another `ObjectProperty` containing a list of employees. Since the application does not need to support adding or removing employees on the fly, this was perfectly fine and simplified the code quite a bit.

## Wiring everything together

Now when the views and models are finished, they had to be wired together. I did not want to use any dependency injection frameworks here in order keep my design patters as framework agnostic as possible. However, to keep components and models reusable and testable, they were not allowed to look up any dependencies themselves. Services would have to be injected into models and models would have to be injected into UI components.

Since I was using Vaadin Designer and nesting designs inside each other, I could not use constructor parameter injection for the UI components. Instead, I had to rely on setter methods. This is slightly more error prone since you can accidentally forget to inject a dependency, but such is life sometimes. The models on the other hand were just POJOs and there I used constructor parameter injection.

Since the Dashboard view itself does not do anything else than composing itself from other components, it was a good candidate for handling the injections. Once the dashboard is done constructing its UI (which it can do without access to any models by the way), it will create and wire up all the necessary application models and then pass them on to the UI components, which will register themselves accordingly. When doing this it is a good idea to keep scope in mind - if the model and the UI component have the same scope, there is no need to unregister afterwards since both objects will become garbage collected at the same time anyway (it is a good idea however to make a note of this in a comment somewhere).

## Just one more thing; Service locators

There is one more pattern I'm using that I would like to explain. Since I didn't want to statically look up the backend services from within the models, I had to store the injected references i fields inside the model. Since the models are part of the Vaadin application and stored in the Vaadin session, they need to be serializable in case you want to support session replication. Since you do not want to serialize your entire service layer, you have a problem.

I have solved this problem by defining a serializable `ServiceLocator` interface. Instead of injecting the backend services directly, I inject
`ServiceLocator`s that know how to look up the backend services. This means I can now properly serialize my UI without coupling it to some static service locator framework. The added advantage with this approach is that you can add more logic to the real service lookup, such as checking whether a service is alive and retrying with an alternative service if not. However, in this sample application, I'm just using method pointers
as service locators so serialization will most likely not work.
 