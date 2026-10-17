package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Graph implementation using an incidence matrix.
 *
 * @param <T> the vertex type
 */
public class IncidenceMatrixGraph<T> implements Graph<T> {
    private final List<T> vertices = new ArrayList<>();
    private final List<EdgeInfo<T>> edges = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>();

    private static class EdgeInfo<T> {
        T source;
        T destination;

        EdgeInfo(T source, T destination) {
            this.source = source;
            this.destination = destination;
        }
    }

    @Override
    public void addVertex(T vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            List<Integer> newRow = new ArrayList<>();
            for (int i = 0; i < edges.size(); i++) {
                newRow.add(0);
            }
            matrix.add(newRow);
        }
    }

    @Override
    public void removeVertex(T vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            vertices.remove(index);
            matrix.remove(index);
            for (int i = edges.size() - 1; i >= 0; i--) {
                EdgeInfo<T> edge = edges.get(i);
                if (edge.source.equals(vertex)
                        || edge.destination.equals(vertex)) {
                    removeEdge(edge.source, edge.destination);
                }
            }
        }
    }

    @Override
    public void addEdge(T source, T destination) {
        addVertex(source);
        addVertex(destination);

        // Check if edge already exists
        for (EdgeInfo<T> edge : edges) {
            if (edge.source.equals(source) && edge.destination.equals(destination)) {
                return;
            }
        }

        edges.add(new EdgeInfo<>(source, destination));
        int edgeIndex = edges.size() - 1;

        for (int i = 0; i < vertices.size(); i++) {
            T v = vertices.get(i);
            int val = 0;
            if (v.equals(source)) {
                val = 1;
            }
            if (v.equals(destination)) {
                val = -1;
            }
            matrix.get(i).add(val);
        }
    }

    @Override
    public void removeEdge(T source, T destination) {
        int edgeIndex = -1;
        for (int i = 0; i < edges.size(); i++) {
            EdgeInfo<T> edge = edges.get(i);
            if (edge.source.equals(source) && edge.destination.equals(destination)) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex != -1) {
            edges.remove(edgeIndex);
            for (List<Integer> row : matrix) {
                row.remove(edgeIndex);
            }
        }
    }

    @Override
    public Collection<T> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public Collection<T> getNeighbors(T vertex) {
        List<T> neighbors = new ArrayList<>();
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            for (int j = 0; j < edges.size(); j++) {
                EdgeInfo<T> edge = edges.get(j);
                if (edge.source.equals(vertex)) {
                    neighbors.add(edge.destination);
                }
            }
        }
        return neighbors;
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