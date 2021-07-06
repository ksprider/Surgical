package com.github.ksprider.surgical.spring.mvc;

import com.github.ksprider.surgical.CustomFilterProvider;
import com.github.ksprider.surgical.SerializationHandler;
import com.github.ksprider.surgical.TreeHandler;
import com.github.ksprider.surgical.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class JSONResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private TreeHandler<Node> treeHandler;
    private SerializationHandler<Node> serializationHandler;
    private final Map<String, CustomFilterProvider> cache = new HashMap<>();

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
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType) &&
                (
                        methodParameter.hasMethodAnnotation(JSON.class)
                        || AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), ResponseBody.class)
                        || methodParameter.hasMethodAnnotation(ResponseBody.class)
                );
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        JSON jsonAnnotation = methodParameter.getMethod().getAnnotation(JSON.class);
        String key = methodParameter.getExecutable().toString();
        CustomFilterProvider customFilterProvider = cache.computeIfAbsent(key, (it) -> {
            boolean isAll = null == jsonAnnotation;
            String treeStr = isAll ? "*" : jsonAnnotation.value();
            Node root = treeHandler.treefy(treeStr);
            return new CustomFilterProvider(root, serializationHandler, key, isAll);
        });
        MappingJacksonValue value = new MappingJacksonValue(o);
        value.setFilters(customFilterProvider);
        return value;
    }

}
