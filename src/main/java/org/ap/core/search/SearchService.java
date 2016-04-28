package org.ap.core.search;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by ymetelkin on 4/28/16.
 */
public class SearchService {
    private final Client client;
    private final SearchConfig config;

    public SearchService(Client client, SearchConfig config) {
        this.client = client;
        this.config = config;
    }

    public String execute(SearchRequest request) {
        SearchRequestBuilder rb = client
                .prepareSearch(this.config.getIndex())
                .setTypes(this.config.getType());
        //.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        if (request != null) {
            String query = request.getQuery();
            if (query != null && query.length() > 0) {
                rb = rb.setQuery(QueryBuilders.queryStringQuery(query));
            }

            int from = request.getFrom();
            if (from > 0) {
                rb = rb.setFrom(from);
            }

            int size = request.getSize();
            if (size > 0) {
                rb = rb.setSize(size);
            }

            String[] fields = request.getFields();
            if (fields != null && fields.length > 0) {
                rb = rb.setFetchSource(fields, null);
            }
        }

        SearchResponse response = rb
                .execute()
                .actionGet();

        return response.toString();
    }
}
