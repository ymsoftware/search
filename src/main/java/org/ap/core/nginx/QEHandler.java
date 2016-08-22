package org.ap.core.nginx;

import nginx.clojure.NginxHttpServerChannel;
import nginx.clojure.java.NginxJavaRequest;
import nginx.clojure.java.NginxJavaRingHandler;
import org.ap.core.Startup;
import org.ap.core.search.QEService;

import java.io.IOException;
import java.util.Map;

import static nginx.clojure.MiniConstants.NGX_HTTP_OK;

/**
 * Created by ymetelkin on 8/22/16.
 */
public class QEHandler implements NginxJavaRingHandler {
    private final QEService qe = Startup.getInstance().getQE();

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        NginxJavaRequest r = ((NginxJavaRequest) request);
        NginxHttpServerChannel channel = r.handler().hijack(r, true);

        //SearchRequest req = null;

        Object uri = r.get(Constants.REQUEST_URI);
//        if (body != null) {
//            InputStream stream = (InputStream) body;
//
//            try {
//                req = new SearchRequest(stream);
//            } catch (JsonParsingException e) {
//                e.printStackTrace();
//            }
//        }

        //String json = this.qe.execute(req);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        r.entrySet().stream().forEach(e -> {
            sb.append("\"").append(e.getKey()).append("\":\"").append(e.getValue()).append("\",");
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        String json = sb.toString();

        channel.sendResponse(new Object[]{
                NGX_HTTP_OK,
                Constants.JSON_CONTENT_TYPE,
                json});

        return null;
    }
}
