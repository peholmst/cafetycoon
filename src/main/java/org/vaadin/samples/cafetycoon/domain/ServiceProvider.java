package org.vaadin.samples.cafetycoon.domain;

import java.io.Serializable;

/**
 * This interface provides a way for serializable UI components to store
 * serializable references to backend services without having to worry about how
 * to look them up.
 * <p>
 * If session replication is needed, implementations of this interface should
 * make sure that they are properly serialized and that they look up their
 * backend services after deserialization. This is not done in this sample
 * application for simplicity's sake.
 */
@FunctionalInterface
public interface ServiceProvider<S> extends Serializable {

	/**
	 * Returns the backend service or throws an exception if it is not
	 * available.
	 */
	S get();
}
