package ru.clevertec.news_service.cache.impl;


import ru.clevertec.news_service.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LRUCache<K, V> implements Cache<K, V> {

    private final Map<K, DoublyLinkedNode> content;
    private final DoublyLinkedNode head;
    private final DoublyLinkedNode tail;
    private final Integer capacity;
    private Integer size;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.content = new HashMap<>(capacity);

        this.head = new DoublyLinkedNode();
        this.tail = new DoublyLinkedNode();

        head.next = tail;
        tail.prev = head;

        this.size = 0;
    }

    @Override
    public void add(K key, V value) {
        DoublyLinkedNode node = content.get(key);
        if (node != null) {
            node.value = value;
            remove(node);
            moveToHeadNode(node);
        } else {
            node = new DoublyLinkedNode();
            node.key = key;
            node.value = value;
            content.put(key, node);
            moveToHeadNode(node);
            this.size++;

            if (this.size > this.capacity) {
                this.content.remove(tail.prev.key);
                remove(tail.prev);
                this.size--;
            }
        }
    }

    @Override
    public Optional<V> get(K key) {
        DoublyLinkedNode node = this.content.get(key);
        if (node == null) {
            return Optional.empty();
        }

        remove(node);
        moveToHeadNode(node);
        return Optional.of(node.value);
    }

    @Override
    public void delete(K key) {
        DoublyLinkedNode node = this.content.get(key);
        if (node != null) {
            this.remove(node);
            this.content.remove(key);
            this.size--;
        }
    }

    @Override
    public Integer getSize() {
        return this.size;
    }

    @Override
    public Integer getCapacity() {
        return this.capacity;
    }

    @Override
    public Boolean isEmpty() {
        return this.content.isEmpty();
    }

    @Override
    public void clear() {
        this.content.clear();
    }

    private void moveToHeadNode(DoublyLinkedNode node) {
        DoublyLinkedNode temp = head.next;
        head.next = node;

        node.prev = head;
        node.next = temp;

        temp.prev = node;
    }

    private void remove(DoublyLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private class DoublyLinkedNode {
        private K key;
        private V value;
        private DoublyLinkedNode prev;
        private DoublyLinkedNode next;

    }
}
