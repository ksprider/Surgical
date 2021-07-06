package com.github.ksprider.surgical;

import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

public class CustomFilterProvider<T> extends FilterProvider {

    private final CustomPropertyFilter customPropertyFilter;

    public CustomFilterProvider(T root, SerializationHandler<T> serializerHandler, String location, boolean isAll) {
        this.customPropertyFilter = new CustomPropertyFilter<>(root, serializerHandler, location, isAll);
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
