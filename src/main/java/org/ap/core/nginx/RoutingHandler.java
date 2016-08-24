package org.ap.core.nginx;

import nginx.clojure.java.NginxJavaRingHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuri on 5/1/16.
 */
public class RoutingHandler implements NginxJavaRingHandler {
    Map<String, NginxJavaRingHandler> handlers = new HashMap<String, NginxJavaRingHandler>();

    public RoutingHandler() {
        handlers.put(Constants.API_SEARCH_URI, new SearchHandler());
        handlers.put(Constants.API_SEARCH_QUERY_URI, new SearchQueryHandler());
        handlers.put(Constants.API_SEARCH_QUERY_ENHANCER, new QEHandler());
    }

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        String uri = request.get(Constants.REQUEST_URI).toString();
        while (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        return handlers.get(uri).invoke(request);
    }
}
