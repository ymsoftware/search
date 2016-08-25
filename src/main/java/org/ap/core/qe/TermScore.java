package org.ap.core.qe;

/**
 * Created by ymetelkin on 8/25/16.
 */
public class TermScore {
    private String term;
    private double score;

    public String getTerm() {
        return this.term;
    }

    public TermScore setTerm(String term) {
        this.term = term;
        return this;
    }

    public double getScore() {
        return this.score;
    }

    public TermScore setScore(double score) {
        this.score = score;
        return this;
    }

    public TermScore() {
    }

    public TermScore(String term, double score) {
        this.term = term;
        this.score = score;
    }
}
