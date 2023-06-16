package ru.clevertec.news_service.cache;

import java.util.Optional;

public interface Cache<K, V> {

    void add(K key, V value);

    Optional<V> get(K key);

    void delete(K key);

    Integer getSize();

    Integer getCapacity();

    Boolean isEmpty();

    void clear();
}
