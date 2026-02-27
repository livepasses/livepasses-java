package com.livepasses.sdk.types;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListPassesParams {

    private final String templateId;
    private final String status;
    private final String platform;
    private final Integer page;
    private final Integer pageSize;
    private final String searchTerm;
    private final String sortBy;
    private final Boolean sortDescending;

    private ListPassesParams(Builder builder) {
        this.templateId = builder.templateId;
        this.status = builder.status;
        this.platform = builder.platform;
        this.page = builder.page;
        this.pageSize = builder.pageSize;
        this.searchTerm = builder.searchTerm;
        this.sortBy = builder.sortBy;
        this.sortDescending = builder.sortDescending;
    }

    public static Builder builder() { return new Builder(); }

    public String getTemplateId() { return templateId; }
    public String getStatus() { return status; }
    public String getPlatform() { return platform; }
    public Integer getPage() { return page; }
    public Integer getPageSize() { return pageSize; }
    public String getSearchTerm() { return searchTerm; }
    public String getSortBy() { return sortBy; }
    public Boolean getSortDescending() { return sortDescending; }

    /** Convert to query parameter map, omitting null values. */
    public Map<String, String> toQueryParams() {
        Map<String, String> params = new LinkedHashMap<>();
        if (templateId != null) params.put("templateId", templateId);
        if (status != null) params.put("status", status);
        if (platform != null) params.put("platform", platform);
        if (page != null) params.put("page", String.valueOf(page));
        if (pageSize != null) params.put("pageSize", String.valueOf(pageSize));
        if (searchTerm != null) params.put("searchTerm", searchTerm);
        if (sortBy != null) params.put("sortBy", sortBy);
        if (sortDescending != null) params.put("sortDescending", String.valueOf(sortDescending));
        return params;
    }

    /** Create a copy with a different page number. */
    public ListPassesParams withPage(int page) {
        Builder b = new Builder();
        b.templateId = this.templateId;
        b.status = this.status;
        b.platform = this.platform;
        b.page = page;
        b.pageSize = this.pageSize;
        b.searchTerm = this.searchTerm;
        b.sortBy = this.sortBy;
        b.sortDescending = this.sortDescending;
        return new ListPassesParams(b);
    }

    public static class Builder {
        private String templateId;
        private String status;
        private String platform;
        private Integer page;
        private Integer pageSize;
        private String searchTerm;
        private String sortBy;
        private Boolean sortDescending;

        public Builder templateId(String v) { this.templateId = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder platform(String v) { this.platform = v; return this; }
        public Builder page(Integer v) { this.page = v; return this; }
        public Builder pageSize(Integer v) { this.pageSize = v; return this; }
        public Builder searchTerm(String v) { this.searchTerm = v; return this; }
        public Builder sortBy(String v) { this.sortBy = v; return this; }
        public Builder sortDescending(Boolean v) { this.sortDescending = v; return this; }

        public ListPassesParams build() { return new ListPassesParams(this); }
    }
}
