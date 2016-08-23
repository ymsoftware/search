package org.ap.core.search;

import org.ap.core.json.JsonProperty;
import org.ap.core.json.JsonVisitor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ymetelkin on 8/22/16.
 */
public class QEResults implements JsonVisitor {
    private Map<String, Double> scores;
    private String term;

    public QEResults() {
        this.scores = new HashMap<String, Double>();
    }

    public List<Map.Entry<String, Double>> getScores(String[] terms) {
        Comparator<Map.Entry<String, Double>> sort = (entry1, entry2) -> {
            Double v2 = entry2.getValue();
            Double v1 = entry1.getValue();
            return v2.compareTo(v1);
        };

        List<Map.Entry<String, Double>> results = this.scores
                .entrySet()
                .stream()
                .filter(e -> Unique(terms, e.getKey()))
                .sorted(sort)
                .limit(10)
                .collect(Collectors.toList());

        int size = results.size();
        if (size > 0) {
            Double lcd = results.get(size - 1).getValue();

            results.forEach(e -> {
                Double value = e.getValue() / lcd;
                e.setValue(Math.sqrt(value));
            });
        }

        return results;
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals(Constants.QE_TERM)) {
            this.term = jp.valueAsString();
        } else if (jp.field.equals(Constants.QE_SCORE)) {
            Double score = jp.valueAsDouble();
            score = score * score;
            if (this.scores.containsKey(this.term)) {
                score = this.scores.get(this.term) + score;
            }
            this.scores.put(this.term, score);
        }
    }

    private boolean Unique(String[] terms, String term) {
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].compareTo(term) == 0) {
                return false;
            }
        }

        return true;
    }
}
