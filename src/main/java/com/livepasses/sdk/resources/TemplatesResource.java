package com.livepasses.sdk.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.livepasses.sdk.internal.LivepassesHttpClient;
import com.livepasses.sdk.types.*;

import java.util.List;

/**
 * Operations on templates: list, get, create, update, activate, deactivate.
 */
public class TemplatesResource {

    private final LivepassesHttpClient http;

    public TemplatesResource(LivepassesHttpClient http) {
        this.http = http;
    }

    /**
     * List templates (paginated).
     */
    public PagedResponse<TemplateListItem> list(ListTemplatesParams params) {
        return http.getPaged(
                "/api/templates",
                params != null ? params.toQueryParams() : null,
                new TypeReference<List<TemplateListItem>>() {
                });
    }

    /**
     * Get a single template by ID.
     */
    public TemplateDetail get(String templateId) {
        return http.get("/api/templates/" + templateId, null, TemplateDetail.class);
    }

    /**
     * Create a new template.
     */
    public TemplateDetail create(CreateTemplateParams params) {
        return http.post("/api/templates", params, TemplateDetail.class);
    }

    /**
     * Update a template.
     */
    public TemplateDetail update(String templateId, UpdateTemplateParams params) {
        return http.put("/api/templates/" + templateId, params, TemplateDetail.class);
    }

    /**
     * Activate a template for pass generation.
     */
    public void activate(String templateId) {
        http.post("/api/templates/" + templateId + "/activate", null, Void.class);
    }

    /**
     * Deactivate a template.
     */
    public void deactivate(String templateId) {
        http.post("/api/templates/" + templateId + "/deactivate", null, Void.class);
    }
}
