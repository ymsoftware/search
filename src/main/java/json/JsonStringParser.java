package json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuri on 4/24/16.
 */
public class JsonStringParser {
    private int position = 0;
    private int depth = 0;
    private boolean isField = false;
    private boolean isArray = false;
    private String[] field = null;
    private char[] chars = null;
    private List array = null;
    private JsonVisitor visitor = null;

    public void parse(String json, JsonVisitor visitor) throws JsonParsingException {
        if (json == null || json.length() == 0) {
            return;
        }

        this.chars = json.toCharArray();
        this.visitor = visitor;
        this.position = 0;
        this.depth = 0;
        this.field = new String[8];

        while (this.position < this.chars.length) {
            char chr = this.chars[this.position++];

            switch (chr) {
                case '{':
                    this.depth++;
                    this.isField = false;
                    break;
                case '}':
                    this.depth--;
                    this.isField = false;
                    break;
                case '[':
                    this.isArray = true;
                    this.array = new ArrayList();
                    break;
                case ']':
                    if (this.isField) {
                        JsonProperty jp = new JsonProperty(getField(), this.array.toArray());
                        this.visitor.visit(jp);
                    }
                    this.isArray = false;
                    this.isField = false;
                    break;
                case 'f':
                    parseFalse();
                    break;
                case 't':
                    parseTrue();
                    break;
                case 'n':
                    parseNull();
                    break;
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    parseNumber();
                    break;
                case '"':
                    parseQuotes();
                    break;
                case ':':
                    this.isField = true;
                    this.isArray = false;
                    break;
                case ',':
                    if (!this.isArray) {
                        this.isField = false;
                    }
                    break;
            }
        }
    }

    private void parseQuotes() {
        boolean isEnd = false;

        StringBuilder sb = new StringBuilder();
        sb.append(this.chars[this.position++]);

        while (!isEnd) {
            char chr = this.chars[this.position++];
            if (chr == '"') {
                int idx = sb.length() - 1;
                if (sb.charAt(idx) == '\\') {
                    sb.deleteCharAt(idx);
                    sb.append(chr);
                } else {
                    isEnd = true;
                }
            } else {
                sb.append(chr);
            }
        }

        String value = sb.toString();

        if (this.isField) {
            if (this.isArray) {
                this.array.add(value);
            } else {
                JsonProperty jp = new JsonProperty(getField(), value);
                this.visitor.visit(jp);
                this.isField = false;
            }
        } else {
            this.field[this.depth - 1] = value;
        }
    }

    private void parseNumber() throws JsonParsingException {
        boolean isNumber = true;
        boolean isDecimal = false;
        this.position -= 1;

        StringBuilder sb = new StringBuilder();
        char chr = this.chars[this.position++];
        sb.append(chr);
        boolean isNegative = chr == '-';
        boolean isZero = chr == '0';

        while (isNumber) {
            chr = this.chars[this.position++];
            switch (chr) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (isZero) throw JsonParsingException.UNEXPECTED_CHARACTER(chr, "number", this.position);
                    sb.append(chr);
                    break;
                case '0':
                    if (isZero) throw JsonParsingException.UNEXPECTED_CHARACTER(chr, "number", this.position);
                    isZero = true;
                    sb.append(chr);
                    break;
                case '.':
                    if (isDecimal) throw JsonParsingException.UNEXPECTED_CHARACTER(chr, "number", this.position);

                    isZero = false;
                    isDecimal = true;
                    sb.append(chr);
                    break;
                default:
                    isNumber = false;
            }
        }

        this.position--;

        Number number = null;
        if (isDecimal) {
            number = Double.parseDouble(sb.toString());
        } else {
            number = Long.parseLong(sb.toString());
        }

        if (this.isArray) {
            this.array.add(number);
        } else {
            JsonProperty jp = new JsonProperty(getField(), number);
            this.visitor.visit(jp);
            this.isField = false;
        }
    }

    private void parseTrue() throws JsonParsingException {
        match(new char[]{'r', 'u', 'e'}, true); //true
    }

    private void parseFalse() throws JsonParsingException {
        match(new char[]{'a', 'l', 's', 'e'}, false); //false
    }

    private void parseNull() throws JsonParsingException {
        match(new char[]{'u', 'l', 'l'}, null); //null
    }

    private void match(char[] test, Object value) throws JsonParsingException {
        for (int i = 0; i < test.length; i++) {
            char actual = this.chars[this.position++];
            char expected = test[i];
            if (actual != expected) {
                throw JsonParsingException.UNEXPECTED_CHARACTER(actual, expected, this.position);
            }
        }

        if (this.isArray) {
            this.array.add(value);
        } else {
            JsonProperty jp = new JsonProperty(getField(), value);
            this.visitor.visit(jp);
            this.isField = false;
        }
    }

    private String getField() {
        if (this.depth == 1) {
            return this.field[0];
        } else {
            StringBuilder sb = new StringBuilder(this.depth * 2 - 1);
            for (int i = 0; i < this.depth; i++) {
                if (i > 0) sb.append('.');
                sb.append(this.field[i]);
            }
            return sb.toString();
        }
    }
}
