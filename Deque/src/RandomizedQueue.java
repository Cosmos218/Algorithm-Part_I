import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] array;

    public RandomizedQueue() {
        size = 0;
        array = (Item[]) new Object[1];
    }                // construct an empty randomized queue

    public boolean isEmpty() {
        return size == 0;
    }                // is the randomized queue empty?

    public int size() {
        return size;
    }                       // return the number of items on the randomized queue

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size >= array.length)
            resize(array.length * 2);
        array[size++] = item;
    }          // add the item

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(array, 0, copy, 0, size);
        array = copy;
    }

    public Item dequeue() {
        if (size == 0) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniform(size);
        Item result = array[i];
        array[i] = array[size - 1];
        array[--size] = null;
        if (size > 0 && size == array.length / 4) resize(array.length / 2);
        return result;
    }                   // remove and return a random item

    public Item sample() {
        if (size == 0) throw new java.util.NoSuchElementException();
        return array[StdRandom.uniform(size)];
    }                    // return a random item (but do not remove it)

    private class RQiterator implements Iterator<Item> {
        private Item[] a;
        private int i = -1;

        public RQiterator() {
            Item[] copy = (Item[]) new Object[size];
            System.arraycopy(array, 0, copy, 0, size);
            StdRandom.shuffle(copy);
            a = copy;
        }

        @Override
        public boolean hasNext() {
            return i != a.length - 1;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[++i];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new RQiterator();
    }        // return an independent iterator over items in random order

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        int i = 4;
        rq.enqueue(i++);
        rq.enqueue(i++);
        rq.enqueue(i++);
        Iterator<Integer> it = rq.iterator();
        while (it.hasNext())
            System.out.println(it.next());

    }  // unit testing (optional)
}
