package json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuri on 4/26/16.
 */
public abstract class JsonParser {
    private int depth = 0;
    private boolean isField = false;
    private boolean isArray = false;
    private String[] field = null;
    private List array = null;
    private JsonVisitor visitor = null;

    protected int position = 0;

    public abstract void parse(JsonVisitor visitor) throws JsonParsingException;

    protected abstract char next();

    protected void process(char chr) throws JsonParsingException {
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
                parseNumber(chr);
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

    protected void init(JsonVisitor visitor) {
        this.visitor = visitor;
        this.position = 0;
        this.depth = 0;
        this.field = new String[8];
    }

    private void parseQuotes() {
        boolean isEnd = false;

        StringBuilder sb = new StringBuilder();

        while (!isEnd) {
            char chr = next();
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

    private void parseNumber(char first) throws JsonParsingException {
        boolean isNumber = true;
        boolean isDecimal = false;

        StringBuilder sb = new StringBuilder();
        sb.append(first);
        boolean isNegative = first == '-';
        boolean isZero = first == '0';

        char chr = 0;

        while (isNumber) {
            chr = next();
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

        process(chr);
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
            char actual = next();
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
