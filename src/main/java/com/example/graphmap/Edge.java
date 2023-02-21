package com.example.graphmap;

import java.util.Objects;

public class Edge<T> {
    private final String name;
    private final T destination;
    private int weight;

    public Edge(String name, T destination, int weight) {
        this.name = Objects.requireNonNull(name);
        this.destination = Objects.requireNonNull(destination);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public T getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight){
        if(weight < 0)
            throw new IllegalArgumentException("Weight can not be negative.");
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "to " + destination + " by " + name + " takes " + weight;
    }
}