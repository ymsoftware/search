import org.ap.core.json.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


/**
 * Created by yuri on 4/24/16.
 */
public class JsonTests {
    @Test
    public void testSimpleJson() throws Exception {
        JsonVisitorTest visitor = new JsonVisitorTest();
        String json = "{ \"trueF\": true, \"falseF\": false, \"nullF\": null, \"number\": -0.25, \"name\": \"YM\", \"escape\": \"\\\"Title\\\"\" }";
        JsonParser parser = new JsonStringParser(json);
        parser.parse(visitor);
        Assert.assertTrue(visitor.trueF);
        Assert.assertFalse(visitor.falseF);
        Assert.assertNull(visitor.nullF);
        Assert.assertTrue(visitor.numberF.equals(-0.25));
        Assert.assertTrue(visitor.nameF.equals("YM"));
        Assert.assertTrue(visitor.escapeF.equals("\"Title\""));

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        parser = new JsonInputStreamParser(stream);
        parser.parse(visitor);
        Assert.assertTrue(visitor.trueF);
        Assert.assertFalse(visitor.falseF);
        Assert.assertNull(visitor.nullF);
        Assert.assertTrue(visitor.numberF.equals(-0.25));
        Assert.assertTrue(visitor.nameF.equals("YM"));
        Assert.assertTrue(visitor.escapeF.equals("\"Title\""));
    }

    @Test
    public void testArrayJson() throws Exception {
        JsonVisitorTest visitor = new JsonVisitorTest();
        String json = "{ \"arrayTextF\": [\"a\", \"b\", \"c\"], \"arrayNumberF\": [1, 2, 3], \"arrayBooleanF\": [true, false] }";
        JsonParser parser = new JsonStringParser(json);
        parser.parse(visitor);
        Assert.assertTrue(visitor.arrayTextF.length == 3);
        Assert.assertTrue(visitor.arrayTextF[0].equals("a"));
        Assert.assertTrue(visitor.arrayNumberF.length == 3);
        Assert.assertTrue((long) visitor.arrayNumberF[0] == 1);
        Assert.assertTrue(visitor.arrayBooleanF.length == 2);
        Assert.assertTrue(visitor.arrayBooleanF[0]);

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        parser = new JsonInputStreamParser(stream);
        parser.parse(visitor);
        Assert.assertTrue(visitor.arrayTextF.length == 3);
        Assert.assertTrue(visitor.arrayTextF[0].equals("a"));
        Assert.assertTrue(visitor.arrayNumberF.length == 3);
        Assert.assertTrue((long) visitor.arrayNumberF[0] == 1);
        Assert.assertTrue(visitor.arrayBooleanF.length == 2);
        Assert.assertTrue(visitor.arrayBooleanF[0]);
    }

    @Test
    public void testNestedJson() throws Exception {
        JsonVisitorTest visitor = new JsonVisitorTest();
        String json = "{ \"name\": { \"first\": \"Y\", \"last\": \"M\" }, \"a\": [ { \"b\": \"c\" }, { \"x\": [ \"y\", \"z\" ] } ] }";
        JsonParser parser = new JsonStringParser(json);
        parser.parse(visitor);
        Assert.assertTrue(visitor.firstNameF.equals("Y"));
        Assert.assertTrue(visitor.lastNameF.equals("M"));
        Assert.assertTrue(visitor.abcF.equals("c"));
        Assert.assertTrue(visitor.axyzF.length == 2);
        Assert.assertTrue(visitor.axyzF[0].equals("y"));
        Assert.assertTrue(visitor.axyzF[1].equals("z"));

        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        parser = new JsonInputStreamParser(stream);
        parser.parse(visitor);
        Assert.assertTrue(visitor.firstNameF.equals("Y"));
        Assert.assertTrue(visitor.lastNameF.equals("M"));
        Assert.assertTrue(visitor.abcF.equals("c"));
        Assert.assertTrue(visitor.axyzF.length == 2);
        Assert.assertTrue(visitor.axyzF[0].equals("y"));
        Assert.assertTrue(visitor.axyzF[1].equals("z"));
    }
}

