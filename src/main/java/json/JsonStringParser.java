package json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuri on 4/24/16.
 */
public class JsonStringParser extends JsonParser {
    private char[] chars = null;

    public JsonStringParser(String json) {
        if (json != null && json.length() > 0) {
            this.chars = json.toCharArray();
        }
    }

    @Override
    public void parse(JsonVisitor visitor) throws JsonParsingException {
        if (this.chars == null) {
            return;
        }

        init(visitor);

        while (this.position < this.chars.length) {
            char chr = next();
            process(chr);
        }
    }

    @Override
    protected char next() {
        return this.chars[this.position++];
    }
}
