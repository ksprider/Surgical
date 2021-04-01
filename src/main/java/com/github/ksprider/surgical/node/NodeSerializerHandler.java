package com.github.ksprider.surgical.node;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.github.ksprider.surgical.SerializationHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NodeSerializerHandler implements SerializationHandler<Node> {

    private static final String ALL = "*";

    @Override
    public boolean isSerialize(JsonStreamContext context, String currentPropertyName, Node node) {
        AtomicBoolean serialize = new AtomicBoolean(false);
        Stack<String> stack = new Stack<>();
        String propertyName = currentPropertyName;

        while (!Objects.isNull(context.getParent())) {
            if (null != propertyName) {
                stack.push(propertyName);
            }
            context = context.getParent();
            propertyName = context.getCurrentName();
        }

        if (!stack.isEmpty()) {
            for (Node n = node.getNodes().get(stack.peek()); Objects.nonNull(node) && !stack.isEmpty() && (node.getPropertySet().contains(stack.peek()) || Objects.nonNull(n) || node.getPropertySet().contains(ALL));) {
                stack.pop();
                node = n;
                if (null != node && !stack.isEmpty()) {
                    n = node.getNodes().get(stack.peek());
                }
            }
            if (stack.isEmpty()) {
                serialize.set(true);
            }
        }
        return serialize.get();
    }
}
