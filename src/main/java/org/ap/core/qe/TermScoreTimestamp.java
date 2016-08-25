package org.ap.core.qe;

import org.joda.time.DateTime;

/**
 * Created by ymetelkin on 8/25/16.
 */
public class TermScoreTimestamp extends TermScore {
    private DateTime timestamp;

    public DateTime getTimestamp() {
        return this.timestamp;
    }

    public TermScoreTimestamp setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TermScoreTimestamp() {
    }

    public TermScoreTimestamp(String term, double score, DateTime timestamp) {
        super(term, score);
        this.timestamp = timestamp;
    }
}
