package com.github.kspider.surgical;

import com.fasterxml.jackson.core.JsonStreamContext;

public interface SerializationHandler<T> {

    boolean isSerialize(JsonStreamContext context, String currentPropertyName, T t);
}
