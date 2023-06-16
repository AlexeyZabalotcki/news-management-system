package ru.clevertec.news_service.cache.impl;


import ru.clevertec.news_service.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LFUCache<K, V> implements Cache<K, V> {

    private final Integer capacity;
    private Integer size;
    private Integer minFrequency;
    private final Map<K, Node> content;
    private final Map<Integer, DoubleLinkedList> bucket;

    public LFUCache(int capacity) {
        this.capacity = capacity;

        this.content = new HashMap<>(capacity);
        this.bucket = new HashMap<>();

        this.size = 0;
    }

    @Override
    public void add(K key, V value) {
        if (this.content.containsKey(key)) {
            Node node = content.get(key);
            node.value = value;
            updateNode(node);
        } else {

            this.size++;

            if (this.size > this.capacity) {
                DoubleLinkedList minFreqList = this.bucket.get(this.minFrequency);
                this.content.remove(minFreqList.tail.prev.key);
                minFreqList.remove(minFreqList.tail.prev);
                this.size--;
            }

            this.minFrequency = 1;
            Node newNode = new Node(key, value);

            DoubleLinkedList curList = bucket.getOrDefault(1, new DoubleLinkedList());
            curList.add(newNode);
            this.bucket.put(1, curList);
            this.content.put(key, newNode);
        }
    }

    @Override
    public Optional<V> get(K key) {
        Node node = this.content.get(key);
        if (node == null) {
            return Optional.empty();
        }
        updateNode(node);
        return Optional.of(node.value);
    }

    @Override
    public void delete(K key) {
        Node node = this.content.get(key);
        if (node != null) {
            int frequency = node.frequency;
            DoubleLinkedList list = this.bucket.get(frequency);
            list.remove(node);
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

    private void updateNode(Node node) {
        int nodeFreq = node.frequency;
        DoubleLinkedList curList = bucket.get(nodeFreq);
        curList.remove(node);

        if (nodeFreq == minFrequency && curList.listSize == 0) {
            minFrequency++;
        }

        node.frequency++;

        DoubleLinkedList newList = bucket.getOrDefault(node.frequency, new DoubleLinkedList());
        newList.add(node);
        bucket.put(node.frequency, newList);
    }

    private class Node {
        private final K key;
        private V value;
        private int frequency;
        private Node prev;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
        }
    }

    private class DoubleLinkedList {
        private int listSize;
        private final Node head;
        private final Node tail;

        private DoubleLinkedList() {
            this.head = new Node(null, null);
            this.tail = new Node(null, null);
            head.next = tail;
            tail.prev = head;
        }

        private void add(Node curNode) {
            Node nextNode = head.next;
            curNode.next = nextNode;
            curNode.prev = head;
            head.next = curNode;
            nextNode.prev = curNode;
            listSize++;
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            listSize--;
        }

    }
}
