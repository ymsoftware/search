package org.ap.core.nginx;

import nginx.clojure.java.NginxJavaRequest;
import nginx.clojure.java.NginxJavaRingHandler;
import org.ap.core.Startup;
import org.ap.core.json.JsonParsingException;
import org.ap.core.search.SearchRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static nginx.clojure.MiniConstants.NGX_HTTP_OK;

/**
 * Created by ymetelkin on 5/2/16.
 */
public class SearchQueryHandler implements NginxJavaRingHandler {
    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        NginxJavaRequest r = ((NginxJavaRequest) request);

        String json = null;

        Object body = r.get(Constants.REQUEST_BODY);
        if (body == null) {
            json = "{}";
        } else {
            InputStream stream = (InputStream) body;

            try {
                SearchRequest req = new SearchRequest(stream);
                json = Startup.getInstance().getSearch().getSearchRequestBuilder(req).toString();
            } catch (JsonParsingException e) {
                json = String.format("{ \"error\": \"%s\" }", e.getMessage());
            }
        }

        return new Object[]{
                NGX_HTTP_OK,
                Constants.JSON_CONTENT_TYPE,
                json};
    }
}

