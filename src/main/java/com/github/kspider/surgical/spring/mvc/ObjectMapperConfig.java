package com.github.kspider.surgical.spring.mvc;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig implements InitializingBean {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        objectMapper.addMixIn(Object.class, CustomPropertyFilterMixIn.class);
    }

    @JsonFilter("customPropertyFilter")
    class CustomPropertyFilterMixIn {}
}

