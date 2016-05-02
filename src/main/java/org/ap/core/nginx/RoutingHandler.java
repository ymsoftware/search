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
    }

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        return handlers.get(request.get(Constants.REQUEST_URI)).invoke(request);
    }
}
