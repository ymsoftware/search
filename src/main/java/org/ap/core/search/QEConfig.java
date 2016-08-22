package org.ap.core.search;

/**
 * Created by ymetelkin on 8/22/16.
 */
public class QEConfig {
    private String host;
    private String cluster;
    private int port;
    private String index;
    private String type;

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
}
