package org.ap.core.search;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by ymetelkin on 4/28/16.
 */
public class QSQBuilder extends QueryBuilder {
    private String query;
    private QSQBuilder.Operator defaultOperator;
    private String[] fields;

    public QSQBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getQuery() {
        return this.query;
    }

    public Operator getDefaultOperator() {
        return this.defaultOperator;
    }

    public QSQBuilder setDefaultOperator(Operator defaultOperator) {
        this.defaultOperator = defaultOperator;
        return this;
    }

    public String[] getFields() {
        return this.fields;
    }

    public QSQBuilder setFields(String[] fields) {
        this.fields = fields;
        return this;
    }

    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(Constants.QSQ_QUERY_STRING);
        builder.field(Constants.QSQ_QUERY, this.query);

        if (this.fields != null && this.fields.length > 0) {
            builder.startArray(Constants.QSQ_FIELDS);

            for (String field : this.fields) {
                builder.value(field);
            }

            builder.endArray();
        }

        if (this.defaultOperator != null) {
            builder.field(Constants.QSQ_OPERATOR, this.defaultOperator.name().toLowerCase(Locale.ROOT));
        }

        builder.endObject();
    }

    public static enum Operator {
        OR,
        AND;

        private Operator() {
        }
    }
}
