package com.github.ksprider.surgical;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomPropertyFilter<T> extends SimpleBeanPropertyFilter {

    private final T t;
    private final SerializationHandler<T> serializerHandler;
    private final String location;
    private final boolean isAll;
    private final Map<String, Boolean> cache = new HashMap<>();

    public CustomPropertyFilter(T t, SerializationHandler<T> serializerHandler, String location, boolean isAll) {
        this.t = t;
        this.serializerHandler = serializerHandler;
        this.location = location;
        this.isAll = isAll;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer) throws Exception {
        if (pojo instanceof MappingJacksonValue) {
            writer.serializeAsField(pojo, gen, prov);
            return;
        }

        String key = getKey(gen.getOutputContext(), writer.getName());
        boolean serialize = isAll || cache.computeIfAbsent(key, it -> {
            T ot = (T) BeanUtils.instantiateClass(t.getClass());
            BeanUtils.copyProperties(t, ot);
            return serializerHandler.isSerialize(gen.getOutputContext(), writer.getName(), ot);
        });
        if (serialize) {
            writer.serializeAsField(pojo, gen, prov);
        }
    }

    private String getKey(JsonStreamContext context, String name) {
        StringBuilder sb = new StringBuilder(location);
        for (JsonStreamContext c = context.getParent(); Objects.nonNull(c); c = c.getParent()) {
            sb.append("#");
            sb.append(Objects.isNull(c.getCurrentName()) ? name : c.getCurrentName());
        }
        return sb.toString();
    }

}
