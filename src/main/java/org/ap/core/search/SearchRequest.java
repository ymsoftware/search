package org.ap.core.search;

import org.ap.core.json.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by yuri on 4/28/16.
 */
public class SearchRequest implements JsonVisitor {
    private String query;
    private String[] mediaTypes;
    private int[] includeProducts;
    private int[] excludeProducts;
    private String sidouts;
    private int futureHours;
    private String[] fields;
    private int from;
    private int size;

    public String getQuery() {
        return this.query;
    }

    public SearchRequest setQuery(String query) {
        this.query = query;
        return this;
    }

    public String[] getMediaTypes() {
        return this.mediaTypes;
    }

    public SearchRequest setMediaTypes(String[] mediaTypes) {
        this.mediaTypes = mediaTypes;
        return this;
    }

    public int[] getIncludeProducts() {
        return this.includeProducts;
    }

    public SearchRequest setIncludeProducts(int[] includeProducts) {
        this.includeProducts = includeProducts;
        return this;
    }

    public int[] getExcludeProducts() {
        return this.excludeProducts;
    }

    public SearchRequest setExcludeProducts(int[] excludeProducts) {
        this.excludeProducts = excludeProducts;
        return this;
    }

    public String getSidouts() {
        return this.sidouts;
    }

    public SearchRequest setSidouts(String sidouts) {
        this.sidouts = sidouts;
        return this;
    }

    public int getFutureHours() {
        return this.futureHours;
    }

    public SearchRequest setFutureHours(int futureHours) {
        this.futureHours = futureHours;
        return this;
    }

    public int getFrom() {
        return this.from;
    }

    public SearchRequest setFrom(int from) {
        this.from = from;
        return this;
    }

    public String[] getFields() {
        return this.fields;
    }

    public SearchRequest setFields(String[] fields) {
        this.fields = fields;
        return this;
    }

    public int getSize() {
        return this.size;
    }

    public SearchRequest setSize(int size) {
        this.size = size;
        return this;
    }

    public SearchRequest() {

    }

    public SearchRequest(InputStream stream) throws JsonParsingException {
        JsonParser parser = new JsonInputStreamParser(stream);
        parser.parse(this);
    }

    public void read(SearchRequestBuilder requestBuilder, QSQBuilder qsqTemplate) {
        QSQBuilder qsq = null;
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        boolean useBool = false;

        String query = getQuery();
        if (query != null && query.length() > 0) {
            qsq = qsqTemplate;
            qsq.setQuery(query);
        }

        String[] types = getMediaTypes();
        if (types != null && types.length > 0) {
            bool.filter(QueryBuilders.termsQuery(Constants.APPL_MEDIA_TYPE, types));
            useBool = true;
        }

        int[] products = getIncludeProducts();
        if (products != null && products.length > 0) {
            bool.filter(QueryBuilders.termsQuery(Constants.APPL_PRODUCTS, products));
            useBool = true;
        }

        products = getExcludeProducts();
        if (products != null && products.length > 0) {
            bool.mustNot(QueryBuilders.termsQuery(Constants.APPL_PRODUCTS, products));
            useBool = true;
        }

        String sidouts = getSidouts();
        if (sidouts != null && sidouts.length() > 0) {
            bool.mustNot(QueryBuilders.termQuery(Constants.APPL_EXPANDED_SID_OUTS, sidouts.toLowerCase()));
            useBool = true;
        }

        int hours = getFutureHours();
        if (hours > 0) {
            String lte = String.format("now+%dh", hours);
            bool.should(QueryBuilders.rangeQuery(Constants.APPL_RELEASEDATETIME).lte(lte));
            bool.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(Constants.APPL_RELEASEDATETIME)));
            useBool = true;
        }

        if (useBool) {
            if (qsq != null) {
                bool.must(qsq);
            }
            requestBuilder.setQuery(bool);
        } else {
            if (qsq != null) {
                requestBuilder.setQuery(qsq);
            }
        }

        int from = getFrom();
        if (from > 0) {
            requestBuilder.setFrom(from);
        }

        int size = getSize();
        if (size > 0) {
            requestBuilder.setSize(size);
        }

        String[] fields = getFields();
        if (fields != null && fields.length > 0) {
            requestBuilder.setFetchSource(fields, null);
        }
    }

    @Override
    public void visit(JsonProperty jp) {
        if (jp.field.equals(Constants.REQUEST_QUERY)) {
            setQuery(jp.valueAsString());
        } else if (jp.field.equals(Constants.REQUEST_MEDIA_TYPES)) {
            setMediaTypes(jp.valueAsStringArray());
        } else if (jp.field.equals(Constants.REQUEST_ENTS_INCLUDE)) {
            setIncludeProducts(jp.valueAsIntArray());
        } else if (jp.field.equals(Constants.REQUEST_ENTS_EXCLUDE)) {
            setExcludeProducts(jp.valueAsIntArray());
        } else if (jp.field.equals(Constants.REQUEST_ENTS_SIDOUTS)) {
            setSidouts(jp.valueAsString());
        } else if (jp.field.equals(Constants.REQUEST_ENTS_FUTURE_HOURS)) {
            setFutureHours(jp.valueAsInt());
        } else if (jp.field.equals(Constants.REQUEST_FIELDS)) {
            setFields(jp.valueAsStringArray());
        } else if (jp.field.equals(Constants.REQUEST_FROM)) {
            setFrom(jp.valueAsInt());
        } else if (jp.field.equals(Constants.REQUEST_SIZE)) {
            setSize(jp.valueAsInt());
        }
    }
}
