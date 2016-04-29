import org.ap.core.Startup;
import org.ap.core.json.JsonParsingException;
import org.ap.core.ConfigManager;
import org.ap.core.search.SearchService;
import org.junit.Assert;
import org.junit.Test;
import org.ap.core.search.SearchConfig;
import org.ap.core.search.SearchRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by yuri on 4/28/16.
 */
public class SearchTests {
    @Test
    public void testConfig() throws JsonParsingException {
        ConfigManager configs = new ConfigManager();
        configs.load("config.json");
        SearchConfig config = configs.getSearchConfig();
        Assert.assertTrue(config.getHost().equals("127.0.0.1"));
        Assert.assertTrue(config.getCluster().equals("ym"));
        Assert.assertTrue(config.getPort() == 9310);
        Assert.assertTrue(config.getIndex().equals("appl"));
        Assert.assertTrue(config.getType().equals("doc"));

        String[] fields = config.getFields();
        Assert.assertTrue(fields.length == 3);
        Assert.assertTrue(fields[0].equals("main.nitf"));
        Assert.assertTrue(fields[1].equals("headline^5"));
    }

    @Test
    public void testRequest() throws JsonParsingException {
        String json = "{ \"query\": \"+messi +barcelona\", "
                + "\"media_types\": [ \"text\", \"photo\" ], "
                + "\"entitlements\": { \"include\": [ 1, 2, 100001 ], \"exclude\": [ 3 ], \"sid_code\": \"NYT\", \"future_hours\": 2 }, "
                + "\"fields\": [ \"itemid\", \"title\" ], \"size\": 20 }";

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        SearchRequest request = new SearchRequest(stream);
        Assert.assertTrue(request.getQuery().equals("+messi +barcelona"));

        String[] types = request.getMediaTypes();
        Assert.assertTrue(types.length == 2);
        Assert.assertTrue(types[0].equals("text"));
        Assert.assertTrue(types[1].equals("photo"));

        String[] fields = request.getFields();
        Assert.assertTrue(fields.length == 2);
        Assert.assertTrue(fields[0].equals("itemid"));
        Assert.assertTrue(fields[1].equals("title"));

        int[] products = request.getIncludeProducts();
        Assert.assertTrue(products.length == 3);
        Assert.assertTrue(products[0] == 1);
        Assert.assertTrue(products[1] == 2);
        Assert.assertTrue(products[2] == 100001);

        products = request.getExcludeProducts();
        Assert.assertTrue(products.length == 1);
        Assert.assertTrue(products[0] == 3);

        String sidouts = request.getSidouts();
        Assert.assertTrue(sidouts.equals("NYT"));

        int hours = request.getFutureHours();
        Assert.assertTrue(hours == 2);

        Assert.assertTrue(request.getSize() == 20);
    }

    @Test
    public void testHandler(){
        String json = "{ \"query\": \"obama\", "
                + "\"media_types\": [ \"text\", \"photo\" ], "
                + "\"entitlements\": { \"include\": [ 1, 2 ], \"exclude\": [ 3 ], \"sid_code\": \"NYT\", \"future_hours\": 2 }, "
                + "\"fields\": [ \"itemid\", \"title\" ], \"size\": 20 }";

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        SearchRequest req = null;
        try {
            req = new SearchRequest(stream);
        } catch (JsonParsingException e) {
            e.printStackTrace();
        }

        SearchService svc = Startup.getInstance().getSearch();
        json = svc.execute(req);
    }
}
