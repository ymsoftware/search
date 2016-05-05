import org.ap.core.search.QueryStringParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ymetelkin on 5/5/16.
 */
public class QueryStringParserTests {
    @Test
    public void testSingleTerms() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("ap");
        Assert.assertTrue(result.equals("ap"));
    }

    @Test
    public void testPhrase() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("\"Yuri Metelkin\"");
        Assert.assertTrue(result.equals("\"Yuri Metelkin\""));
    }

    @Test
    public void testMultipleTerms() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("Yuri Metelkin");
        Assert.assertTrue(result.equals("Yuri Metelkin"));
    }

    @Test
    public void testSingleField() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("title:ap");
        Assert.assertTrue(result.equals("title:ap"));
    }

    @Test
    public void testSpacedField() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("ap title : ap");
        Assert.assertTrue(result.equals("ap title:ap"));
    }

    @Test
    public void testRangeField() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("priority:[1 TO 4]");
        Assert.assertTrue(result.equals("priority:[1 TO 4]"));
        result = parser.parse("priority:{ 1  TO  4 ]");
        Assert.assertTrue(result.equals("priority:{1 TO 4]"));
        result = parser.parse("priority:[1 to 4]");
        Assert.assertTrue(result.equals("priority 1 to 4"));
    }

    @Test
    public void testRegexField() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("title:/NY|NJ/");
        Assert.assertTrue(result.equals("title:/NY|NJ/"));
        result = parser.parse("title:/ NY|NJ (CT PA)/");
        Assert.assertTrue(result.equals("title:/ NY|NJ (CT PA)/"));
        result = parser.parse("title:/NY|NJ");
        Assert.assertTrue(result.equals("title /NY|NJ"));
    }

    @Test
    public void testGroupField() throws Exception {
        QueryStringParser parser = new QueryStringParser();
        String result = parser.parse("title:(NY +NJ -CT)");
        Assert.assertTrue(result.equals("title:(NY +NJ -CT)"));
        result = parser.parse("title:((+NY +CT) (+NJ +PA))");
        Assert.assertTrue(result.equals("title:((+NY +CT) (+NJ +PA))"));
        result = parser.parse("title:((+NY +CT) (+NJ +PA)");
        Assert.assertTrue(result.equals("title ((+NY +CT) (+NJ +PA)"));
    }
}
