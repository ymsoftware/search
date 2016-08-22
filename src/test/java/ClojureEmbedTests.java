import nginx.clojure.embed.NginxEmbedServer;
import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.NginxJavaRingHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by yuri on 5/1/16.
 */
public class ClojureEmbedTests {
    @Test
    public void testSearchHandler() throws ClientProtocolException, IOException {
        NginxEmbedServer server = NginxEmbedServer.getServer();
        Map<String, String> opts = ArrayMap.create("port", "8080");
        //server.start("HelloHandler", opts);
        server.start("/home/yuri/nginx/nginx-clojure-0.4.4/conf/nginx.conf");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:8081/api/search");
        String resp = EntityUtils.toString(httpclient.execute(httpget).getEntity());
        Assert.assertEquals("Hello, Embeded Nginx!", resp);
        server.stop();
    }

    public static class HelloHandler implements NginxJavaRingHandler {

        @Override
        public Object[] invoke(Map<String, Object> request) throws IOException {
            return new Object[]{200, null, "Hello, Embeded Nginx!"};
        }

    }
}