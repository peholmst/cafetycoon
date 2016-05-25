package org.vaadin.samples.cafetycoon.domain;

import java.io.Serializable;

@FunctionalInterface
public interface ServiceProvider<S> extends Serializable {

	S get();
}
