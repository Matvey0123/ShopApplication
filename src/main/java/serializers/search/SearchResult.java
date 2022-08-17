package serializers.search;

import entities.Customer;

import java.util.List;

public class SearchResult {
    private Object criteria;
    private List<Customer> results;

    public SearchResult(Object criteria, List<Customer> results) {
        this.criteria = criteria;
        this.results = results;
    }

}
