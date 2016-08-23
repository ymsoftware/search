package org.ap.core.search;

import org.ap.core.json.*;

import java.io.InputStream;

/**
 * Created by ymetelkin on 8/23/16.
 */
public class QERequest implements JsonVisitor {
    private String[] terms;

    public String[] getTerms() {
        return this.terms;
    }

    public QERequest setTerms(String[] terms) {
        this.terms = terms;
        return this;
    }

    public QERequest(InputStream stream) throws JsonParsingException {
        JsonParser parser = new JsonInputStreamParser(stream);
        parser.parse(this);
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals(Constants.REQUEST_QUERY)) {
            setTerms(jp.valueAsString().split(" "));
        }
    }
}
