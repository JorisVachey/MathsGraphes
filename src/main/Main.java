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
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ExportException;
import java.io.FileWriter;


public class Main {
    public static void main(String[] args) {
        Graph<String, DefaultEdge> graph = CollaborationGraphBuilder.buildCollaborationGraph("./src/donnees/data_100.json");
        System.out.println("Nombre de personnes : " + graph.vertexSet().size());
        System.out.println("Nombre de collaborations : " + graph.edgeSet().size());
        
        int i = 0;

        for (String s : graph.vertexSet())
        {
            if (s.equals("Al Pacino")){i++;}
        }
        System.out.println("i = " + i);

        /*
        Set<String> inactifs = new HashSet<>();
		for( String v : graph.vertexSet()){
			if(graph.degreeOf(v)<30)
				inactifs.add(v);
		}
		graph.removeAllVertices(inactifs);
        */

        System.out.println(graph.vertexSet());
        System.out.println("Nombre de personnes : " + graph.vertexSet().size());
        System.out.println("Nombre de collaborations : " + graph.edgeSet().size());
        //System.out.println(getNeighborsOf(graph, "Al Pacino"));
        System.out.println(collaborateursCommun(graph, "Al Pacino", "Austin Butler"));
        /* 
        try{
        afficheGraphe(graph);
        }
        catch (IOException e) {System.out.println("ERREUR");}
        */

        System.out.println(centralite(graph, "Leonardo DiCaprio", 8));
    
		

    }
    public static Set<String> getNeighborsOf(Graph<String, DefaultEdge> graph, String p)
    {
        Set<String> set = new HashSet<>();

        for (DefaultEdge e : graph.edgeSet())
        {
            if (graph.getEdgeSource(e).equals(p)) {set.add(graph.getEdgeTarget(e));}
            else if (graph.getEdgeTarget(e).equals(p) ){set.add(graph.getEdgeSource(e));}
        }
        return set;
    }


    public static Set<String> collaborateursCommun(Graph<String, DefaultEdge> graph, String p1, String p2)
    {
        Set<String> neighbors1 = getNeighborsOf(graph, p1);
        Set<String> neighbors2 = getNeighborsOf(graph, p2);

        Set<String> collabo = new HashSet<>();

        for (String p : neighbors1)
        {
            if (neighbors2.contains(p))
            {
                collabo.add(p);
            }
        }

        return collabo;
    }


    public static Set<String> collaborateursProches(Graph<String, DefaultEdge> graph, String u, int k) {
        if (!graph.containsVertex(u)) {
            System.out.println(u + " est un illustre inconnu");
            return null;
        }
        Set<String> collaborateurs = new HashSet<>();
        collaborateurs.add(u);

        for (int i = 1; i <= k; i++) {
            Set<String> collaborateursDirects = new HashSet<>();
            for (String c : collaborateurs) {
                for (DefaultEdge edge : graph.edgesOf(c)) {
                    String v = graph.getEdgeSource(edge).equals(c) ? graph.getEdgeTarget(edge) : graph.getEdgeSource(edge);
                    if (!collaborateurs.contains(v)) {
                        collaborateursDirects.add(v);
                    }
                }
            }
            collaborateurs.addAll(collaborateursDirects);
        }
        return collaborateurs;
    }

    public static Integer centralite(Graph<String, DefaultEdge> graph, String u, int k) {
        if (!graph.containsVertex(u)) {
            System.out.println(u + " est un illustre inconnu");
            return null;
        }
        Set<String> collaborateurs = new HashSet<>();
        collaborateurs.add(u);
        int dist = 0;

        for (int i = 1; i <= k; i++) {
            Set<String> collaborateursDirects = new HashSet<>();
            for (String c : collaborateurs) {
                for (DefaultEdge edge : graph.edgesOf(c)) {
                    String v = graph.getEdgeSource(edge).equals(c) ? graph.getEdgeTarget(edge) : graph.getEdgeSource(edge);
                    if (!collaborateurs.contains(v)) {
                        collaborateursDirects.add(v);
                        if (dist < i){dist = i;}
                    }
                }
            }
            collaborateurs.addAll(collaborateursDirects);
        }
        return dist;
    }

    public static String centraliteMin(Graph<String, DefaultEdge> graph)
    {
        Integer min = null;
        String name = null;
        for (String s : graph.vertexSet())
        {
            if (min == null)
            {
                min = centralite(graph, s, 8);
                name = s;
            }
            else {
                int c = centralite(graph, s, 8);
                if (c < min)
                {
                    min = c;
                    name = s;
                }
            }
        }  
        return name;
    }

    public static String centraliteMax(Graph<String, DefaultEdge> graph)
    {
        Integer max = null;
        String name = null;
        for (String s : graph.vertexSet())
        {
            if (max == null)
            {
                max = centralite(graph, s, 8);
                name = s;
            }
            else {
                int c = centralite(graph, s, 8);
                if (c > max)
                {
                    max = c;
                    name = s;
                }
            }
        }  
        return name;
    }

    public static void afficheGraphe(Graph<String, DefaultEdge> graph) throws IOException
    {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        try (FileWriter writer = new FileWriter("/home/iut45/Etudiants/o22401713/Semestre2/SAE_semestre2/MathsGraphes/src/main/graph.dot")) {
        exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
        exporter.exportGraph(graph, new FileWriter("./src/main/graph.dot"));
            System.out.println("Graph exported to DOT format successfully.");
        } catch (ExportException | IOException e) {
            System.err.println("Error exporting graph to DOT format: " + e.getMessage());
        }
    }

    
}

