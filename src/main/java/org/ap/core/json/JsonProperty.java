package org.ap.core.json;

import java.util.List;

import static java.lang.Math.toIntExact;

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

    public String valueAsString() {
        return (String) this.value;
    }

    public int valueAsInt() {
        return toIntExact((long) this.value);
    }

    public boolean valueAsBoolean() {
        return (boolean) this.value;
    }

    public String[] valueAsStringArray() {
        List list = (List) this.value;
        String[] values = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = (String) list.get(i);
        }
        return values;
    }

    public Number[] valueAsNumberArray() {
        List list = (List) this.value;
        Number[] values = new Number[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = (Number) list.get(i);
        }
        return values;
    }

    public boolean[] valueAsBooleanArray() {
        List list = (List) this.value;
        boolean[] values = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = (boolean) list.get(i);
        }
        return values;
    }

    public int[] valueAsIntArray() {
        List list = (List) this.value;
        int[] values = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = (int) list.get(i);
        }
        return values;
    }
}
