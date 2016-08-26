package org.ap.core;

import org.ap.core.json.*;
import org.ap.core.qe.QEConfig;
import org.ap.core.search.SearchConfig;

import java.io.InputStream;

/**
 * Created by yuri on 4/28/16.
 */
public class ConfigManager implements JsonVisitor {
    private SearchConfig searchConfig;
    private QEConfig qeConfig;

    public SearchConfig getSearchConfig() {
        return this.searchConfig;
    }

    public QEConfig getQEConfig() {
        return this.qeConfig;
    }

    public void load(String resourcePath) throws JsonParsingException {
        this.searchConfig = new SearchConfig();
        this.qeConfig = new QEConfig();

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
        JsonParser parser = new JsonInputStreamParser(stream);
        parser.parse(this);
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals(Constants.CONFIG_SEARCH_HOST)) {
            this.searchConfig.setHost(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_SEARCH_CLUSTER)) {
            this.searchConfig.setCluster(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_SEARCH_PORT)) {
            this.searchConfig.setPort(jp.valueAsInt());
        } else if (jp.field.equals(Constants.CONFIG_SEARCH_INDEX)) {
            this.searchConfig.setIndex(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_SEARCH_TYPE)) {
            this.searchConfig.setType(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_SEARCH_FIELDS)) {
            this.searchConfig.setFields(jp.valueAsStringArray());
        } else if (jp.field.equals(Constants.CONFIG_SEARCH_OPERATOR)) {
            this.searchConfig.setDefaultOperator(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_QE_HOST)) {
            this.qeConfig.setHost(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_QE_CLUSTER)) {
            this.qeConfig.setCluster(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_QE_PORT)) {
            this.qeConfig.setPort(jp.valueAsInt());
        } else if (jp.field.equals(Constants.CONFIG_QE_INDEX)) {
            this.qeConfig.setIndex(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_QE_TYPE)) {
            this.qeConfig.setType(jp.valueAsString());
        } else if (jp.field.equals(Constants.CONFIG_QE_DAYS)) {
            this.qeConfig.setDays(jp.valueAsInt());
        } else if (jp.field.equals(Constants.CONFIG_QE_SAME_SENTENCE_WEIGTH)) {
            this.qeConfig.setSameSentenceWeight(jp.valueAsDouble());
        }
    }
}
