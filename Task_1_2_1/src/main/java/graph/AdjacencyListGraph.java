package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph implementation using adjacency lists.
 *
 * @param <T> the vertex type
 */
public class AdjacencyListGraph<T> implements Graph<T> {
    private final Map<T, List<T>> adjacencyList = new HashMap<>();

    @Override
    public void addVertex(T vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(T vertex) {
        adjacencyList.remove(vertex);
        for (List<T> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(T source, T destination) {
        addVertex(source);
        addVertex(destination);
        List<T> neighbors = adjacencyList.get(source);
        if (!neighbors.contains(destination)) {
            neighbors.add(destination);
        }
    }

    @Override
    public void removeEdge(T source, T destination) {
        List<T> neighbors = adjacencyList.get(source);
        if (neighbors != null) {
            neighbors.remove(destination);
        }
    }

    @Override
    public Collection<T> getVertices() {
        return adjacencyList.keySet();
    }

    @Override
    public Collection<T> getNeighbors(T vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    @Override
    public void readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 2) {
                    @SuppressWarnings("unchecked")
                    T source = (T) parts[0];
                    @SuppressWarnings("unchecked")
                    T dest = (T) parts[1];
                    addEdge(source, dest);
                }
            }
        }
    }
}