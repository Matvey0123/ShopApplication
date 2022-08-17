package serializers.search;

import serializers.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchOutput {
    private final String type = "search";
    private List<SearchResult> results;

    public SearchOutput() {
        this.results = new ArrayList<>();
    }

    public void addSearchResult(SearchResult result) {
        results.add(result);
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }
}
