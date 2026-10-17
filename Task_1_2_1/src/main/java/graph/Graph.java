package graph;

import java.io.IOException;
import java.util.List;

//общий интерфейс для работы с ориентированным/неориентированным графом
public interface Graph<V> {

    //добавляет вершину в граф
    void addVertex(V vertex);

    //удаляет вершину и все связанные с ней рёбра
    void removeVertex(V vertex);

    //добавляет ребро между двумя вершинами
    void addEdge(V source, V destination);

    //удаляет ребро между двумя вершинами
    void removeEdge(V source, V destination);

    //возвращает список всех "соседей" (выходящих вершин) для данной вершины
    List<V> getNeighbors(V vertex);

    //возвращает список всех вершин графа
    List<V> getVertices();

    //считывает граф из файла определенного формата
    void readFromFile(String filepath) throws IOException;
}