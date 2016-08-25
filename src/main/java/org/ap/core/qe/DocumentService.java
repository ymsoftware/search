package org.ap.core.qe;

import org.ap.core.json.*;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.joda.time.DateTime;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ymetelkin on 8/25/16.
 */
public class DocumentService {
    private static final char[] REMOVE = "`!@#$%(){}[]\\:;\"<>?/".toCharArray();
    private static final char[] SPACES = "~_-=+&.^,'".toCharArray();

    private final Client client;
    private final QEConfig config;

    public DocumentService(Client client, QEConfig config) {
        this.client = client;
        this.config = config;
    }

    public String execute(DocumentRequest request) {
        String index = this.config.getIndex();
        //String type = this.config.getType();
        int days = this.config.getDays();

        Map<String, QETerm> terms = parse(request);
        if (terms != null) {
            DateTime ts = DateTime.now();

            for (String key : terms.keySet()) {
                QETerm term = terms.get(key);

                List<TermScoreTimestamp> history = getHistory(index, "history", key);
                if (history != null) {
                    List<TermScoreTimestamp> h = term.getHistory();
                    if (h == null) {
                        h = history;
                    } else {
                        h.addAll(history);
                    }
                    term.setHistory(h);
                }

                term.calculate(ts, days);
            }
        }

        return null;
    }

    private Map<String, QETerm> parse(DocumentRequest request) {
        Map<String, QETerm> terms = new HashMap<>();

        DateTime timestamp = request.getTimestamp();
        String headline = request.getHeadline();
        String title = request.getTitle();

        addTerms(terms, parseField(headline), 1);
        addTerms(terms, parseField(title), 1);

        if (terms.size() == 0) return null;

        DateTime ts = timestamp == null ? DateTime.now() : timestamp;

        for (String key : terms.keySet()) {
            Map<String, Double> scores = new HashMap<>();

            QETerm term = terms.get(key);
            for (TermScoreTimestamp h : term.getHistory()) {
                String t = h.getTerm();
                double score = scores.containsKey(t) ? scores.get(t) : 0;
                double s = h.getScore();
                score += s * s;
                scores.put(t, score);
            }

            List<TermScoreTimestamp> history = scores
                    .entrySet()
                    .stream()
                    .map(e -> new TermScoreTimestamp(e.getKey(), Math.sqrt(e.getValue()), ts))
                    .collect(Collectors.toList());

            term.setHistory(history);
        }

        return terms;
    }

    private List<String> getTokens(String sentence) {
        if (sentence == null || sentence.length() == 0) return null;

        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char c : sentence.toCharArray()) {
            if (c == ' ' || contains(SPACES, c)) {
                addToken(tokens, sb);
                sb = new StringBuilder();
            } else if (!contains(REMOVE, c) && Character.isLetter(c)) {
                sb.append(c);
            }
        }

        addToken(tokens, sb);

        return tokens.size() == 0 ? null : tokens;
    }

    private List<List<String>> parseField(String value) {
        if (value == null) return new ArrayList<List<String>>();

        String[] sentences = value
                .toLowerCase()
                .replace("<p>", ".")
                .replace("</p>", ".")
                .replace("<p />", ".")
                .replace("<p/>", ".")
                .split("\\.");

        return Arrays.stream(sentences)
                .map(e -> getTokens(e))
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }

    private boolean contains(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) return true;
        }
        return false;
    }

    private void addToken(List<String> tokens, StringBuilder sb) {
        if (sb.length() > 2) {
            String token = sb.toString();
            if (!this.config.getStopwords().contains(token) && !tokens.contains(token)) {
                tokens.add(token);
            }
        }
    }

    private void addTerms(Map<String, QETerm> terms, List<List<String>> sentences, double score) {
        for (List<String> sentence : sentences) {
            for (String token : sentence) {
                QETerm term = terms.containsKey(token) ? terms.get(token) : new QETerm(token);

                for (String t : sentence) {
                    if (t.compareTo(token) != 0) {
                        term.addHistory(t, score, DateTime.now());
                    }
                }

                terms.put(token, term);
            }
        }
    }

    private List<TermScoreTimestamp> getHistory(String index, String type, String id) {
        GetResponse response = client.prepareGet(index, type, id).get();

        Object test = response.getSource().get("terms");
        if (test == null) return null;

        List<TermScoreTimestamp> history = ((List<Map<String, Object>>) test)
                .stream()
                .map(e -> {
                    String term = (String) e.get("term");
                    double score = (double) e.get("score");
                    DateTime ts = DateTime.parse((String) e.get("timestamp"));
                    return new TermScoreTimestamp(term, score, ts);
                })
                .collect(Collectors.toList());
        return history;
    }

    private String getTermBulkRequest(QETerm term) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }
}
