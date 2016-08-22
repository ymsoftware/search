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

    public String execute(String[] terms) {
        String index = this.config.getIndex();
        String type = this.config.getType();

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

        List<Map.Entry<String, Double>> scores = results.getScores();

        StringBuilder sb = new StringBuilder();

        if (scores.size() > 0) {
            sb
                    .append("{\"query\":{\"bool\":{\"must\":{\"query_string\":{\"query\":\"")
                    .append(String.join(" ", terms))
                    .append("\"}}},\"should\":{\"query_string\":{\"query\":\"")
                    .append(String.join(" ", scores.stream().map(e -> e.getKey()).collect(Collectors.toList())))
                    .append("\"}}}}");
        } else {
            sb
                    .append("{\"query\":{\"query_string\":{\"query\":\"")
                    .append(String.join(" ", terms))
                    .append("\"}}}");
        }

        return sb.toString();
    }
}
