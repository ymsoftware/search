package org.ap.core.qe;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ymetelkin on 8/25/16.
 */
public class QETerm {
    private String term;
    private List<TermScore> terms;
    private List<TermScoreTimestamp> history;

    public String getTerm() {
        return this.term;
    }

    public QETerm setTerm(String term) {
        this.term = term;
        return this;
    }

    public List<TermScore> getTerms() {
        return this.terms;
    }

    public QETerm setTerms(List<TermScore> terms) {
        this.terms = terms;
        return this;
    }

    public List<TermScoreTimestamp> getHistory() {
        return this.history;
    }

    public QETerm setHistory(List<TermScoreTimestamp> history) {
        this.history = history;
        return this;
    }

    public QETerm() {
    }

    public QETerm(String term) {
        this.term = term;
    }

    public boolean isEmtpy() {
        return this.term == null
                || this.term.length() == 0
                || this.history == null
                || this.history.size() == 0;
    }

    public QETerm merge(QETerm term) {
        if (!term.isEmtpy() && this.term.equals(term.getTerm())) {
            if (this.history == null) {
                return setHistory(term.getHistory());
            } else {
                this.history.addAll(term.getHistory());
            }
        }
        return this;
    }

    public QETerm calculate(DateTime timestamp, int days) {
        if (!isEmtpy()) {
            Map<String, Double> scores = new HashMap<>();
            List<TermScoreTimestamp> history = new ArrayList<>();

            for (TermScoreTimestamp h : this.history) {
                String term = h.getTerm();

                if (this.term.compareTo(term) != 0) {
                    double total = Hours.hoursBetween(timestamp, h.getTimestamp()).getHours() / 24;
                    if (total < days) {
                        history.add(h);

                        double diff = days / 2 - total;
                        double ratio = 1 / (1 + Math.exp(-diff));
                        double score = h.getScore() * ratio;

                        if (scores.containsKey(term)) {
                            score += scores.get(term);
                        }
                        scores.put(term, score);
                    }
                }
            }

            Comparator<TermScore> sort = (term1, term2) -> {
                Double v2 = term2.getScore();
                Double v1 = term1.getScore();
                return v2.compareTo(v1);
            };

            List<TermScore> terms = scores
                    .entrySet()
                    .stream()
                    .map(e -> new TermScore(e.getKey(), e.getValue()))
                    .sorted(sort)
                    .limit(10)
                    .collect(Collectors.toList());

            setTerms(terms);
            setHistory(history);
        }

        return this;
    }

    public QETerm addHistory(String term, double score, DateTime timestamp) {
        if (this.history == null) {
            this.history = new ArrayList<>();
        }
        this.history.add(new TermScoreTimestamp(term, score, timestamp));
        return this;
    }

    public QETerm addTerm(String term, double score) {
        if (this.terms == null) {
            this.terms = new ArrayList<>();
        }
        this.terms.add(new TermScore(term, score));
        return this;
    }
}
