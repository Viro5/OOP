package graph;

import java.io.IOException;
import java.util.*;

public class IncidenceMatrixGraph<V> implements Graph<V> {
    private final List<V> vertices = new ArrayList<>();
    // список рёбер, где каждое ребро — это пара [source, destination]
    private final List<Edge<V>> edges = new ArrayList<>();
    // матрица: строки — вершины, столбцы — рёбра
    private final List<List<Integer>> matrix = new ArrayList<>();

    private static class Edge<V> {
        V source;
        V destination;

        Edge(V source, V destination) {
            this.source = source;
            this.destination = destination;
        }
    }

    @Override
    public void addVertex(V vertex) {
        if (vertices.contains(vertex)) {
            return;
        }
        vertices.add(vertex);
        List<Integer> newRow = new ArrayList<>();
        // заполняем нулями для всех существующих рёбер
        for (int i = 0; i < edges.size(); i++) {
            newRow.add(0);
        }
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(V vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) {
            return;
        }

        // удаляем все рёбра, связанные с этой вершиной
        for (int i = edges.size() - 1; i >= 0; i--) {
            Edge<V> edge = edges.get(i);
            if (edge.source.equals(vertex) || edge.destination.equals(vertex)) {
                removeEdge(edge.source, edge.destination);
            }
        }

        vertices.remove(index);
        matrix.remove(index);
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        // проверяем, нет ли уже такого ребра
        for (Edge<V> edge : edges) {
            if (edge.source.equals(source) && edge.destination.equals(destination)) {
                return;
            }
        }

        edges.add(new Edge<>(source, destination));
        int srcIndex = vertices.indexOf(source);
        int destIndex = vertices.indexOf(destination);

        // добавляем новый столбец во все строки матрицы
        for (int i = 0; i < vertices.size(); i++) {
            int val = 0;
            if (i == srcIndex) val = -1;
            else if (i == destIndex) val = 1;
            matrix.get(i).add(val);
        }
    }

    @Override
    public void removeEdge(V source, V destination) {
        int edgeIndex = -1;
        for (int i = 0; i < edges.size(); i++) {
            Edge<V> edge = edges.get(i);
            if (edge.source.equals(source) && edge.destination.equals(destination)) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex == -1) {
            return;
        }

        edges.remove(edgeIndex);
        // удаляем соответствующий столбец из каждой строки матрицы
        for (List<Integer> row : matrix) {
            row.remove(edgeIndex);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        int srcIndex = vertices.indexOf(vertex);
        if (srcIndex == -1) {
            return neighbors;
        }

        for (int j = 0; j < edges.size(); j++) {
            // если в строке нашей вершины стоит -1, значит, ребро исходит из неё
            if (matrix.get(srcIndex).get(j) == -1) {
                Edge<V> edge = edges.get(j);
                neighbors.add(edge.destination);
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
        return Objects.hash(vertices, edges, matrix);
    }

    @Override
    public String toString() {
        return "IncidenceMatrixGraph{vertices=" + vertices + ", edges=" + edges.size() + ", matrix=" + matrix + "}";
    }
}