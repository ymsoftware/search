import org.ap.core.Startup;
import org.ap.core.json.JsonParsingException;
import org.ap.core.qe.DocumentRequest;
import org.ap.core.qe.DocumentService;
import org.ap.core.qe.QERequest;
import org.ap.core.qe.QEService;
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

    @Test
    public void testDocument() {
        String json = "{ \"headline\": \"clinton private email server. clinton visits ukraine\", \"arrivaldatetime\": \"2016-08-25T00:00:00\" }";

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        DocumentRequest req = null;
        try {
            req = new DocumentRequest(stream);
        } catch (JsonParsingException e) {
            e.printStackTrace();
        }

        DocumentService svc = Startup.getInstance().getDocumentService();
        json = svc.execute(req);
    }
}
