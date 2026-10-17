package graph;

import java.util.*;

public class GraphUtils {

    /**
     * считывает граф из текстового файла.
     * каждая строка файла должна содержать две вершины ребра, разделенные пробелом (например: "A B").
     */
    public static <V> void readGraphFromFile(Graph<V> graph, String filepath, java.util.function.Function<String, V> parser) throws java.io.IOException {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Пропускаем пустые строки и комментарии
                }
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    V source = parser.apply(parts[0]);
                    V destination = parser.apply(parts[1]);
                    graph.addEdge(source, destination);
                }
            }
        }
    }

    /**
     * выполняет топологическую сортировку графа.
     *
     * @ граф для сортировки
     * @param <V> тип вершин
     * @return список вершин в топологическом порядке
     * @throws IllegalStateException если в графе обнаружен цикл
     */
    public static <V> List<V> topologicalSort(Graph<V> graph) {
        List<V> vertices = graph.getVertices();
        Map<V, Integer> visited = new HashMap<>(); // 0 - не посещена, 1 - в процессе, 2 - посещена
        for (V v : vertices) {
            visited.put(v, 0);
        }

        LinkedList<V> result = new LinkedList<>();

        for (V vertex : vertices) {
            if (visited.get(vertex) == 0) {
                if (!dfs(vertex, graph, visited, result)) {
                    throw new IllegalStateException("Граф содержит цикл, топологическая сортировка невозможна!");
                }
            }
        }

        return result;
    }

    private static <V> boolean dfs(V vertex, Graph<V> graph, Map<V, Integer> visited, LinkedList<V> result) {
        visited.put(vertex, 1); // помечаем как «серая» (находимся в процессе обхода)

        for (V neighbor : graph.getNeighbors(vertex)) {
            int state = visited.getOrDefault(neighbor, 0);
            if (state == 1) {
                return false; // найден цикл (вернулись в вершину, которая еще обрабатывается)
            }
            if (state == 0) {
                if (!dfs(neighbor, graph, visited, result)) {
                    return false;
                }
            }
        }

        visited.put(vertex, 2); // помечаем как «черная» (полностью обработана)
        result.addFirst(vertex); // добавляем в начало результата
        return true;
    }
}