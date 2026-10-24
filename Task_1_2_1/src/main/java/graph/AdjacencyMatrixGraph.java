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
 * Graph implementation using an adjacency matrix.
 *
 * @param <T> the vertex type
 */
public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private final List<T> vertices = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>();

    @Override
    public void addVertex(T vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            for (List<Integer> row : matrix) {
                row.add(0);
            }
            List<Integer> newRow = new ArrayList<>();
            for (int i = 0; i < vertices.size(); i++) {
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
            for (List<Integer> row : matrix) {
                row.remove(index);
            }
        }
    }

    @Override
    public void addEdge(T source, T destination) {
        addVertex(source);
        addVertex(destination);
        int srcIndex = vertices.indexOf(source);
        int destIndex = vertices.indexOf(destination);
        matrix.get(srcIndex).set(destIndex, 1);
    }

    @Override
    public void removeEdge(T source, T destination) {
        int srcIndex = vertices.indexOf(source);
        int destIndex = vertices.indexOf(destination);
        if (srcIndex != -1 && destIndex != -1) {
            matrix.get(srcIndex).set(destIndex, 0);
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
            List<Integer> row = matrix.get(index);
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == 1) {
                    neighbors.add(vertices.get(i));
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