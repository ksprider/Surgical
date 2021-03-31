package com.github.kspider.surgical.spring.mvc;

import com.github.kspider.surgical.CustomFilterProvider;
import com.github.kspider.surgical.SerializationHandler;
import com.github.kspider.surgical.TreeHandler;
import com.github.kspider.surgical.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class JSONResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private TreeHandler<Node> treeHandler;
    private SerializationHandler<Node> serializationHandler;
    private Map<String, CustomFilterProvider> cache = new HashMap<>();

    @Autowired
    public void setTreeHandler(TreeHandler<Node> treeHandler) {
        this.treeHandler = treeHandler;
    }

    @Autowired
    public void setSerializationHandler(SerializationHandler<Node> serializationHandler) {
        this.serializationHandler = serializationHandler;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType) && methodParameter.getMethod().isAnnotationPresent(JSON.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        JSON jsonAnnotation = methodParameter.getMethod().getAnnotation(JSON.class);
        String key = methodParameter.getExecutable().toString();
        CustomFilterProvider customFilterProvider = cache.computeIfAbsent(key, (it) -> {
            Node root = treeHandler.treefy(jsonAnnotation.value());
            return new CustomFilterProvider(root, serializationHandler, key);
        });

        MappingJacksonValue value = new MappingJacksonValue(o);
        value.setFilters(customFilterProvider);
        return value;
    }

}
