import org.ap.core.Startup;
import org.ap.core.json.JsonParsingException;
import org.ap.core.search.QERequest;
import org.ap.core.search.QEService;
import org.ap.core.search.SearchRequest;
import org.ap.core.search.SearchService;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by ymetelkin on 8/22/16.
 */
public class QETests {
    @Test
    public void testHandler() {
        String json = "{ \"query\": \"clinton trump\", \"boost\": true }";

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        QERequest req = null;
        try {
            req = new QERequest(stream);
        } catch (JsonParsingException e) {
            e.printStackTrace();
        }

        QEService svc = Startup.getInstance().getQE();
        json = svc.execute(req);
    }
}
