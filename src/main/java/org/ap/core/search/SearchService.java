package org.ap.core.search;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;

/**
 * Created by ymetelkin on 4/28/16.
 */
public class SearchService {
    private final Client client;
    private final SearchConfig config;

    private QueryStringBuilder qsq;

    public SearchService(Client client, SearchConfig config) {
        this.client = client;
        this.config = config;

        QueryStringBuilder qsq = new QueryStringBuilder();
        String[] fields = config.getFields();
        if (fields != null && fields.length > 0) {
            qsq.setFields(fields);
        }

        String operator = config.getDefaultOperator();
        if (operator != null && operator.equalsIgnoreCase(Constants.QSQ_AND_OPERATOR)) {
            qsq.setDefaultOperator(QueryStringBuilder.Operator.AND);
        }

        this.qsq = qsq;
    }

    public String execute(SearchRequest request) {
        SearchRequestBuilder rb = getSearchRequestBuilder(request);

        SearchResponse response = rb
                .execute()
                .actionGet();

        return response.toString();
    }

    public SearchRequestBuilder getSearchRequestBuilder(SearchRequest request) {
        SearchRequestBuilder rb = client
                .prepareSearch(this.config.getIndex())
                .setTypes(this.config.getType());

        if (request != null) {
            request.read(rb, this.qsq);
        }

        return rb;
    }
}
