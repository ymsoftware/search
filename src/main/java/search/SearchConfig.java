package search;

/**
 * Created by yuri on 4/27/16.
 */
public class SearchConfig {
    private String index;
    private String type;

    public String getIndex() {
        return this.index;
    }

    public SearchConfig setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public SearchConfig setType(String type) {
        this.type = type;
        return this;
    }
}
