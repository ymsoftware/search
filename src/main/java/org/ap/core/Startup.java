package org.ap.core;

import org.ap.core.json.JsonParsingException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.ap.core.search.SearchConfig;
import org.ap.core.search.SearchService;

import java.net.InetSocketAddress;

/**
 * Created by ymetelkin on 4/28/16.
 */
public class Startup {
    private ConfigManager config;
    private SearchService search;

    private static Startup instance = new Startup();

    public static Startup getInstance() {
        return instance;
    }

    public ConfigManager getConfig() {
        return this.config;
    }

    public SearchService getSearch() {
        return this.search;
    }

    private Startup() {
        try {
            init();
        } catch (JsonParsingException e) {
            e.printStackTrace();
        }

    }

    private void init() throws JsonParsingException {
        ConfigManager configs = new ConfigManager();
        configs.load("config.json");
        this.config = configs;

        initSearch();
    }

    private void initSearch() {
        Client client;

        SearchConfig config = this.config.getSearchConfig();

        String host = Helpers.safeTrim(config.getHost());
        if (host == null) {
            host = "127.0.0.1";
        }

        int port = config.getPort();
        if (port == 0) {
            port = 9300;
        }

        InetSocketTransportAddress address = new InetSocketTransportAddress(new InetSocketAddress(host, port));

        String cluster = Helpers.safeTrim(config.getCluster());
        if (cluster == null) {
            client = TransportClient
                    .builder()
                    .build()
                    .addTransportAddress(address);
        } else {
            Settings settings = Settings
                    .settingsBuilder()
                    .put("cluster.name", cluster)
                    .build();

            client = TransportClient
                    .builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(address);
        }

        this.search = new SearchService(client, config);
    }
}
