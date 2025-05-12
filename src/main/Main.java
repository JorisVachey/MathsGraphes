package main;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.ExportException;
import java.io.FileWriter;


public class Main {
    public static void main(String[] args) {
        Graph<String, DefaultEdge> graph = CollaborationGraphBuilder.buildCollaborationGraph("/home/iut45/Etudiants/o22401713/Semestre2/SAE_semestre2/MathsGraphes/src/donnees/data_100.json");
        System.out.println("Nombre de personnes : " + graph.vertexSet().size());
        System.out.println("Nombre de collaborations : " + graph.edgeSet().size());
        
        
        Set<String> inactifs = new HashSet<>();
		for( String v : graph.vertexSet()){
			if(graph.degreeOf(v)<20)
				inactifs.add(v);
		}
		graph.removeAllVertices(inactifs);
        
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        try (FileWriter writer = new FileWriter("/home/iut45/Etudiants/o22401713/Semestre2/SAE_semestre2/MathsGraphes/src/main/graph.dot")) {
            exporter.exportGraph(graph, writer);
            System.out.println("Graph exported to DOT format successfully.");
        } catch (ExportException | IOException e) {
            System.err.println("Error exporting graph to DOT format: " + e.getMessage());
        }

        
		

    }
}
