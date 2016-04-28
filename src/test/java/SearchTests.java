import json.JsonParser;
import json.JsonParsingException;
import json.JsonStringParser;
import org.junit.Assert;
import org.junit.Test;
import search.SearchConfig;
import search.SearchRequest;

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
        Assert.assertTrue(config.getIndex().equals("appl"));
        Assert.assertTrue(config.getType().equals("doc"));
    }

    @Test
    public void testRequest() throws JsonParsingException {
        String json = "{ \"query\": \"+messi +barcelona\" }";
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        SearchRequest request = new SearchRequest(stream);
        Assert.assertTrue(request.getQuery().equals("+messi +barcelona"));
    }
}
