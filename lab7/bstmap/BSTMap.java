package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        private Node(K key, V val, Node l, Node r) {
            this.key = key;
            this.value = val;
            this.left = l;
            this.right = r;
        }
    }

    private int size;
    private Node root;

    public BSTMap() {
        size = 0;
        root = null;
    }


    @Override
       /** Removes all of the mappings from this map. */
    public void clear(){
        root = null;
    }

    // Helper function for containsKey() and get()
    private Node searchKey(Node current, K key) {
        if (current == null) {
            return null;
        } else if (current.key == key) {
            return current;
        } else {
            if (current.key.compareTo(key) > 0){
                return searchKey(current.left, key);
            } else {
                return searchKey(current.right, key);
            }
        }
    }

    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
        return searchKey(root, key) != null;
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        Node target = searchKey(root, key);
        if (target != null) {
            return target.value;
        }
        return null;
    }

    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    private Node insert(Node current, K key, V value) {
        if (current == null) {
            return new Node(key, value, null, null);
        } else if (current.key.compareTo(key) > 0){
            current.left = insert(current.left, key, value);
            return current;
        } else {
            current.right = insert(current.left, key, value);
            return current;
        }
    }

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        Node target = searchKey(root, key);
        if (target == null) {
            root = insert(root, key, value);
        }
    }

    // Helper method for keySet()
    private void addKeys(Node current, HashSet<K> s) {
        if (current != null) {
            s.add(current.key);
            addKeys(current.left, s);
            addKeys(current.right, s);
        }
    }

    @Override
    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        HashSet<K> keySet = new HashSet<>();
        addKeys(root, keySet);
        return keySet;
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }  

    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    // Helper method for printInOrder
    private void inOrderTraversal(Node current) {
        if (current != null) {
            inOrderTraversal(current.left);
            System.out.println(current.key);
            inOrderTraversal(current.right);
        }
    }

    // print the keys in increasing order
    // performs in order traversal
    public void printInOrder() {
        inOrderTraversal(root);
    }
}
