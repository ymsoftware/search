package org.ap.core.search;

import org.ap.core.json.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by yuri on 4/28/16.
 */
public class SearchRequest implements JsonVisitor {
    private String query;
    private String[] mediaTypes;
    private String[] fields;
    private int from;
    private int size;

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

    public int getFrom() {
        return this.from;
    }

    public SearchRequest setFrom(int from) {
        this.from = from;
        return this;
    }

    public String[] getFields() {
        return this.fields;
    }

    public SearchRequest setFields(String[] fields) {
        this.fields = fields;
        return this;
    }

    public int getSize() {
        return this.size;
    }

    public SearchRequest setSize(int size) {
        this.size = size;
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
            setQuery(jp.valueAsString());
        } else if (jp.field.equals("media_types")) {
            setMediaTypes(jp.valueAsStringArray());
        } else if (jp.field.equals("fields")) {
            setFields(jp.valueAsStringArray());
        } else if (jp.field.equals("from")) {
            setFrom(jp.valueAsInt());
        } else if (jp.field.equals("size")) {
            setSize(jp.valueAsInt());
        }
    }
}
