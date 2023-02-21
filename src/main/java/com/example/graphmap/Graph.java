package com.example.graphmap;

import java.util.List;
import java.util.Set;

public interface Graph<T> {
    void add(T node);
    void remove(T node);
    void connect(T node1, T node2, String name, int weight);
    void disconnect(T node1, T node2);
    Edge<T> getEdgeBetween(T node1, T node2);
    void setConnectionWeight(T node1, T node2, int weight);
    Set<T> getNodes();
    Set<Edge<T>> getEdgesFrom(T node1);
    boolean pathExists(T node1, T node2);
    List<Edge<T>> getPath(T node1, T node2);
}