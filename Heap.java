package edu.sdsu.cs.datastructures;


import javax.sound.midi.Track;
import java.util.*;

/**
 * A heap data structure implementing the priority queue interface.
 * <p>An efficient, array-based priority queue data structure.
 * </p>
 *
 * @author Michael Tinglof, cssc0255
 */

public final class Heap<E> extends AbstractQueue<E> implements Queue<E> {

    final Comparator<E> comp;
    final List<E> storage;
    private int elements;
    int test;
    boolean min = false;


    /***
     * The collection constructor generates a new heap from the existing
     * collection using the enclosed item's natural ordering. Thus, these
     * items must support the Comparable interface.
     * @param col
     */
    public Heap(Collection<? extends E> col) {
        this();
        for (E info : col) {
            offer(info);
        }
    }

    /***
     * The default constructor generates a new heap using the natural order
     * of the objects inside. Consequently, all objects placed in this
     * container must implement the Comparable interface.
     */
    public Heap() {
        comp = (Comparator<E>) Comparator.naturalOrder();
        storage = new CirArrayList<>();
    }

    /***
     * Generates a new Heap from the provided collection using the specified
     * ordering. This allows the user to escape the natural ordering or
     * provide one in objects without.
     * @param col the collection to use
     * @param orderToUse the ordering to use when sorting the heap
     */
    public Heap(Collection<? extends E> col, Comparator<E> orderToUse) {
        this(orderToUse);
        for (E info : col) {
            offer(info);
        }
    }

    /***
     * Generates a new, empty heap using the Comparator object provided as
     * its parameter. Thus, items in this heap possess no interface
     * requirements.
     *
     * @param orderToUse
     */
    public Heap(Comparator<E> orderToUse) {
        comp = orderToUse;
        storage = new CirArrayList<>();
    }

    /***
     * An IN-PLACE sort function using heapsort.
     *
     * @param data a list of data to sort
     */
    public static <T> void sort(List<T> data) {
        Heap heaper = new Heap<>(data);
        int elements = data.size();
        data.clear();
        int i= 0;
        while (i < elements){
            data.add(i++, (T)heaper.poll());
        }
    }


    /***
     * An IN-PLACE sort function using heapsort.
     *
     * @param data a list of data to sort
     * @param order the comparator object expressing the desired order
     */
    public static <T> void sort(List<T> data, Comparator<T> order) {
        /**if (((Comparable)data.get(0)).compareTo(data.get(1)) < 0)
            if (order.compare(data.get(0), data.get(1)) > 0){
            sTor = true;
            }*/
        Heap heaper = new Heap(data, order);
        int elements = data.size();
        data.clear();
        int i= 0;
        while (i < elements){
            data.add(i++, (T)heaper.poll());
        }
    }


    /**
     * The iterator in this assignment provides the data to the user in heap
     * order. The lowest element will arrive first, but that is the only real
     * promise.
     *
     * @return an iterator to the heap
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            CirArrayList<E> temp = new CirArrayList(storage);
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < temp.size();
            }

            @Override
            public E next() {
                Tracker<E> a = (Tracker) temp.get(index++);
                return a.getInfo();
            }
        };
    }

    /***
     * Provides the caller with the number of items currently inside the heap.
     * @return the number of items in the heap.
     */
    @Override
    public int size() {
        return elements;
    }

    /***
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions. Heaps
     * represent priority queues, so the first element in the queue must
     * represent the item with the lowest ordering (highest priority).
     *
     * @param e element to offer the queue
     * @return
     */
    @Override
    public boolean offer(E e) {
        storage.add(elements, (E) new Tracker<>(elements, e));
        Tracker<E> a = (Tracker) storage.get(0);
        Tracker<E> b = (Tracker) storage.get(elements);
        if(elements == 1){
            test = comp.compare(b.getInfo(), a.getInfo());
            if(test == (((Comparable)b.getInfo()).compareTo(a.getInfo()))){
                min = true;
            }
        }
        trickleUp();
        return true;
    }

    /***
     * Retrieves and removes the head of this queue, or returns null if this
     * queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    @Override
    public E poll() {
        if (elements == 0) {
            return null;
        } else {
            Tracker<E> a = (Tracker)storage.get(0);
            trickleDown();
            return a.getInfo();
        }
    }

    /***
     * Retrieves, but does not remove, the head of this queue, or returns
     * null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    @Override
    public E peek() {
        if (elements == 0) {
            return null;
        } else {
            Tracker<E> a = (Tracker)storage.get(0);
            return a.getInfo();
        }
    }

    private void trickleDown() {
        Tracker<E> a = (Tracker)storage.get(elements-1);
        storage.set(0, (E)a);
        storage.remove(elements - 1);
        elements--;
        int parent = 0;
        boolean ready;
        if (elements == 0){
            ready = true;
        }
        else{
            ready = noBottom(parent);
        }
        if(min){
            while (!ready) {
                Tracker<E> o = (Tracker) storage.get(parent);
                if (noBottom(parent)) {
                    ready = true;
                } else if (onlyLeft(parent)) {
                    if (((Comparable) o.getInfo()).compareTo(getLeft(parent)) > 0) {
                        Tracker<E> b = (Tracker) storage.get(leftIndex(parent));
                        storage.set(parent, (E)b);
                        storage.set(leftIndex(parent), (E)o);
                        parent = leftIndex(parent);
                    }
                    else {
                        ready = true;
                    }
                } else if (((Comparable) o.getInfo()).compareTo(getLeft(parent)) > 0 ||
                        ((Comparable) o.getInfo()).compareTo(getRight(parent)) > 0) {
                    parent = compareChildren(parent);
                } else {
                    ready = true;
                }
            }
        }
        else{
            while (!ready) {
                Tracker<E> o = (Tracker) storage.get(parent);
                if (noBottom(parent)) {
                    ready = true;
                } else if (onlyLeft(parent)) {
                    if (((Comparable) o.getInfo()).compareTo(getLeft(parent)) < 0) {
                        Tracker<E> b = (Tracker) storage.get(leftIndex(parent));
                        storage.set(parent, (E)b);
                        storage.set(leftIndex(parent), (E)o);
                        parent = leftIndex(parent);
                    }
                    else {
                        ready = true;
                    }
                } else if (((Comparable) o.getInfo()).compareTo(getLeft(parent)) < 0 ||
                        ((Comparable) o.getInfo()).compareTo(getRight(parent)) < 0) {
                    parent = compareChildren(parent);
                } else {
                    ready = true;
                }
            }
        }

    }

    private void trickleUp() {
        boolean ready = false;
        int index = elements;
        elements++;
        if (index == 0) {
            return;
        }
        else if(min){
            while (!ready) {
                Tracker<E> b = (Tracker) storage.get(index);
                if (!hasParent(index)) {
                    ready = true;
                } else if (((Comparable) b.getInfo()).compareTo(getParent(index)) < 0) {
                    Tracker<E> a = (Tracker) storage.get(parentIndex(index));
                    storage.set(parentIndex(index), (E)b);
                    storage.set(index, (E)a);
                    index = parentIndex(index);
                }
                else if(((Comparable)b.getInfo()).compareTo(getParent(index)) == 0){
                    Tracker<E> a = (Tracker) storage.get(parentIndex(index));
                    if (b.getIndex() < a.getIndex()){
                        storage.set(parentIndex(index), (E)b);
                        storage.set(index, (E)a);
                        index = parentIndex(index);
                    }
                    else{
                        ready = true;
                    }
                }
                else {
                    ready = true;
                }
            }
        }
        else{
            while (!ready) {
                Tracker<E> b = (Tracker) storage.get(index);
                if (!hasParent(index)) {
                    ready = true;
                } else if (((Comparable) b.getInfo()).compareTo(getParent(index)) > 0) {
                    Tracker<E> a = (Tracker) storage.get(parentIndex(index));
                    storage.set(parentIndex(index), (E)b);
                    storage.set(index, (E)a);
                    index = parentIndex(index);
                }
                else if(((Comparable)b.getInfo()).compareTo(getParent(index)) == 0){
                    Tracker<E> a = (Tracker) storage.get(parentIndex(index));
                    if (b.getIndex() > a.getIndex()){
                        storage.set(parentIndex(index), (E)b);
                        storage.set(index, (E)a);
                        index = parentIndex(index);
                    }
                    else{
                        ready = true;
                    }
                }
                else {
                    ready = true;
                }
            }
        }
    }


    private int leftIndex(int parent) {
        return ((2 * parent) + 1);
    }

    private E getLeft(int parent) {
        Tracker<E> a = (Tracker) storage.get(leftIndex(parent));
        return a.getInfo();
    }

    private int rightIndex(int parent) {
        return ((2 * parent) + 2);
    }

    private E getRight(int parent) {
        Tracker<E> a = (Tracker) storage.get(rightIndex(parent));
        return a.getInfo();
    }

    private int parentIndex(int index) {
        if (index == 0) {
            return 0;
        } else if (index % 2 == 0) {
            return (index / 2) - 1;
        } else {
            index = index + 1;
            return (index / 2) - 1;
        }
    }

    private E getParent(int index) {
        Tracker<E> a = (Tracker) storage.get(parentIndex(index));
        return a.getInfo();
    }

    private boolean hasParent(int index) {
        if (storage.get(parentIndex(index)) == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean noBottom(int index) {
        return (leftIndex(index) > elements - 1);
    }

    private boolean onlyLeft(int index){
        return (leftIndex(index) == elements-1 && rightIndex(index) > elements-1);
    }

    private int compareChildren(int parent) {
        if (min) {
            if (((Comparable)getLeft(parent)).compareTo(getRight(parent)) < 0) {
                Tracker<E> a = (Tracker) storage.get(parent);
                Tracker<E> b = (Tracker) storage.get(leftIndex(parent));
                storage.set(parent, (E)b);
                storage.set(leftIndex(parent), (E)a);
                return leftIndex(parent);
            }
            else if (((Comparable)getLeft(parent)).compareTo(getRight(parent)) == 0){
                Tracker<E> a = (Tracker) storage.get(leftIndex(parent));
                Tracker<E> b = (Tracker) storage.get(rightIndex(parent));
                Tracker<E> c = (Tracker) storage.get(parent);
                if (a.getIndex() < b.getIndex()){
                    storage.set(parent, (E)a);
                    storage.set(leftIndex(parent), (E)c);
                    return leftIndex(parent);
                }
                else{
                    storage.set(parent, (E)b);
                    storage.set(leftIndex(parent), (E)c);
                    return leftIndex(parent);
                    }
            }
            else {
                Tracker<E> a = (Tracker) storage.get(parent);
                Tracker<E> b = (Tracker) storage.get(rightIndex(parent));
                storage.set(parent, (E)b);
                storage.set(rightIndex(parent), (E)a);
                return rightIndex(parent);
            }
        }
        else{
            if(((Comparable) getLeft(parent)).compareTo(getRight(parent)) > 0){
                Tracker<E> a = (Tracker) storage.get(parent);
                Tracker<E> b = (Tracker) storage.get(leftIndex(parent));
                storage.set(parent, (E)b);
                storage.set(leftIndex(parent), (E)a);
                return leftIndex(parent);
            }
            else if (((Comparable)getLeft(parent)).compareTo(getRight(parent)) == 0){
                Tracker<E> a = (Tracker) storage.get(leftIndex(parent));
                Tracker<E> b = (Tracker) storage.get(rightIndex(parent));
                Tracker<E> c = (Tracker) storage.get(parent);
                if (a.getIndex() > b.getIndex()){
                    storage.set(parent, (E)a);
                    storage.set(leftIndex(parent), (E)c);
                    return leftIndex(parent);
                }
                else{
                    storage.set(parent, (E)b);
                    storage.set(leftIndex(parent), (E)c);
                    return leftIndex(parent);
                }
            }
            else{
                Tracker<E> a = (Tracker) storage.get(parent);
                Tracker<E> b = (Tracker) storage.get(rightIndex(parent));
                storage.set(parent, (E)b);
                storage.set(rightIndex(parent), (E)a);
                return rightIndex(parent);
            }
        }
    }
}

    class Tracker<E> {
        private E info;
        private int index;

        protected Tracker(int i, E e) {
            info = e;
            index = i;
        }

        public E getInfo() {
            return info;
        }

        public int getIndex() {
            return index;
        }
}
