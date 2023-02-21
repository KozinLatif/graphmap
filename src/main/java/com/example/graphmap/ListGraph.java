package com.example.graphmap;

import java.util.*;

public class ListGraph<T> implements Graph<T> {
    private Map<T, Set<Edge<T>>> nodes = new HashMap<>();

    public void add(T node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    public void remove(T node) {
        if(!nodes.containsKey(node))
            throw new NoSuchElementException("Element is not in the list");
        for(Edge<T> e: nodes.get(node)){
            T copy = e.getDestination();
            nodes.get(copy).removeIf(x -> {
                return x.getDestination().equals(node);
            });
        }
        nodes.remove(node);
    }

    public void connect(T node1, T node2, String name, int weight) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2))
            throw new NoSuchElementException("Nodes are missing.");
        if(weight < 0)
            throw new IllegalArgumentException("Weight can not be negative.");
        if(isConnected(node1, node2) || isConnected(node2, node1))
            throw new IllegalStateException("Nodes are already connected.");
        Edge<T> edge1 = new Edge<>(name, node2, weight);
        Edge<T> edge2 = new Edge<>(name, node1, weight);
        nodes.get(node1).add(edge1);
        nodes.get(node2).add(edge2);
    }

    public void disconnect(T node1, T node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2))
            throw new NoSuchElementException("Nodes are missing.");
        if(!isConnected(node1, node2) || !isConnected(node2, node1))
            throw new IllegalStateException("Nodes are not connected.");
        Edge<T> edge1 = getEdgeBetween(node1, node2);
        Edge<T> edge2 = getEdgeBetween(node2, node1);
        nodes.get(node1).remove(edge1);
        nodes.get(node2).remove(edge2);
    }

    public Edge<T> getEdgeBetween(T node1, T node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2))
            throw new NoSuchElementException("Nodes are missing.");
        for(Edge<T> e: nodes.get(node1)){
            if(e.getDestination() == node2){
                return e;
            }
        }
        return null;
    }

    public void setConnectionWeight(T node1, T node2, int weight) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2))
            throw new NoSuchElementException("Nodes are missing.");
        getEdgeBetween(node1, node2).setWeight(weight);
        getEdgeBetween(node2, node1).setWeight(weight);
    }

    public Set<T> getNodes() {
        Set<T> copy = new HashSet<>();
        copy.addAll(nodes.keySet());
        return copy;
    }

    public Set<Edge<T>> getEdgesFrom(T node1) {
        if(!nodes.containsKey(node1))
            throw new NoSuchElementException("Nodes are missing.");
        Set<Edge<T>> copy = new HashSet<>();
        copy.addAll(nodes.get(node1));
        return copy;
    }

    public boolean pathExists(T node1, T node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2))
            return false;
        Set<T> visited = new HashSet<>();
        depthFirstSearch(node1, visited);
        return visited.contains(node2);
    }

    public List<Edge<T>> getPath(T node1, T node2) {
        Set<T> visited = new HashSet<>();
        Map<T, T> path = new HashMap<>();

        depthFirstSearch(node1, null, visited, path);

        if(!visited.contains(node2)){
            return null;
        }

        return gatherPath(node1, node2, path);
    }

    private void depthFirstSearch(T node1, T node2, Set<T> visited, Map <T, T> via){
        visited.add(node1);
        via.put(node1, node2);
        for(Edge<T> e : nodes.get(node1)){
            if (!visited.contains(e.getDestination())){
                depthFirstSearch(e.getDestination(), node1, visited, via);
            }
        }
    }

    private void depthFirstSearch(T where, Set<T> visited){
        visited.add(where);
        for(Edge<T> e : nodes.get(where))
            if (!visited.contains(e.getDestination()))
                depthFirstSearch(e.getDestination(), visited);
    }

    private List <Edge<T>> gatherPath (T node1, T node2, Map<T, T> via){
        List<Edge<T>> path = new ArrayList<>();

        T where = node2;
        while (!where.equals(node1)){
            T node = via.get(where);
            Edge<T> e = getEdgeBetween(node, where);
            path.add(e);
            where = node;
        }
        Collections.reverse(path);
        return path;
    }

    public boolean hasNode(T node){
        return nodes.containsKey(node);
    }
    public boolean isConnected(T node1, T node2){
        for(Edge<T> c : nodes.get(node2)){
            if(c.getDestination().equals(node1)){
                return true;
            }
        }
        return false;
    }

    public Map<T, Set<Edge<T>>> getNodesList() {
        return nodes;
    }

    public String toString() {
        return nodes.toString();
    }
}
