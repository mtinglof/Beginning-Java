package edu.sdsu.cs.datastructures;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * A circular version of an array list.
 * <p>Operates as a standard java.util.ArrayList, but additions and removals
 * from the List's front occur in constant time, for its circular nature
 * eliminates the shifting required when adding or removing elements to the
 * front of the ArrayList.
 * </p>
 *
 * @author STUDENT NAME HERE, csscXXXX
 */
public final class CirArrayList<E> extends AbstractList<E> implements
        List<E>, RandomAccess {

    private E[] storage;
    private final int DEFAULT_SIZE = 20;
    private int front = 0;
    private int back = 0;
    private int currSize = 0;

    /**
     * Builds a new, empty CirArrayList.
     */
    public CirArrayList() {
        storage = (E[]) (new Object[DEFAULT_SIZE]);
    }

    /**
     * Constructs a new CirArrayList containing all the items in the input
     * parameter.
     *
     * @param col the Collection from which to base
     */
    public CirArrayList(Collection<? extends E> col) {
        storage = (E[]) col.toArray(new Object[col.size() + DEFAULT_SIZE]);
        currSize = col.size();
        back = col.size()-1;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index (0 based) of the element to return.
     * @return element at the specified position in the list.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0
     *                                   || index >= maxSize())
     */
    @Override
    public E get(int index) {
        if (index < 0 || size() == 0) {
            throw new IndexOutOfBoundsException();
        }
        return storage[arrayIndex(index)];
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param value element to be stored at teh specified position
     * @return element previously at the specified position
     * @throws IndexOutOfBoundsException if index is out of the range (index < 0
     *                                   || index >= maxSize())
     */
    @Override
    public E set(int index, E value) {
        if (index < 0 || size() == 0){
            throw new IndexOutOfBoundsException();
        }
        E tempVariable = storage[arrayIndex(index)];
        storage[arrayIndex(index)] = value;
        return tempVariable;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param value element to be inserted
     */
    @Override
    public void add(int index, E value) {
        if(currSize == 0){
            storage[arrayIndex(index)] = value;
            back = arrayIndex(index);
        }
        else{
            for (int i= arrayIndex(back); i >= arrayIndex(index); i--) {
                storage[arrayIndex(i+1)] = storage[arrayIndex(i)];
            }
            storage[arrayIndex(index)] = value;
            back = arrayIndex(back+1);
        }
        currSize++;
        final double EXPAND = .75;
        if (((double)currSize / (double)storage.length) > EXPAND){
            expand();
        }

    }

    /**
     * Removes the element at the specified position in this list.  Shifts
     * any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index index of element to remove
     * @return the element previously at the specified position.
     */
    @Override
    public E remove(int index) {
        E removedElement = storage[arrayIndex(index)];
        if (front == back){
            storage[arrayIndex(index)] = null;
        }
        else if(arrayIndex(index) == back){
            storage[arrayIndex(index)] = null;
            back--;
        }
        else{
            for (int i=index; i < currSize; i++){
                storage[arrayIndex(i)] = storage[arrayIndex(i+1)];
            }
            storage[back] = null;
            back = arrayIndex(back -1);
        }
        currSize--;
        return removedElement;
    }

    /**
     * Reports the number of items in the List.
     *
     * @return the item count.
     */
    @Override
    public int size() {
        return currSize;
    }
    private void expand(){
        E[] tempStorage = (E[]) (new Object[storage.length*2]);
        int i;
        for (i=0; i <= arrayIndex(back) ; i++){
            tempStorage[i] = storage[arrayIndex(i)];
        }
        front=0;
        back=size()-1;
        storage = tempStorage;
    }
    private int arrayIndex(int index){
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        else if(index + front <= storage.length-1){
            return (index + front);
        }
        else{
            return ((front + index) - storage.length)+1;
        }
    }
}
