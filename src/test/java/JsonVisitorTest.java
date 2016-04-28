import org.ap.core.json.JsonProperty;
import org.ap.core.json.JsonVisitor;

/**
 * Created by ymetelkin on 4/28/16.
 */
class JsonVisitorTest implements JsonVisitor {
    boolean trueF;
    boolean falseF;
    Object nullF;
    Number numberF;
    String nameF;
    String escapeF;
    boolean[] arrayBooleanF;
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
            this.nameF = jp.valueAsString();
        } else if (jp.field.equals("escape")) {
            this.escapeF = jp.valueAsString();
        } else if (jp.field.equals("arrayTextF")) {
            this.arrayTextF = jp.valueAsStringArray();
        } else if (jp.field.equals("arrayNumberF")) {
            this.arrayNumberF = jp.valueAsNumberArray();
        } else if (jp.field.equals("arrayBooleanF")) {
            this.arrayBooleanF = jp.valueAsBooleanArray();
        } else if (jp.field.equals("name.first")) {
            this.firstNameF = jp.valueAsString();
        } else if (jp.field.equals("name.last")) {
            this.lastNameF = jp.valueAsString();
        } else if (jp.field.equals("a.b")) {
            this.abcF = jp.valueAsString();
        } else if (jp.field.equals("a.x")) {
            this.axyzF = jp.valueAsStringArray();
        }
    }
}
