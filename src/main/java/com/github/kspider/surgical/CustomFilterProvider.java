package com.github.kspider.surgical;

import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

public class CustomFilterProvider<T> extends FilterProvider {

    private final T root;
    private final SerializationHandler<T> serializerHandler;
    private final String location;
    private final CustomPropertyFilter customPropertyFilter;

    public CustomFilterProvider(T root, SerializationHandler<T> serializerHandler, String location) {
        this.root = root;
        this.serializerHandler = serializerHandler;
        this.location = location;
        this.customPropertyFilter = new CustomPropertyFilter<>(root, serializerHandler, location);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        return customPropertyFilter;
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
        return customPropertyFilter;
    }

}
