package json;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuri on 4/26/16.
 */
public class JsonInputStreamParser extends JsonParser {
    private InputStream inputstream = null;

    public JsonInputStreamParser(InputStream inputstream) {
        this.inputstream = inputstream;
    }

    @Override
    public void parse(JsonVisitor visitor) throws JsonParsingException {
        init(visitor);

        char chr = next();
        while (chr != 0) {
            process(chr);
            chr = next();
        }
        try {
            this.inputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected char next() {
        this.position++;

        try {
            int data = this.inputstream.read();
            return data == -1 ? 0 : (char) data;
        } catch (IOException e) {
            return 0;
        }
    }
}
