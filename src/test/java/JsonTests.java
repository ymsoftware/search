import json.JsonProperty;
import json.JsonStringParser;
import json.JsonVisitor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


/**
 * Created by yuri on 4/24/16.
 */
public class JsonTests {
    @Test
    public void testSimpleJson() throws Exception {
        JsonStringParser parser = new JsonStringParser();
        JsonVisitorTest visitor = new JsonVisitorTest();
        String json = "{ \"trueF\": true, \"falseF\": false, \"nullF\": null, \"number\": -0.25, \"name\": \"YM\", \"escape\": \"\\\"Title\\\"\" }";
        parser.parse(json, visitor);
        Assert.assertTrue(visitor.trueF);
        Assert.assertFalse(visitor.falseF);
        Assert.assertNull(visitor.nullF);
        Assert.assertTrue(visitor.numberF.equals(-0.25));
        Assert.assertTrue(visitor.nameF.equals("YM"));
        Assert.assertTrue(visitor.escapeF.equals("\"Title\""));
    }

    @Test
    public void testArrayJson() throws Exception {
        JsonStringParser parser = new JsonStringParser();
        JsonVisitorTest visitor = new JsonVisitorTest();
        String json = "{ \"arrayTextF\": [\"a\", \"b\", \"c\"], \"arrayNumberF\": [1, 2, 3], \"arrayBooleanF\": [true, false] }";
        parser.parse(json, visitor);
        Assert.assertTrue(visitor.arrayTextF.length == 3);
        Assert.assertTrue(visitor.arrayTextF[0].equals("a"));
        Assert.assertTrue(visitor.arrayNumberF.length == 3);
        Assert.assertTrue((long) visitor.arrayNumberF[0] == 1);
        Assert.assertTrue(visitor.arrayBooleanF.length == 2);
        Assert.assertTrue(visitor.arrayBooleanF[0]);
    }

    @Test
    public void testNestedJson() throws Exception {
        JsonStringParser parser = new JsonStringParser();
        JsonVisitorTest visitor = new JsonVisitorTest();
        String json = "{ \"name\": { \"first\": \"Y\", \"last\": \"M\" }, \"a\": [ { \"b\": \"c\" }, { \"x\": [ \"y\", \"z\" ] } ] }";
        parser.parse(json, visitor);
        Assert.assertTrue(visitor.firstNameF.equals("Y"));
        Assert.assertTrue(visitor.lastNameF.equals("M"));
        Assert.assertTrue(visitor.abcF.equals("c"));
        Assert.assertTrue(visitor.axyzF.length == 2);
        Assert.assertTrue(visitor.axyzF[0].equals("y"));
        Assert.assertTrue(visitor.axyzF[1].equals("z"));
    }
}

class JsonVisitorTest implements JsonVisitor {
    boolean trueF;
    boolean falseF;
    Object nullF;
    Number numberF;
    String nameF;
    String escapeF;
    Boolean[] arrayBooleanF;
    String[] arrayTextF;
    Number[] arrayNumberF;
    String firstNameF;
    String lastNameF;
    String abcF;
    String[] axyzF;

    public void visit(JsonProperty jp) {
        if (jp.field.equals("trueF")) {
            this.trueF = (Boolean) jp.value;
        } else if (jp.field.equals("falseF")) {
            this.falseF = (Boolean) jp.value;
        } else if (jp.field.equals("nullF")) {
            this.nullF = jp.value;
        } else if (jp.field.equals("number")) {
            this.numberF = (Number) jp.value;
        } else if (jp.field.equals("name")) {
            this.nameF = (String) jp.value;
        } else if (jp.field.equals("escape")) {
            this.escapeF = (String) jp.value;
        } else if (jp.field.equals("arrayTextF")) {
            Object[] values = (Object[]) jp.value;
            this.arrayTextF = Arrays.copyOf(values, values.length, String[].class);
        } else if (jp.field.equals("arrayNumberF")) {
            Object[] values = (Object[]) jp.value;
            this.arrayNumberF = Arrays.copyOf(values, values.length, Number[].class);
        } else if (jp.field.equals("arrayBooleanF")) {
            Object[] values = (Object[]) jp.value;
            this.arrayBooleanF = Arrays.copyOf(values, values.length, Boolean[].class);
        } else if (jp.field.equals("name.first")) {
            this.firstNameF = (String) jp.value;
        } else if (jp.field.equals("name.last")) {
            this.lastNameF = (String) jp.value;
        } else if (jp.field.equals("a.b")) {
            this.abcF = (String) jp.value;
        } else if (jp.field.equals("a.x")) {
            Object[] values = (Object[]) jp.value;
            this.axyzF = Arrays.copyOf(values, values.length, String[].class);
        }
    }
}
