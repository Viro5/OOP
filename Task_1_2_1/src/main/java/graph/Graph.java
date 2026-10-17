package graph;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface representing a directed graph with vertices of type T.
 *
 * @param <T> the type of vertex
 */
public interface Graph<T> {

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the vertex to add
     */
    void addVertex(T vertex);

    /**
     * Removes a vertex and all its incident edges.
     *
     * @param vertex the vertex to remove
     */
    void removeVertex(T vertex);

    /**
     * Adds a directed edge between source and destination.
     *
     * @param source      the source vertex
     * @param destination the destination vertex
     */
    void addEdge(T source, T destination);

    /**
     * Removes a directed edge between source and destination.
     *
     * @param source      the source vertex
     * @param destination the destination vertex
     */
    void removeEdge(T source, T destination);

    /**
     * Returns a collection of all vertices in the graph.
     *
     * @return collection of vertices
     */
    Collection<T> getVertices();

    /**
     * Returns a collection of neighboring vertices for a given vertex.
     *
     * @param vertex the vertex
     * @return collection of neighbors
     */
    Collection<T> getNeighbors(T vertex);

    /**
     * Reads graph edges from a file.
     *
     * @param filename the path to the file
     * @throws IOException if an I/O error occurs
     */
    void readFromFile(String filename) throws IOException;
}