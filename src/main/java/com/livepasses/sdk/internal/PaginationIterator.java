package com.livepasses.sdk.internal;

import com.livepasses.sdk.types.PagedResponse;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Lazy iterator that automatically paginates through all pages.
 * Fetches the next page only when all items on the current page are consumed.
 */
public class PaginationIterator<T> implements Iterator<T> {

    private final Function<Integer, PagedResponse<T>> fetchPage;
    private int currentPage;
    private int totalPages;
    private List<T> currentItems;
    private int currentIndex;
    private boolean exhausted;

    public PaginationIterator(Function<Integer, PagedResponse<T>> fetchPage) {
        this.fetchPage = fetchPage;
        this.currentPage = 0;
        this.totalPages = Integer.MAX_VALUE;
        this.currentItems = Collections.emptyList();
        this.currentIndex = 0;
        this.exhausted = false;
    }

    @Override
    public boolean hasNext() {
        if (exhausted) {
            return false;
        }
        // If we still have items in the current page
        if (currentIndex < currentItems.size()) {
            return true;
        }
        // Try to fetch next page
        if (currentPage >= totalPages) {
            exhausted = true;
            return false;
        }
        fetchNextPage();
        return !exhausted && currentIndex < currentItems.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentItems.get(currentIndex++);
    }

    private void fetchNextPage() {
        currentPage++;
        PagedResponse<T> response = fetchPage.apply(currentPage);
        currentItems = response.getItems() != null ? response.getItems() : Collections.emptyList();
        currentIndex = 0;
        totalPages = response.getPagination().getTotalPages();

        if (currentItems.isEmpty()) {
            exhausted = true;
        }
    }

    /**
     * Create an Iterable wrapper so this can be used in enhanced for-loops.
     */
    public static <T> Iterable<T> iterable(Function<Integer, PagedResponse<T>> fetchPage) {
        return () -> new PaginationIterator<>(fetchPage);
    }
}
