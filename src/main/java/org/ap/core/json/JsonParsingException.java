package org.ap.core.json;

/**
 * Created by yuri on 4/24/16.
 */
public class JsonParsingException extends Exception {

    public JsonParsingException(String message) {
        super(message);
    }

    public static JsonParsingException UNEXPECTED_CHARACTER(char actual, char expected, int position) {
        return new JsonParsingException(String.format("Invalid character '%c' at %d; '%c' is expected", position, expected));
    }

    public static JsonParsingException UNEXPECTED_CHARACTER(char actual, String expected, int position) {
        return new JsonParsingException(String.format("Invalid character '%c' at %d; %s is expected", position, expected));
    }
}
