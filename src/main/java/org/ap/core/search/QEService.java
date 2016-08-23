package org.ap.core.search;

import org.ap.core.json.JsonInputStreamParser;
import org.ap.core.json.JsonParser;
import org.ap.core.json.JsonParsingException;
import org.ap.core.json.JsonStringParser;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ymetelkin on 8/22/16.
 */
public class QEService {
    private final Client client;
    private final QEConfig config;

    public QEService(Client client, QEConfig config) {
        this.client = client;
        this.config = config;
    }

    public String execute(QERequest request) {
        String index = this.config.getIndex();
        String type = this.config.getType();
        String[] terms = request.getTerms();
        boolean keepScores = request.getScores();

        MultiGetRequestBuilder builder = this.client.prepareMultiGet();
        for (int i = 0; i < terms.length; i++) {
            builder = builder.add(index, type, terms[i]);
        }

        MultiGetResponse response = builder.get();
        QEResults results = new QEResults();

        for (MultiGetItemResponse item : response) {
            GetResponse resp = item.getResponse();
            if (resp.isExists()) {
                String json = resp.getSourceAsString();
                JsonParser parser = new JsonStringParser(json);
                try {
                    parser.parse(results);
                } catch (JsonParsingException e) {
                    e.printStackTrace();
                }
            }
        }

        List<Map.Entry<String, Double>> scores = results.getScores(terms);

        StringBuilder sb = new StringBuilder();

        if (scores.size() > 0) {
            String should = null;

            sb
                    .append("{\"query\":{\"bool\":{\"must\":{\"query_string\":{\"query\":\"")
                    .append(String.join(" ", terms))
                    .append("\"}}},\"should\":");

            if (keepScores) {
                Map<Integer, List<String>> map = new HashMap<>();

                scores.forEach(e -> {
                    Integer score = e.getValue().intValue();
                    List<String> list = map.containsKey(score) ? map.get(score) : new ArrayList<String>();
                    list.add(e.getKey());
                    map.put(score, list);
                });

                sb.append("[");

                map.keySet().forEach(e -> {
                    sb.append("{\"query_string\":{\"query\":\"");

                    List<String> list = map.get(e);
                    StringBuilder sb2 = new StringBuilder();
                    list.forEach(t -> {
                        sb2.append(t).append(" ");
                    });

                    sb.append(sb2.toString().trim()).append("\"");

                    if (e > 1) {
                        sb.append(",\"boost\":").append(e);
                    }
                    sb.append("}},");
                });

                sb.deleteCharAt(sb.length() - 1);
                sb.append("]}}");
            } else {
                sb
                        .append("{\"query_string\":{\"query\":\"")
                        .append(String.join(" ", scores.stream().map(e -> e.getKey()).collect(Collectors.toList())))
                        .append("\"}}}}");
            }
        } else {
            sb
                    .append("{\"query\":{\"query_string\":{\"query\":\"")
                    .append(String.join(" ", terms))
                    .append("\"}}}");
        }

        return sb.toString();
    }
}
