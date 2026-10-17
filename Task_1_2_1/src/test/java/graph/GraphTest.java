package graph;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    // предоставляет все комбинации реализаций графа для тестов
    static Stream<Graph<String>> stringGraphProvider() {
        return Stream.of(
                new AdjacencyListGraph<>(),
                new AdjacencyMatrixGraph<>(),
                new IncidenceMatrixGraph<>()
        );
    }

    @ParameterizedTest
    @MethodSource("stringGraphProvider")
    public void testBasicOperations(Graph<String> graph) {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        assertTrue(graph.getVertices().contains("A"));
        assertTrue(graph.getVertices().contains("B"));
        assertTrue(graph.getVertices().contains("C"));

        assertTrue(graph.getNeighbors("A").contains("B"));
        assertFalse(graph.getNeighbors("B").contains("A"));

        graph.removeEdge("A", "B");
        assertFalse(graph.getNeighbors("A").contains("B"));

        graph.removeVertex("C");
        assertFalse(graph.getVertices().contains("C"));
    }

    @ParameterizedTest
    @MethodSource("stringGraphProvider")
    public void testTopologicalSort(Graph<String> graph) {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "C");

        List<String> sorted = GraphUtils.topologicalSort(graph);

        int indexA = sorted.indexOf("A");
        int indexB = sorted.indexOf("B");
        int indexC = sorted.indexOf("C");

        assertTrue(indexA < indexB);
        assertTrue(indexB < indexC);
    }

    @ParameterizedTest
    @MethodSource("stringGraphProvider")
    public void testTopologicalSortCycleDetection(Graph<String> graph) {
        graph.addEdge("A", "B");
        graph.addEdge("B", "A");

        assertThrows(IllegalStateException.class, () -> {
            GraphUtils.topologicalSort(graph);
        });
    }

    @ParameterizedTest
    @MethodSource("stringGraphProvider")
    public void testReadFromFile(Graph<String> graph) throws IOException {
        Path tempFile = Files.createTempFile("graph_test", ".txt");
        Files.writeString(tempFile, "Node1 Node2\nNode2 Node3\n");

        graph.readFromFile(tempFile.toString());

        assertTrue(graph.getVertices().contains("Node1"));
        assertTrue(graph.getNeighbors("Node1").contains("Node2"));

        Files.deleteIfExists(tempFile);
    }

    // отдельный тест для проверки работы с другим типом данных (например, Integer)
    @ParameterizedTest
    @MethodSource("integerGraphProvider")
    public void testIntegerGraph(Graph<Integer> graph) {
        graph.addEdge(10, 20);
        graph.addEdge(20, 30);

        assertTrue(graph.getVertices().contains(10));
        assertTrue(graph.getNeighbors(10).contains(20));

        List<Integer> sorted = GraphUtils.topologicalSort(graph);
        assertTrue(sorted.indexOf(10) < sorted.indexOf(20));
        assertTrue(sorted.indexOf(20) < sorted.indexOf(30));
    }

    static Stream<Graph<Integer>> integerGraphProvider() {
        return Stream.of(
                new AdjacencyListGraph<>(),
                new AdjacencyMatrixGraph<>(),
                new IncidenceMatrixGraph<>()
        );
    }
}