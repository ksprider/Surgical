package com.github.ksprider.surgical;

import com.fasterxml.jackson.core.JsonStreamContext;

public interface SerializationHandler<T> {

    boolean isSerialize(JsonStreamContext context, String currentPropertyName, T t);
}
