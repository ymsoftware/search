package org.ap.core;

import org.ap.core.json.JsonParsingException;
import org.ap.core.qe.DocumentService;
import org.ap.core.qe.QEConfig;
import org.ap.core.qe.QEService;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.ap.core.search.SearchConfig;
import org.ap.core.search.SearchService;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ymetelkin on 4/28/16.
 */
public class Startup {
    private ConfigManager config;
    private SearchService search;
    private QEService qe;
    private DocumentService doc;

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

    public DocumentService getDocumentService() {
        return this.doc;
    }

    public QEService getQE() {
        return this.qe;
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

        //initSearch();
        //initQE();
        initDocumentService();
    }

    private void initSearch() {
        SearchConfig config = this.config.getSearchConfig();
        Client client = initClient(config);
        this.search = new SearchService(client, config);
    }

    private void initQE() {
        QEConfig config = this.config.getQEConfig();
        Client client = initClient(config);

        //List<String> stopwords=new ArrayList<>();
        //InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("stopwords.txt");

        this.qe = new QEService(client, config);
    }

    private void initDocumentService() {
        QEConfig config = this.config.getQEConfig();
        Client client = initClient(config);

        String url = ClassLoader.getSystemClassLoader().getResource("stopwords.txt").getFile();

        try (Stream<String> stream = Files.lines(Paths.get(url))) {
            config.setStopwords(stream.collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.doc = new DocumentService(client, config);
    }

    private Client initClient(ESClientConfig config) {
        Client client;

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

        return client;
    }
}
