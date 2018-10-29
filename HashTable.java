package edu.sdsu.cs.datastructures;

import java.util.*;


public class HashTable<K extends Comparable<K>,V> implements MapADT<K,V> {

    final int DEFAULTSIZE = 3;
    public int currSize=0;
    private List<Entry<K, V>>[] buckets;
    public int lastPrime;

    public static class Entry<K,V> {
        K key;
        V value;

        public Entry(K k, V v) {
            key = k;
            value = v;
        }
    }

    public HashTable(){
        buckets = new List[DEFAULTSIZE];
        for(int curBin = 0; curBin < DEFAULTSIZE; curBin++){
            buckets[curBin] = new LinkedList<>();
        }
    }

    public HashTable(int inital) {
        buckets = new List[inital];
        for (int curBin = 0; curBin < inital; curBin++) {
            buckets[curBin] = new LinkedList<>();
        }
    }


    /**
     * Returns true if the map has an object for the corresponding key.
     *
     * @param key object to search for
     * @return true if within map, false otherwise
     */

    public boolean contains(K key) {
        int index = getHash(key);
        if(buckets[index] != null){
            for (int i = 0; i < buckets[index].size(); i++){
                if (buckets[index].get(i).key.equals(key)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the given key/value pair to the map.
     *
     * @param key   Key to add to the map
     * @param value Corresponding value to associate with the key
     * @return the previous value associated with this key or null if new
     */
    public V add(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        int index = getHash(key);
        int spot = getIndex(key);
        if(contains(key)){
            Entry<K, V> temp = buckets[index].get(spot);
            buckets[index].set((spot), entry);
            return temp.value;
        }
        else{
            buckets[index].add(entry);
            currSize++;
            check();
            return null;
        }
    }

    /**
     * Removes the key/value pair identified by the key parameter from the map.
     *
     * @param key item to remove
     * @return true if removed, false if not found or unable to remove
     */

    public boolean delete(K key) {
        if(contains(key)) {
            int index = getHash(key);
            int spot = getIndex(key);
            buckets[index].remove(spot);
            currSize--;
            check();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Returns the value associated with the parameter key.
     *
     * @param key key to lookup in the map
     * @return Value associated with key or null if not found
     */
    public V getValue(K key) {
        if(contains(key)) {
            int index = getHash(key);
            int spot = getIndex(key);
            Entry<K, V> temp = buckets[index].get(spot);
            return temp.value;
        }
        else{
            return null;
        }
    }

    /**
     * Returns the first key found with the parameter value.
     *
     * @param value value to locate
     * @return key of first item found with the matching value
     */

    public K getKey(V value) {
        for (int i = 0; i < buckets.length; i++){
            if(buckets[i] != null){
                for(int j = 0; j < buckets[i].size(); j++){
                    if(buckets[i].get(j).value.equals(value)){
                        return buckets[i].get(j).key;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Identifies the maxSize of the map.
     *
     * @return Number of entries stored in the map.
     */
    public int size() {
        return currSize;
    }

    /**
     * Indicates if the map contains nothing.
     *
     * @return true if the map is empty, as the method cryptically indicates.
     */
    public boolean isEmpty() {
        if(size()==0){
            return true;
        }
        return false;
    }

    /**
     * Resets the map to an empty state with no entries.
     */
    public void clear() {
        buckets = new List[DEFAULTSIZE];
        for(int curBin = 0; curBin < DEFAULTSIZE; curBin++){
            buckets[curBin] = new LinkedList<>();
        }
    }

    /**
     * Provides a key iterator.
     *
     * @return Iterator over the keys (some data structures provided sorted)
     */
    public Iterator<K> keys() {
        List<Entry<K, V>> temp = new ArrayList<>();
        for (int i = 0; i < buckets.length; i++){
            if(buckets[i].size() != 0){
                for (int j = 0; j < buckets[i].size(); j++){
                    temp.add(buckets[i].get(j));
                }
            }
        }
        return new Iterator<K>() {
            int index = 0;
            @Override
            public boolean hasNext() {
               return (index < temp.size());
            }

            @Override
            public K next() {
                return temp.get(index++).key;
            }
        };
    }

    /**
     * Provides a value iterator. The values arrive corresponding to their
     * keys in the key order.
     *
     * @return Iterator over the values.
     */
    public Iterator<V> values() {
        List<Entry<K, V>> temp = new ArrayList<>();
        for (int i = 0; i < buckets.length; i++){
            if(buckets[i].size() != 0){
                for (int j = 0; j < buckets[i].size(); j++){
                    temp.add(buckets[i].get(j));
                }
            }
        }
        return new Iterator<V>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return (index < currSize);
            }

            @Override
            public V next() {
                return temp.get(index++).value;
            }
        };
    }

    private int primeGen(int num) {
        boolean ready = false;
        lastPrime =num;
        num=num*2;
        do{
            num++;
            for (int i = 2; i < num; i++) {
                if(num%i == 0){
                    break;
                }
                else if(i+1 == num){
                    ready = true;
                }
            }
        }while(!ready);
        return num;
    }

    private void check(){
        if ((double)size()/(double)buckets.length > .75){
            grow();
        }
        else if((double)size()/(double)buckets.length < .15){
            shrink();
        }
    }


    private void grow(){
        int newSize = primeGen(buckets.length);
        List<Entry<K, V>>[] temp = new List[newSize];
        for(int curBin = 0; curBin < temp.length; curBin++){
            temp[curBin] = new LinkedList<>();
        }
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (int j = 0; j < buckets[i].size(); j++) {
                    Entry<K, V> insert = buckets[i].get(j);
                    K key = insert.key;
                    int index = key.hashCode()%newSize;
                    if (index < 0){
                        index = index * -1;
                    }
                    temp[index].add(insert);
                }
            }
        }
        buckets = temp;
    }

    private void shrink() {
        List<Entry<K, V>>[] temp = new List[lastPrime];
        for (int curBin = 0; curBin < lastPrime; curBin++) {
            temp[curBin] = new LinkedList<>();
        }
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (int j = 0; j < buckets[i].size(); j++) {
                    Entry<K, V> insert = buckets[i].get(j);
                    K key = insert.key;
                    int index = key.hashCode()%lastPrime;
                    if (index < 0){
                        index = index * -1;
                    }
                    temp[index].add(insert);
                }
            }
        }
        buckets = temp;
    }

    private int getHash(K k){
        int hash = k.hashCode()%buckets.length;
        if (hash < 0){
            hash = hash * -1;
            return hash;
        }
        return hash;
    }

    private int getIndex(K key){
        int index = getHash(key);
        for(int i= 0; i < buckets[index].size(); i++){
            if ((buckets[index].get(i).key).equals(key)){
                return i;
            }
        }
        return -1;
    }
}



