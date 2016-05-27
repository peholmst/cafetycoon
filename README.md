# Cafe Tycoon Example Application

This application is an attempt to implement a more complex UI in Vaadin [without using the MVP pattern](https://vaadin.com/blog/-/blogs/is-mvp-a-best-practice-). 
The application simulates some kind of mission control system (so no small screens) for a chain of coffee shops, where the owner - you - can track the progress of the cafés in real time. When they run out of beans, it is your responsibility to restock them. The employees
can also send messages to you, although the messaging system is far from production ready.

You can find a more detailed description of the technical design of this application [here](design/README.md).

To try this application out, you should first clone the repository. Once you've done that, you should build the project
using Maven (you will also need Java 8):

``$ mvn clean install -Pwidgetset``

The `widgetset` profile will trigger the compilation of the custom widgetset and theme. After that, you can run the application
using the embedded Jetty server:

``$ mvn jetty:run``

Sales and messages are generated randomly, so just open up a café, lean back and see what happens. Oh and by the way, the theme
has so far only been optimized for Chrome. Pull requests are accepted. ;-)

