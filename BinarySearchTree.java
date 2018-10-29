package edu.sdsu.cs.datastructures;

import java.lang.reflect.Array;
import java.util.*;

public class BinarySearchTree<K extends Comparable<K>,V> implements MapADT<K,V> {

    private LinkedList<Entry <K, V>> tree;
    public Entry<K,V> root;
    public int elements=0;

    public BinarySearchTree(){
        tree = new LinkedList<>();
    }

    public static class Entry<K,V> {
        K key;
        V value;
        Entry<K, V> rightChild = null;
        Entry<K, V> leftChild = null;
        Entry<K, V> parent = null;

        public Entry(K k, V v) {
            key = k;
            value = v;
        }

        public Entry<K, V> getRight(K key){
            return rightChild;
        }

        public Entry<K, V> getLeft(K key){
            return leftChild;
        }

        public Entry<K, V> getParent (K key){
            return parent;
        }

        public void setRight (Entry<K, V> n){
            rightChild = n;
        }

        public void setLeft (Entry<K, V> n){
            leftChild = n;
        }

        public void setParent (Entry<K, V> n){
            parent = n;
        }


    }

    /**
     * Returns true if the map has an object for the corresponding key.
     *
     * @param key object to search for
     * @return true if within map, false otherwise
     */
    public boolean contains(K key){
        return findKey(key);
    }

    /**
     * Adds the given key/value pair to the map.
     *
     * @param key   Key to add to the map
     * @param value Corresponding value to associate with the key
     * @return the previous value associated with this key or null if new
     */
    public V add(K key, V value){
        Entry<K, V> entry = new Entry<>(key, value);
        if (tree.size() == 0){
            tree.add(entry);
            setRoot(entry);
            elements++;
            return null;
        }
        else{
            if(findKey(entry.key)){
                Entry<K, V> copy = tree.get(getIndex(key));
                return copy.value;
            }
            tree.add(entry);
            elements++;
            place(entry);
            return null;
        }
}

    /**
     * Removes the key/value pair identified by the key parameter from the map.
     *
     * @param key item to remove
     * @return true if removed, false if not found or unable to remove
     */
    public boolean delete(K key){
        if (findKey(key) && elements > 0){
            int spot = getIndex(key);
            Entry<K, V> index = tree.get(spot);
            if (!hasLeft(index) && !hasRight(index)){
                return noChild(index);
            }
            else if(hasRight(index)&& hasLeft(index)){
                return twoChild(index);
            }
            else if (hasRight(index) && !hasLeft(index)){
                return rightChild(index);
            }
            else if (!hasRight(index) && hasLeft(index)){
                return leftChild(index);
            }
            else{ return false; }
        }
        return false;
    }

    private boolean isRoot (Entry<K, V> test){
        if (test == getRoot()){ return true; }
        else { return false; }
    }

    private void setChild (Entry<K, V> old, Entry<K, V> replacement){
        Entry<K, V> parent = old.getParent(old.key);
        if ((old.key).compareTo(parent.key) < 0){
            parent.setLeft(replacement);
            replacement.setParent(parent);
        }
        else{
            parent.setRight(replacement);
            replacement.setParent(parent);
        }
    }

    private boolean twoChild (Entry<K, V> index){
        Entry<K, V> parent;
        Entry<K, V> right;
        Entry <K, V> left = index.getLeft(index.key);
        if (hasRight(left)){
            right = left.getRight(left.key);
            while(hasRight(right)){
                right = right.getRight(right.key);
            }
            if(hasLeft(right)){
                parent = right.getParent(right.key);
                left = right.getLeft(right.key);
                left.setParent(parent);
                parent.setRight(left);
            }
            else{
                parent = right.getParent(right.key);
                parent.setRight(null);
            }
            right.setRight(index.getRight(index.key));
            right.setLeft(index.getLeft(index.key));
            (index.getLeft(index.key)).setParent(right);
            (index.getRight(index.key)).setParent(right);
            if (isRoot(index)){
                setRoot(right);
            }
            else{ setChild(index, right);}
            tree.remove(index);
            elements--;
            return true;
        }
        else if (hasLeft(left) || !hasLeft(left)){
            right = index.getRight(index.key);
            right.setParent(left);
            left.setRight(right);
            if (isRoot(index)){
                setRoot(left);
            }
            else { setChild(index, left);}
            tree.remove(index);
            elements--;
            return true;
        }
        else { return false; }
    }

    private boolean rightChild (Entry<K, V> index){
        Entry<K, V> right = index.getRight(index.key);
        if (isRoot(index)){
            setRoot(right);
        }
        else{ setChild(index, right);}
        tree.remove(index);
        elements--;
        return true;
    }

    private boolean leftChild (Entry<K, V> index){
        Entry<K, V> left = index.getLeft(index.key);
        if (isRoot(index)){
            setRoot(left);
        }
        else {setChild(index, left);}
        tree.remove(index);
        elements--;
        return true;
    }

    private boolean noChild(Entry<K, V> index){
        if (isRoot(index)){
            tree.remove(index);
            elements--;
            return true;
        }
        else{
            Entry<K, V> parent = index.getParent(index.key);
            if ((index.key).compareTo(parent.key) < 0){
                parent.setLeft(null);
            }
            else{
                parent.setRight(null);
            }
            tree.remove(index);
            elements--;
            return true;
        }
    }

    /**
     * Returns the value associated with the parameter key.
     *
     * @param key key to lookup in the map
     * @return Value associated with key or null if not found
     */
    public V getValue(K key){
        Entry<K, V> temp = getRoot();
        boolean ready = false;
        if (tree.isEmpty()){
            ready = true;
        }
        while (!ready){
            if ((key).compareTo(temp.key) > 0){
                if (hasRight(temp)){
                    temp = temp.getRight(temp.key);
                }
                else{ return null; }
            }
            else if ((key).compareTo(temp.key) < 0){
                if (hasLeft(temp)){
                    temp = temp.getLeft(temp.key);
                }
                else{ return null; }
            }
            else if ((key).compareTo(temp.key) == 0){ return temp.value; }
            else{ return null; }
        }
        return null;
    }

    /**
     * Returns the first key found with the parameter value.
     *
     * @param value value to locate
     * @return key of first item found with the matching value
     */
    public K getKey(V value){
        for(int i=0; i < tree.size(); i++){
            if (tree.get(i).value.equals(value)){
                return tree.get(i).key;
            }
        }
        return null;
    }

    /**
     * Identifies the maxSize of the map.
     *
     * @return Number of entries stored in the map.
     */
    public int size(){ return elements; }
    /**
     * Indicates if the map contains nothing.
     * @return true if the map is empty, as the method cryptically indicates.
     */
    public boolean isEmpty(){
        if (elements == 0){
            return true;
        }
        return false;
    }
    /**
     * Resets the map to an empty state with no entries.
     */
    public void clear(){
        tree = new LinkedList<>();
        elements = 0;
    }
    /**
     * Provides a key iterator.
     * @return Iterator over the keys (some data structures provided sorted)
     */

    private K keyHelp(int i){
        LinkedList<Entry<K, V>> temp = tree;
        for (int out = temp.size()-1; out > 0; out--){
            for (int in = 0; in < out; in++){
                if ((temp.get(in).key).compareTo(temp.get(in+1).key) > 0){
                    Entry<K, V> entry = temp.get(in);
                    temp.set(in, temp.get(in+1));
                    temp.set(in+1, entry);
                }
            }
        }
        return temp.get(i).key;
    }

    private V valueHelp(int i){
        LinkedList<Entry<K, V>> temp = tree;
        for (int out = temp.size()-1; out > 0; out--){
            for (int in = 0; in < out; in++){
                if ((temp.get(in).key).compareTo(temp.get(in+1).key) > 0){
                    Entry<K, V> entry = temp.get(in);
                    temp.set(in, temp.get(in+1));
                    temp.set(in+1, entry);
                }
            }
        }
        return temp.get(i).value;
    }


    public Iterator<K> keys(){
        return new Iterator<K>() {
            int curr = 0;
            @Override
            public boolean hasNext() {
                return (curr < tree.size());
            }

            @Override
            public K next() { return keyHelp(curr++); }
        };
    }

    /**
     * Provides a value iterator. The values arrive corresponding to their
     * keys in the key order.
     * @return Iterator over the values.
     */
    public Iterator<V> values(){
        return new Iterator<V>() {
            int curr = 0;
            @Override
            public boolean hasNext() { return (curr < tree.size()); }

            @Override
            public V next() { return valueHelp(curr++); }
        };
    }

    private V place (Entry<K, V> entry){
        Entry<K, V> temp = root;
        boolean ready = false;
        while (!ready){
            if ((entry.key).compareTo(temp.key) == 0) {
                return entry.value;
            }
            else if ((entry.key).compareTo(temp.key) > 0){
                if (hasRight(temp)){
                    temp = (temp.getRight(temp.key));
                }
                else{
                    temp.setRight(entry);
                    entry.setParent(temp);
                    return entry.value;
                }
            }
            else if ((entry.key).compareTo(temp.key) < 0){
                if (hasLeft(temp)){
                    temp = (temp.getLeft(temp.key));
                }
                else{
                    temp.setLeft(entry);
                    entry.setParent(temp);
                    return entry.value;
                }
            }
            else{
                return null;
            }
        }
        return null;
    }

    private boolean findKey(K key){
        if (getRoot() != null){
            Entry<K, V> find = getRoot();
            boolean ready = false;
            while (!ready){
                if ((key).compareTo(find.key) == 0){ return true; }
                else if ((key).compareTo(find.key) > 0){
                    if (hasRight(find)) {
                        find = find.getRight(find.key);
                    }
                    else{ return false; }
                }
                else if ((key).compareTo(find.key) < 0){
                    if (hasLeft(find)){
                        find = find.getLeft(find.key);
                    }
                    else{ return false; }
                }
                else{ return false; }
            }
        }
        return false;
    }

    private boolean hasRight (Entry<K, V> entry){
        if (entry.getRight(entry.key) == null){
            return false;
        }
        else{ return true; }
    }

    private boolean hasLeft (Entry<K, V> entry){
        if (entry.getLeft(entry.key) ==  null){
            return false;
        }
        else{ return true;}
    }

    private int getIndex(K key){
        for(int i = 0; i < tree.size(); i++){
            if (key.equals(tree.get(i).key)){
                return i;
            }
        }
        return -1;
    }

    private void setRoot(Entry<K, V> entry){
        entry.setParent(null);
        root = entry;
    }

    private Entry<K, V> getRoot() {
        return root;
    }
}
