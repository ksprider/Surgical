package com.github.kspider.surgical.node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node implements Cloneable {


    private String name;

    private Set<String> propertySet = new HashSet<>();

    private Map<String, Node> nodes = new HashMap<>();


    public Node() {

    }

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPropertySet() {
        return propertySet;
    }

    public void setPropertySet(Set<String> propertySet) {
        this.propertySet = propertySet;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Node clone() throws CloneNotSupportedException {
        return (Node)super.clone();
    }
}
