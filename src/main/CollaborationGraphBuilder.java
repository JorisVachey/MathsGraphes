package main;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CollaborationGraphBuilder {

    public static Graph<String, DefaultEdge> buildCollaborationGraph(String filePath) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Gson gson = new Gson();

        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            reader.beginArray();
            while (reader.hasNext()) {
                Film film = gson.fromJson(reader, Film.class);

                Set<String> participants = new HashSet<>();
                if (film.cast != null) participants.addAll(cleanNames(film.cast));
                //if (film.directors != null) participants.addAll(cleanNames(film.directors));
                //if (film.producers != null) participants.addAll(cleanNames(film.producers));

                for (String person : participants) {
                    graph.addVertex(person);
                }

                // Connect all participants to each other (clique)
                List<String> participantList = new ArrayList<>(participants);
                for (int i = 0; i < participantList.size(); i++) {
                    for (int j = i + 1; j < participantList.size(); j++) {
                        graph.addEdge(participantList.get(i), participantList.get(j));
                    }
                }
            }
            reader.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    // Nettoie les noms Wikipédia de la forme [[Nom]] ou [[Nom|Alias]]
    private static List<String> cleanNames(List<String> rawNames) {
        List<String> result = new ArrayList<>();
        for (String raw : rawNames) {
            if (raw.contains("[[")) {
                String clean = raw.replaceAll("\\[\\[([^\\]|]+)(\\|([^\\]]+))?\\]\\]", "$3").trim();
                if (clean.isEmpty()) {
                    clean = raw.replaceAll("\\[\\[([^\\]|]+)\\]\\]", "$1");
                }
                result.add(clean);
            } else {
                result.add(raw.trim());
            }
        }
        return result;
    }
}

