package com.github.ksprider.surgical.spring.mvc;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksprider.surgical.CustomFilterProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
public class ObjectMapperConfig implements InitializingBean {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public CustomFilterProvider<?> defaultFilterProvider() {
        return new CustomFilterProvider<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        objectMapper.addMixIn(Serializable.class, CustomPropertyFilterMixIn.class);
        objectMapper.setFilterProvider(defaultFilterProvider());
    }

    @JsonFilter("customPropertyFilter")
    class CustomPropertyFilterMixIn {}
}

