package com.livepasses.sdk.internal;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Polls a supplier function until a condition is met.
 */
public final class Polling {

    private Polling() {
    }

    /**
     * Poll until complete or max attempts exhausted.
     *
     * @param fn          function to call on each poll
     * @param isComplete  predicate that returns true when polling should stop
     * @param intervalMs  milliseconds between polls (default: 2000)
     * @param maxAttempts maximum number of poll attempts (default: 150)
     * @param onProgress  called on each poll with the current result (nullable)
     * @return the final result
     */
    public static <T> T pollUntilComplete(
            Supplier<T> fn,
            Predicate<T> isComplete,
            long intervalMs,
            int maxAttempts,
            Consumer<T> onProgress) {

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            T result = fn.get();

            if (onProgress != null) {
                onProgress.accept(result);
            }

            if (isComplete.test(result)) {
                return result;
            }

            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(intervalMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return result;
                }
            }
        }

        // Return the last result even if not complete
        T finalResult = fn.get();
        if (onProgress != null) {
            onProgress.accept(finalResult);
        }
        return finalResult;
    }
}
