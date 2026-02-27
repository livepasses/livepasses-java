package com.livepasses.sdk.types;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListTemplatesParams {

    private final String type;
    private final String status;
    private final Integer page;
    private final Integer pageSize;
    private final String searchTerm;
    private final String sortBy;
    private final Boolean sortDescending;

    private ListTemplatesParams(Builder builder) {
        this.type = builder.type;
        this.status = builder.status;
        this.page = builder.page;
        this.pageSize = builder.pageSize;
        this.searchTerm = builder.searchTerm;
        this.sortBy = builder.sortBy;
        this.sortDescending = builder.sortDescending;
    }

    public static Builder builder() { return new Builder(); }

    public String getType() { return type; }
    public String getStatus() { return status; }
    public Integer getPage() { return page; }
    public Integer getPageSize() { return pageSize; }
    public String getSearchTerm() { return searchTerm; }
    public String getSortBy() { return sortBy; }
    public Boolean getSortDescending() { return sortDescending; }

    public Map<String, String> toQueryParams() {
        Map<String, String> params = new LinkedHashMap<>();
        if (type != null) params.put("type", type);
        if (status != null) params.put("status", status);
        if (page != null) params.put("page", String.valueOf(page));
        if (pageSize != null) params.put("pageSize", String.valueOf(pageSize));
        if (searchTerm != null) params.put("searchTerm", searchTerm);
        if (sortBy != null) params.put("sortBy", sortBy);
        if (sortDescending != null) params.put("sortDescending", String.valueOf(sortDescending));
        return params;
    }

    public static class Builder {
        private String type;
        private String status;
        private Integer page;
        private Integer pageSize;
        private String searchTerm;
        private String sortBy;
        private Boolean sortDescending;

        public Builder type(String v) { this.type = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder page(Integer v) { this.page = v; return this; }
        public Builder pageSize(Integer v) { this.pageSize = v; return this; }
        public Builder searchTerm(String v) { this.searchTerm = v; return this; }
        public Builder sortBy(String v) { this.sortBy = v; return this; }
        public Builder sortDescending(Boolean v) { this.sortDescending = v; return this; }

        public ListTemplatesParams build() { return new ListTemplatesParams(this); }
    }
}
