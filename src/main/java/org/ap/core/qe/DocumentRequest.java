package org.ap.core.qe;

import org.ap.core.json.*;
import org.joda.time.DateTime;

import java.io.InputStream;

/**
 * Created by ymetelkin on 8/25/16.
 */
public class DocumentRequest implements JsonVisitor {
    private String type;
    private DateTime timestamp;
    private String headline;
    private String title;

    public String getType() {
        return this.type;
    }

    public DocumentRequest setType(String type) {
        this.type = type;
        return this;
    }

    public DateTime getTimestamp() {
        return this.timestamp;
    }

    public DocumentRequest setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getHeadline() {
        return this.headline;
    }

    public DocumentRequest setHeadline(String headline) {
        this.headline = headline;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public DocumentRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public DocumentRequest(InputStream stream) throws JsonParsingException {
        JsonParser parser = new JsonInputStreamParser(stream);
        parser.parse(this);
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals(Constants.APPL_TYPE)) {
            setType(jp.valueAsString());
        } else if (jp.field.equals(Constants.APPL_TIMESTAMP)) {
            setTimestamp(jp.valueAsDateTime());
        } else if (jp.field.equals(Constants.APPL_HEADLINE)) {
            setHeadline(jp.valueAsString());
        } else if (jp.field.equals(Constants.APPL_TITLE)) {
            setTitle(jp.valueAsString());
        }
    }
}
