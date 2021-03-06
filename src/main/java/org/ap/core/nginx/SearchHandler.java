package org.ap.core.nginx;

import org.ap.core.Startup;
import org.ap.core.json.JsonParsingException;
import nginx.clojure.NginxHttpServerChannel;
import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.NginxJavaRequest;
import nginx.clojure.java.NginxJavaRingHandler;
import org.ap.core.search.SearchRequest;
import org.ap.core.search.SearchService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static nginx.clojure.MiniConstants.NGX_HTTP_OK;

/**
 * Created by ymetelkin on 4/28/16.
 */
public class SearchHandler implements NginxJavaRingHandler {
    private final SearchService search = Startup.getInstance().getSearch();

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        NginxJavaRequest r = ((NginxJavaRequest) request);
        NginxHttpServerChannel channel = r.handler().hijack(r, true);

        SearchRequest req = null;

        Object body = r.get(Constants.REQUEST_BODY);
        if (body != null) {
            InputStream stream = (InputStream) body;

            try {
                req = new SearchRequest(stream);
            } catch (JsonParsingException e) {
                e.printStackTrace();
            }
        }

        String json = this.search.execute(req);

        channel.sendResponse(new Object[]{
                NGX_HTTP_OK,
                Constants.JSON_CONTENT_TYPE,
                json});

        return null;
    }
}
