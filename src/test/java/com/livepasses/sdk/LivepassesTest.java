package com.livepasses.sdk;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

class LivepassesTest {

    @Test
    void shouldThrowOnNullApiKey() {
        assertThatThrownBy(() -> new Livepasses(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("API key is required");
    }

    @Test
    void shouldThrowOnEmptyApiKey() {
        assertThatThrownBy(() -> new Livepasses(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("API key is required");
    }

    @Test
    void shouldThrowOnBlankApiKey() {
        assertThatThrownBy(() -> new Livepasses("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("API key is required");
    }

    @Test
    void shouldCreateResourceInstances() {
        Livepasses client = new Livepasses("lp_test_key",
                LivepassesOptions.builder().baseUrl("http://localhost:9999").build());

        assertThat(client.passes()).isNotNull();
        assertThat(client.templates()).isNotNull();
        assertThat(client.webhooks()).isNotNull();
    }

    @Test
    void shouldAcceptCustomOptions() {
        LivepassesOptions options = LivepassesOptions.builder()
                .baseUrl("https://custom.api.com")
                .timeout(Duration.ofSeconds(60))
                .maxRetries(5)
                .build();

        assertThat(options.getBaseUrl()).isEqualTo("https://custom.api.com");
        assertThat(options.getTimeout()).isEqualTo(Duration.ofSeconds(60));
        assertThat(options.getMaxRetries()).isEqualTo(5);
    }

    @Test
    void shouldHaveCorrectDefaults() {
        LivepassesOptions defaults = LivepassesOptions.defaults();

        assertThat(defaults.getBaseUrl()).isEqualTo("https://api.livepasses.com");
        assertThat(defaults.getTimeout()).isEqualTo(Duration.ofSeconds(30));
        assertThat(defaults.getMaxRetries()).isEqualTo(3);
    }
}
