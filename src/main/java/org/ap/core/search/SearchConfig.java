package org.ap.core.search;

/**
 * Created by yuri on 4/27/16.
 */
public class SearchConfig {
    private String host;
    private String cluster;
    private int port;
    private String index;
    private String type;

    public String getHost() {
        return this.host;
    }

    public SearchConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getCluster() {
        return this.cluster;
    }

    public SearchConfig setCluster(String cluster) {
        this.cluster = cluster;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public SearchConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getIndex() {
        return this.index;
    }

    public SearchConfig setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public SearchConfig setType(String type) {
        this.type = type;
        return this;
    }
}
