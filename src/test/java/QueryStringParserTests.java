import org.ap.core.search.QueryStringParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymetelkin on 5/5/16.
 */
public class QueryStringParserTests {
    private QueryStringParser parser;

    public QueryStringParserTests(){
        Map<String, String> map = new HashMap<>();

        this.parser = new QueryStringParser(map);
    }

    @Test
    public void testSingleTerms() throws Exception {
        String result = this.parser.parse("ap");
        Assert.assertTrue(result.equals("ap"));
    }

    @Test
    public void testPhrase() throws Exception {
        String result = this.parser.parse("\"Yuri Metelkin\"");
        Assert.assertTrue(result.equals("\"Yuri Metelkin\""));
    }

    @Test
    public void testMultipleTerms() throws Exception {
        String result = this.parser.parse("Yuri Metelkin");
        Assert.assertTrue(result.equals("Yuri Metelkin"));
    }

    @Test
    public void testSingleField() throws Exception {
        String result = this.parser.parse("title:ap");
        Assert.assertTrue(result.equals("title:ap"));
    }

    @Test
    public void testSpacedField() throws Exception {
        String result = this.parser.parse("ap title : ap");
        Assert.assertTrue(result.equals("ap title:ap"));
    }

    @Test
    public void testRangeField() throws Exception {
        String result = this.parser.parse("priority:[1 TO 4]");
        Assert.assertTrue(result.equals("priority:[1 TO 4]"));
        result = this.parser.parse("priority:{ 1  TO  4 ]");
        Assert.assertTrue(result.equals("priority:{1 TO 4]"));
        result = this.parser.parse("priority:[1 to 4]");
        Assert.assertTrue(result.equals("priority:[1 TO 4]"));
        result = this.parser.parse("priority:[1 to 4");
        Assert.assertTrue(result.equals("priority 1 to 4"));
        result = this.parser.parse("priority:1 to 4]");
        Assert.assertTrue(result.equals("priority:1 to 4]"));
    }

    @Test
    public void testRegexField() throws Exception {
        String result = this.parser.parse("title:/NY|NJ/");
        Assert.assertTrue(result.equals("title:/NY|NJ/"));
        result = this.parser.parse("title:/ NY|NJ (CT PA)/");
        Assert.assertTrue(result.equals("title:/ NY|NJ (CT PA)/"));
        result = this.parser.parse("title:/NY|NJ");
        Assert.assertTrue(result.equals("title /NY|NJ"));
        result = this.parser.parse("title: /NY|NJ/");
        Assert.assertTrue(result.equals("title:/NY|NJ/"));
    }

    @Test
    public void testGroupField() throws Exception {
        String result = this.parser.parse("title:(NY +NJ -CT)");
        Assert.assertTrue(result.equals("title:(NY +NJ -CT)"));
        result = this.parser.parse("title:((+NY +CT) (+NJ +PA))");
        Assert.assertTrue(result.equals("title:((+NY +CT) (+NJ +PA))"));
        result = this.parser.parse("title:((+NY +CT) (+NJ +PA)");
        Assert.assertTrue(result.equals("title ((+NY +CT) (+NJ +PA)"));
    }

}
