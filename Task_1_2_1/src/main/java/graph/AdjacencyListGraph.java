package graph;

import java.io.IOException;
import java.util.*;

public class AdjacencyListGraph<V> implements Graph<V> {
    private final Map<V, List<V>> adjacencyMap = new HashMap<>();

    @Override
    public void addVertex(V vertex) {
        adjacencyMap.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(V vertex) {
        adjacencyMap.remove(vertex);
        for (List<V> neighbors : adjacencyMap.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);
        List<V> neighbors = adjacencyMap.get(source);
        if (!neighbors.contains(destination)) {
            neighbors.add(destination);
        }
    }

    @Override
    public void removeEdge(V source, V destination) {
        if (adjacencyMap.containsKey(source)) {
            adjacencyMap.get(source).remove(destination);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        return adjacencyMap.getOrDefault(vertex, Collections.emptyList());
    }

    @Override
    public List<V> getVertices() {
        return new ArrayList<>(adjacencyMap.keySet());
    }

    @Override
    public void readFromFile(String filepath) throws IOException {
        GraphUtils.readGraphFromFile(this, filepath, s -> (V) s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;
        Graph<?> graph = (Graph<?>) o;

        List<V> vertices = this.getVertices();
        List<?> otherVertices = graph.getVertices();
        if (!new HashSet<>(vertices).equals(new HashSet<>(otherVertices))) {
            return false;
        }
        for (V v : vertices) {
            // Используем приведение к Graph для безопасного вызова getNeighbors с типом V
            List<?> otherNeighbors = ((Graph<Object>) graph).getNeighbors(v);
            if (!new HashSet<>(this.getNeighbors(v)).equals(new HashSet<>(otherNeighbors))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyMap);
    }

    @Override
    public String toString() {
        return "AdjacencyListGraph{adjacencyMap=" + adjacencyMap + "}";
    }
}