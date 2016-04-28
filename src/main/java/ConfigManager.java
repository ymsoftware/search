import json.*;
import search.SearchConfig;

import java.io.InputStream;

/**
 * Created by yuri on 4/28/16.
 */
public class ConfigManager implements JsonVisitor {
    private SearchConfig searchConfig;

    public SearchConfig getSearchConfig() {
        return this.searchConfig;
    }

    public void load(String resourcePath) throws JsonParsingException {
        this.searchConfig = new SearchConfig();

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
        JsonParser parser = new JsonInputStreamParser(stream);
        parser.parse(this);
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals("search.index")) {
            this.searchConfig.setIndex((String) jp.value);
        } else if (jp.field.equals("search.type")) {
            this.searchConfig.setType((String) jp.value);
        }
    }
}
