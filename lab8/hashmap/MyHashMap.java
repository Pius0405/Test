package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int count = 0; //number of elements in the HashMap
    private double maxLoad = 0.75; //default load factor is 0.75
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() { 
        buckets = createTable(16); //initial size 16 by default
    }

    public MyHashMap(int initialSize) { 
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.maxLoad = maxLoad;
        buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = (Collection<Node>[]) new Collection[tableSize];
        for (int i = 0; i < tableSize; ++i) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
        /** Removes all of the mappings from this map. */
    public void clear() {
        count = 0;
        buckets = createTable(16);
    }

    // Helper function for contains key
    private int getIndex(K key, int tableSize) {
        int hashCode = key.hashCode();
        return Math.floorMod(hashCode, tableSize);
    }


    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        int index = getIndex(key, buckets.length);
        for (Node n : buckets[index]) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        int index = getIndex(key, buckets.length);
        for (Node n : buckets[index]) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return count;
    }

    private void resize() {
        float load = count/buckets.length;
        if (load > maxLoad) {
            Collection<Node>[] newTable = createTable(buckets.length * 2);
            for (Collection<Node> bucket: buckets) {
                for (Node n : bucket) {
                    int index = getIndex(n.key, buckets.length * 2);
                    newTable[index].add(n);
                }
            }
            buckets = newTable;   
        }
    }

    @Override
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        boolean added = false;
        int index = getIndex(key, buckets.length);
        for (Node n : buckets[index]) {
            if (n.key.equals(key)) {
                n.value = value;
                added = true;
                break;
            }
        }
        if (! added) {
            buckets[index].add(new Node(key, value));
            ++count;
            resize();
        }

    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        HashSet<K> keySet = new HashSet<>();
        for (int i = 0; i < buckets.length; ++i) {
            for (Node n : buckets[i]) {
                keySet.add(n.key);
            }
        }
        return keySet;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        V val = get(key);
        if (val != null) { // Use the mapping of the key to determine if 
            int index = getIndex(key, buckets.length);
            for (Node n: buckets[index]) {
                if (n.key.equals(key)) {
                    buckets[index].remove(n);
                    --count;
                    break;
                }
            }
        }
        return val;
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        V val = get(key);
        if (val == value) {
            int index = getIndex(key, buckets.length);
            for (Node n: buckets[index]) {
                if (n.key.equals(key)) {
                    buckets[index].remove(n);
                    --count;
                    return val;
                }
            }
        }
        return val;
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    } 

}
