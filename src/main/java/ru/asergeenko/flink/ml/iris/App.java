package ru.asergeenko.flink.ml.iris;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.Vertex;
import org.apache.flink.graph.library.LabelPropagation;
import org.apache.flink.types.NullValue;

public class App {
    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // Read graph
        Graph<Integer, NullValue, NullValue> socialGraph = Graph.fromCsvReader("src/main/resources/facebook_combined.csv", env).keyType(Integer.class);
        socialGraph.getVertices().print();
        socialGraph.getEdges().print();

        // Community Detection
        DataSet<Vertex<Integer, NullValue>> communities = socialGraph.run(new LabelPropagation<Integer, NullValue, NullValue>(50));
        communities.print();

    }
}
