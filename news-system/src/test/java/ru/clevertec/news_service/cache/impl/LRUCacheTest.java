package ru.clevertec.news_service.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.news_service.cache.Cache;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LRUCacheTest {

    private Cache<Integer, String> cache;

    @BeforeEach
    void setUp() {
        cache = new LRUCache<>(3);
    }

    static Stream<Arguments> keys() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, value 1",
            "2, value 2",
            "3, value 3"
    })
    void checkAddAndGetShouldReturnValue(Integer key, String value) {
        cache.add(key, value);

        Optional<String> optionalString = cache.get(key);

        assertEquals(optionalString, Optional.of(value));
    }

    @Test
    void checkGetShouldReturnOptionalEmpty() {
        assertTrue(cache.get(1).isEmpty());
    }

    @Test
    void checkUpdateShouldReturnUpdatedValue() {
        String value = "first";
        String secondValue = "second";

        cache.add(1, value);
        cache.add(1, secondValue);

        assertEquals(cache.get(1), Optional.of(secondValue));
    }

    @ParameterizedTest
    @MethodSource("keys")
    void checkDeleteShouldReturnOptionalEmpty(Integer key) {
        String value = "value";
        cache.add(key, value);
        cache.delete(key);

        assertEquals(cache.get(key), Optional.empty());
    }

    @Test
    void checkGetSizeForEmptyCacheShouldReturnZero() {
        Integer actual = cache.getSize();

        assertEquals(actual, 0);
    }

    @Test
    void checkGetSizeCacheShouldReturn1() {
        cache.add(1, "1");

        Integer actual = cache.getSize();

        assertEquals(actual, 1);
    }

    @Test
    void checkGetCapacityShouldReturn3() {
        assertEquals(cache.getCapacity(), 3);
    }

    @Test
    void checkIsEmptyShouldReturnTrue() {
        assertTrue(cache.isEmpty());
    }

    @Test
    void clear() {
        cache.add(1, "1");
        cache.add(2, "2");
        cache.add(3, "3");
        cache.add(4, "4");

        cache.clear();

        assertTrue(cache.isEmpty());
    }
}