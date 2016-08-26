package org.ap.core.qe;

import org.ap.core.ESClientConfig;

import java.util.List;

/**
 * Created by ymetelkin on 8/22/16.
 */
public class QEConfig  implements ESClientConfig {
    private String host;
    private String cluster;
    private int port;
    private String index;
    private String type;
    private int days;
    private double sameSentenceWeight;
    private List<String> stopwords;

    public String getHost() {
        return this.host;
    }

    public QEConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getCluster() {
        return this.cluster;
    }

    public QEConfig setCluster(String cluster) {
        this.cluster = cluster;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public QEConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getIndex() {
        return this.index;
    }

    public QEConfig setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public QEConfig setType(String type) {
        this.type = type;
        return this;
    }

    public int getDays() {
        return this.days;
    }

    public QEConfig setDays(int days) {
        this.days = days;
        return this;
    }

    public double getSameSentenceWeight() {
        return this.sameSentenceWeight;
    }

    public QEConfig setSameSentenceWeight(double sameSentenceWeight) {
        this.sameSentenceWeight = sameSentenceWeight;
        return this;
    }

    public List<String> getStopwords() {
        return this.stopwords;
    }

    public QEConfig setStopwords(List<String> stopwords) {
        this.stopwords = stopwords;
        return this;
    }
}
