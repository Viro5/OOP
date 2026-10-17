package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for graph algorithms such as topological sort and file reading.
 */
public final class GraphUtils {

    private GraphUtils() {
        // Utility class constructor
    }

    /**
     * Reads a graph from a file where each line contains two vertices separated by space.
     *
     * @param graph    the graph instance to populate
     * @param filename the path to the file
     * @param <T>      the vertex type
     * @throws IOException if an I/O error occurs
     */
    public static <T> void readGraphFromFile(
            Graph<T> graph, String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 2) {
                    // Assuming string representation for simplicity,
                    // or cast if needed based on generic type usage.
                    @SuppressWarnings("unchecked")
                    T source = (T) parts[0];
                    @SuppressWarnings("unchecked")
                    T dest = (T) parts[1];
                    graph.addEdge(source, dest);
                }
            }
        }
    }

    /**
     * Performs topological sorting on a graph using Depth-First Search (DFS).
     *
     * @param graph the graph to sort
     * @param <T>   the vertex type
     * @return a list of vertices in topologically sorted order
     * @throws IllegalStateException if a cycle is detected
     */
    public static <T> List<T> topologicalSort(Graph<T> graph) {
        Map<T, Integer> visited = new HashMap<>();
        List<T> result = new ArrayList<>();

        for (T vertex : graph.getVertices()) {
            if (!visited.containsKey(vertex)) {
                dfs(vertex, graph, visited, result);
            }
        }
        return result;
    }

    private static <T> void dfs(
            T vertex, Graph<T> graph,
            Map<T, Integer> visited, List<T> result) {
        visited.put(vertex, 1); // 1 means visiting

        for (T neighbor : graph.getNeighbors(vertex)) {
            Integer state = visited.get(neighbor);
            if (state != null) {
                if (state == 1) {
                    throw new IllegalStateException(
                            "Cycle detected in graph");
                }
            } else {
                dfs(neighbor, graph, visited, result);
            }
        }

        visited.put(vertex, 2); // 2 means visited
        result.add(0, vertex); // prepend
    }
}