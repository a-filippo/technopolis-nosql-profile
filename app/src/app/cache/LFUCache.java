package app.cache;
import org.w3c.dom.NodeList;

import java.util.*;

public class LFUCache<K, V> {
    /* Key value store for LFU cache
       The key is normal key and value is a reference to
       a node in frequency list. This node will point to
       its parent which is head of linkedlist for particular
       frequency */
    HashMap<K, LFUCacheEntry<K, V>> kvStore;

    /* A Doubly linked list of frequency nodes */
    NodeList freqList;

    /* HashMap for storing frequencyNode entries */
    HashMap<Integer, FrequencyNode> frequencyMap;

    /* Capacity of cache */
    int capacity;

    /* current size of Cache */
    int size;

    public String toString(){
        return kvStore.toString();
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        kvStore = new HashMap<K, LFUCacheEntry<K, V>>();
        freqList = new NodeList();
        frequencyMap = new HashMap<Integer, FrequencyNode>();
    }

    public void delete (K key){
        if (!kvStore.containsKey(key))
            return;

        LFUCacheEntry<K, V> entry = kvStore.get(key);
        kvStore.remove(key);
        entry.frequencyNode.lfuCacheEntryList.remove(entry);
        if (entry.frequencyNode.lfuCacheEntryList.length <= 0) {
            frequencyMap.remove(entry.frequencyNode.frequency);
            freqList.remove(entry.frequencyNode);
        }
        size--;
    }

    public void delete(LFUCacheEntry<K, V> entry) {
        if (!kvStore.containsKey(entry.key))
            return;

        kvStore.remove(entry.key);
        entry.frequencyNode.lfuCacheEntryList.remove(entry);
        if (entry.frequencyNode.lfuCacheEntryList.length <= 0) {
            frequencyMap.remove(entry.frequencyNode.frequency);
            freqList.remove(entry.frequencyNode);
        }
        size--;
    }


    public FrequencyNode getFrequencyNode(int frequency) {
        if (!frequencyMap.containsKey(frequency - 1) &&
                !frequencyMap.containsKey(frequency) &&
                frequency != 1) {
            System.out.println("Request for Frequency Node " + frequency +
                    " But " + frequency + " or " + (frequency - 1) +
                    " Doesn't exist");
            return null;

        }

        if (!frequencyMap.containsKey(frequency)) {
            FrequencyNode newFrequencyNode = new FrequencyNode(frequency);
            if (frequency != 1)
                freqList.insertAfter(frequencyMap.get(frequency - 1),
                        newFrequencyNode);
            else
                freqList.prepend(newFrequencyNode);
            frequencyMap.put(frequency, newFrequencyNode);
        }

        return frequencyMap.get(frequency);
    }

    public void set(K key, V value) {
        if (capacity == 0)
            return;
        FrequencyNode newFrequencyNode = null;
        if (kvStore.containsKey(key)) {
	    /* Remove old key if exists */
            newFrequencyNode = getFrequencyNode(kvStore.get(key).frequencyNode.frequency + 1);
            delete(kvStore.get(key));
        } else if (size == capacity) {
	    /* If cache size if full remove first element from freq list */
            FrequencyNode fNode = (FrequencyNode) freqList.head;
            LFUCacheEntry<K, V> entry = (LFUCacheEntry<K, V>) fNode.lfuCacheEntryList.head;
            delete(entry);
            System.out.println("Cache full. Removed entry " + entry);
        }
        if (newFrequencyNode == null)
            newFrequencyNode = getFrequencyNode(1);
        LFUCacheEntry<K, V> entry = new LFUCacheEntry<K, V>(key, value,
                newFrequencyNode);
        kvStore.put(key, entry);
        newFrequencyNode.lfuCacheEntryList.append(entry);
        size++;
        System.out.println("Set new " + entry + " entry, cache size: " + size);
    }


    public V get(K key) {
        if (!kvStore.containsKey(key) || capacity == 0)
            return null;

        LFUCacheEntry<K, V> entry = kvStore.get(key);
        FrequencyNode newFrequencyNode =
                getFrequencyNode(entry.frequencyNode.frequency + 1);
        entry.frequencyNode.lfuCacheEntryList.remove(entry);
        newFrequencyNode.lfuCacheEntryList.append(entry);
        if (entry.frequencyNode.lfuCacheEntryList.length <= 0) {
            frequencyMap.remove(entry.frequencyNode.frequency);
            freqList.remove(entry.frequencyNode);
        }
        entry.frequencyNode = newFrequencyNode;

        return entry.value;
    }

    private class LFUCacheEntry<K, V> extends Node {
        K key;
        V value;
        FrequencyNode frequencyNode;

        public LFUCacheEntry(K key, V value,
                             FrequencyNode frequencyNode) {
            this.key = key;
            this.value = value;
            this.frequencyNode = frequencyNode;
        }

        public boolean equals(Object o) {
            LFUCacheEntry<K, V> entry = (LFUCacheEntry<K, V>) o;
            return key.equals(entry.key) &&
                    value.equals(entry.value);
        }

        public int hashCode() {
            return key.hashCode() * 31 + value.hashCode() * 17;
        }

        public String toString() {
            return "[" + key.toString() + "," + value.toString() + ", " + frequencyNode.toString() + "]";
        }
    }

    private abstract class Node {
        Node prev = null;
        Node next = null;
    }

    private class FrequencyNode extends Node {
        int frequency;
        NodeList lfuCacheEntryList;

        public FrequencyNode(int frequency) {
            this.frequency = frequency;
            lfuCacheEntryList = new NodeList();
        }

        public boolean equals(Object o) {
            return frequency == ((FrequencyNode) o).frequency;
        }

        public int hashCode() {
            return frequency * 31;
        }

        public String toString() {
            return Integer.toString(frequency);
        }
    }

    public class NodeList {
        Node head;
        Node tail;
        int length;

        public NodeList() {
            head = null;
            tail = null;
            length = 0;
        }

        public void prepend(Node node) {
            if (head == null) {
                tail = node;
                node.next = null;
            } else {
                node.next = head;
                head.prev = node;
            }
            head = node;
            node.prev = null;
            length++;
        }

        public void append(Node node) {
            if (tail == null) {
                prepend(node);
            } else {
                tail.next = node;
                node.next = null;
                node.prev = tail;
                tail = node;
                length++;
            }
        }

        public void insertAfter(Node position, Node node) {
            if (position == tail) {
                append(node);
            } else {
                node.next = position.next;
                node.prev = position;
                position.next = node;
                node.next.prev = node;
                length++;
            }
        }

        public void remove(Node node) {
            if (node == tail && node == head) { /* single node in LinkedList */
                head = null;
                tail = null;
            } else if (node == tail) {
                tail = tail.prev;
                tail.next = null;
            } else if (node == head) {
                head = head.next;
                head.prev = null;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
            node.next = null;
            node.prev = null;
            length--;
        }


        public void printList() {
            Node walk = head;
            while (walk != null) {
                System.out.print("[" + walk + "] -> ");
                walk = walk.next;
            }
            System.out.println();
        }

    }
}