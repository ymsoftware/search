package org.ap.core.json;

/**
 * Created by yuri on 4/23/16.
 */
public class JsonProperty {
    public String field;
    public Object value;

    public JsonProperty() {

    }

    public JsonProperty(String field, Object value) {
        this.field = field;
        this.value = value;
    }
}
