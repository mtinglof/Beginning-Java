package edu.sdsu.cs.datastructures;

import java.util.AbstractCollection;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * An array-based priority managedList implementation.
 * <p>
 * A first-in-first-out (FIFO) data container, the basic managedList represents
 * a line arranged in the order the items arrive. This managedList differs from
 * the standard implementation in that it requires all enclosed objects
 * support comparison with like-type objects (Comparable), and it
 * automatically arranges the items in the managedList based upon their ranking.
 * Lower items appear closer to the managedList's front than larger items.
 * </p>
 * Due to their nature, queues must support rapid enqueue (offer) and
 * dequeue (poll) operations. That is, getting in and out of line must
 * occur rapidly. In this priority managedList, the poll operation shall occur
 * in constant time, but because the managedList requires arrangement, adding
 * to the circular-array based priority managedList may produce slower timings,
 * for the data structure must use binary-search to locate where the new
 * item belongs in relation to everything else, and it must then shift
 * the existing contents over to make room for the addition.
 * </p>
 * <p>
 * In the likely event two objects share the same priority, new items
 * added to the managedList shall appear after existing entries. When a standard
 * priority customer enters the line, it should naturally go behind the
 * last standard customer currently in line.
 * </p>
 * <p>
 * Although technically a Collection object, the managedList does not support
 * most of the standard Collection operations. That is, one may not add
 * or remove from the Queue using the collection methods, for it breaks
 * the abstraction. One may, however, use a Queue object as an input
 * parameter to any other Collection object constructor.
 * </p>
 *
 * @param <E> Object to store in container. It must support comparisons with
 *            other objects of the same type.
 * @author Michael Tinglof
 */
public final class ArrayPriorityQueue<E extends Comparable<? extends E>>
        extends AbstractCollection<E> implements Queue<E> {

    private final List<E> managedList = new CirArrayList<>();

    /**
     * Builds a new, empty priority managedList.
     */
    public ArrayPriorityQueue() {
        managedList.toArray();
    }

    /**
     * Builds a new priority managedList containing every item in the provided
     * collection.
     *
     * @param col the Collection containing the objects to add to this
     *            managedList.
     */
    public ArrayPriorityQueue(Collection<? extends E> col) {
        for (E itemInExistingCollection : col) {
            offer(itemInExistingCollection);
            binarySearch(itemInExistingCollection);
        }
    }

    /**
     * Inserts the specified element into this managedList if it is possible to do
     * so immediately without violating capacity restrictions. When using a
     * capacity-restricted managedList, this method is generally preferable to add
     * (E), which can fail to insert an element only by throwing an exception.
     *
     * @param e the element to add
     * @return true if element added to managedList, false otherwise
     */
    @Override
    public boolean offer(E e) {
        if (managedList.add(e)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves and removes the head of this managedList. This method
     * differs from
     * poll only in that it throws an exception if this managedList is empty.
     *
     * @return the managedList's head
     * @throws java.util.NoSuchElementException if this managedList is empty
     */
    @Override
    public E remove() {
        if (managedList.size() == 0) {
            throw new NoSuchElementException();
        } else {
            E tempVariable = managedList.get(0);
            managedList.remove(0);
            return tempVariable;
        }
    }

    /**
     * Retrieves and removes the head of this managedList, or returns null if
     * this
     * managedList is empty.
     *
     * @return this managedList's head or null if this managedList is empty.
     */
    @Override
    public E poll() {
        if (managedList.size() == 0) {
            return null;
        } else {
            E tempVariable = managedList.get(0);
            managedList.remove(0);
            return tempVariable;
        }

    }

    /**
     * Retrieves, but does not remove, the head of this managedList. This method
     * differs from peek only in that it throws an exception if this managedList is
     * empty.
     *
     * @return the head of this managedList
     * @throws java.util.NoSuchElementException if this managedList is empty
     */
    @Override
    public E element() {
        if (managedList.size() == 0) {
            throw new NoSuchElementException();
        } else {
            return managedList.get(0);
        }
    }

    /**
     * Retrieves, but does not remove, the head of this managedList, or returns
     * null if this managedList is empty.
     *
     * @return the head of this managedList or null if this managedList is empty
     */
    @Override
    public E peek() {
        if (managedList.size() == 0) {
            return null;
        } else {
            return managedList.get(0);
        }
    }

    /**
     * Returns an iterator over the elements in this collection. The iterator
     * produces results in the order in which they would appear were one to
     * successively poll the managedList.
     *
     * @return an Iterator over the elements in this managedList.
     */
    @Override
    public Iterator<E> iterator() {
        return managedList.iterator();
    }

    /**
     * Reports the number of items in this managedList.
     *
     * @return the number of items in this managedList.
     * @implNote it is INCORRECT to track this as a field in this class.
     */
    @Override
    public int size() {
        return managedList.size();
    }

    public void binarySearch(E elementOne) {
        int low = 0;
        int high = size() - 1;
        if (size() == 1) {
            managedList.set(0, elementOne);
            return;
        }
        if (size() == 2) {
            if (((Comparable) (managedList.get(0))).compareTo(elementOne) == 0 || ((Comparable) (managedList.get(0))).compareTo(elementOne) < 0) {
                managedList.set(1, elementOne);
            } else {
                managedList.set(1, managedList.get(0));
                managedList.set(0, elementOne);
            }
            return;
        }
        while (high >= low) {
            int middle = (low + high) / 2;
            if (((Comparable) (managedList.get(middle))).compareTo(elementOne) == 0) {
                for (int i = size() - 1; i > middle; i--) {
                    managedList.set(i, managedList.get(i - 1));
                }
                managedList.set(middle, elementOne);
                return;
            }
            if (((Comparable) (managedList.get(middle))).compareTo(elementOne) < 0) {
                low = middle + 1;
                if (low > high){
                    for (int i = size() - 1; i > middle; i--) {
                        managedList.set(i, managedList.get(i - 1));
                    }
                    managedList.set(low, elementOne);
                    return;
                }
            }
            if (((Comparable) (managedList.get(middle))).compareTo(elementOne) > 0) {
                high = middle - 1;
                if (low > high){
                    for (int i = size() - 1; i > middle; i--) {
                        managedList.set(i, managedList.get(i - 1));
                    }
                    managedList.set(low, elementOne);
                    return;
                }
            }
        }


    }
    public E binarySearch2() {
        int low = 0;
        int high = size() - 1;
        while (high >= low) {
            int middle = (low + high) / 2;
            if (((Comparable) (managedList.get(middle))).compareTo(low) == 0 || (((Comparable) (managedList.get(middle))).compareTo(high) == 0)){
                return managedList.get(low);
            }
            if (((Comparable) (managedList.get(middle))).compareTo(low) < 0) {
                low = middle + 1;
                if (low > high) {
                    return managedList.get(low);
                }
            }
            if (((Comparable) (managedList.get(middle))).compareTo(high) > 0) {
                high = middle - 1;
                if (low > high) {
                    return managedList.get(low);
                }
            }
        }
        return managedList.get(0);
    }
}