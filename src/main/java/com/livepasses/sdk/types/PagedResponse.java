package com.livepasses.sdk.types;

import java.util.List;

/**
 * SDK-facing paginated response with items and pagination metadata.
 */
public class PagedResponse<T> {

    private final List<T> items;
    private final PaginationMetadata pagination;

    public PagedResponse(List<T> items, PaginationMetadata pagination) {
        this.items = items;
        this.pagination = pagination;
    }

    public List<T> getItems() {
        return items;
    }

    public PaginationMetadata getPagination() {
        return pagination;
    }
}
