package org.ap.core;

import org.ap.core.json.*;
import org.ap.core.search.SearchConfig;

import java.io.InputStream;

import static java.lang.Math.toIntExact;

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
        if (jp.field.equals("search.host")) {
            this.searchConfig.setHost(jp.valueAsString());
        } else if (jp.field.equals("search.cluster")) {
            this.searchConfig.setCluster(jp.valueAsString());
        }else if (jp.field.equals("search.port")) {
            this.searchConfig.setPort(jp.valueAsInt());
        }else if (jp.field.equals("search.index")) {
            this.searchConfig.setIndex(jp.valueAsString());
        } else if (jp.field.equals("search.type")) {
            this.searchConfig.setType(jp.valueAsString());
        }
    }
}
