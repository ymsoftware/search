package search;

import json.*;

import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by yuri on 4/28/16.
 */
public class SearchRequest implements JsonVisitor {
    private String query;
    private String[] mediaTypes;

    public String getQuery() {
        return this.query;
    }

    public SearchRequest setQuery(String query) {
        this.query = query;
        return this;
    }

    public String[] getMediaTypes() {
        return this.mediaTypes;
    }

    public SearchRequest setMediaTypes(String[] mediaTypes) {
        this.mediaTypes = mediaTypes;
        return this;
    }

    public SearchRequest() {

    }

    public SearchRequest(InputStream stream) throws JsonParsingException {
        JsonParser parser = new JsonInputStreamParser(stream);
        parser.parse(this);
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals("query")) {
            setQuery((String) jp.value);
        } else if (jp.field.equals("media_types")) {
            Object[] values = (Object[]) jp.value;
            setMediaTypes(Arrays.copyOf(values, values.length, String[].class));
        }
    }
}
