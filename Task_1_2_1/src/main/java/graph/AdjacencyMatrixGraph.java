package graph;

import java.io.IOException;
import java.util.*;

public class AdjacencyMatrixGraph<V> implements Graph<V> {
    private final List<V> vertices = new ArrayList<>();
    private final List<List<Boolean>> matrix = new ArrayList<>();

    @Override
    public void addVertex(V vertex) {
        if (vertices.contains(vertex)) {
            return;
        }
        vertices.add(vertex);

        // добавляем новую колонку во все существующие строки
        for (List<Boolean> row : matrix) {
            row.add(false);
        }

        // создаем новую строку для новой вершины
        List<Boolean> newRow = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            newRow.add(false);
        }
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(V vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) {
            return;
        }

        vertices.remove(index);
        matrix.remove(index);

        // удаляем соответствующий столбец из всех строк
        for (List<Boolean> row : matrix) {
            row.remove(index);
        }
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        int srcIndex = vertices.indexOf(source);
        int destIndex = vertices.indexOf(destination);

        matrix.get(srcIndex).set(destIndex, true);
    }

    @Override
    public void removeEdge(V source, V destination) {
        int srcIndex = vertices.indexOf(source);
        int destIndex = vertices.indexOf(destination);

        if (srcIndex != -1 && destIndex != -1) {
            matrix.get(srcIndex).set(destIndex, false);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        int index = vertices.indexOf(vertex);
        if (index == -1) {
            return neighbors;
        }

        List<Boolean> row = matrix.get(index);
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i)) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    @Override
    public List<V> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public void readFromFile(String filepath) throws IOException {
        GraphUtils.readGraphFromFile(this, filepath, s -> (V) s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph<?> graph)) return false;

        List<?> otherVertices = graph.getVertices();
        if (!new HashSet<>(vertices).equals(new HashSet<>(otherVertices))) {
            return false;
        }

        for (V v : vertices) {
            List<?> otherNeighbors = ((Graph<Object>) graph).getNeighbors(v);
            if (!new HashSet<>(this.getNeighbors(v)).equals(new HashSet<>(otherNeighbors))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, matrix);
    }

    @Override
    public String toString() {
        return "AdjacencyMatrixGraph{vertices=" + vertices + ", matrix=" + matrix + "}";
    }
}