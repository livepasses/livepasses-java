package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Paginated API response envelope.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiPagedResponse<T> {

    private final boolean success;
    private final List<T> items;
    private final PaginationMetadata pagination;
    private final ApiResponse.ApiError error;
    private final String message;

    public ApiPagedResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("items") List<T> items,
            @JsonProperty("pagination") PaginationMetadata pagination,
            @JsonProperty("error") ApiResponse.ApiError error,
            @JsonProperty("message") String message) {
        this.success = success;
        this.items = items;
        this.pagination = pagination;
        this.error = error;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<T> getItems() {
        return items;
    }

    public PaginationMetadata getPagination() {
        return pagination;
    }

    public ApiResponse.ApiError getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
