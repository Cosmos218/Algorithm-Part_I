import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node prev;
        Node next;
    }

    public Deque() {
        size = 0;
        first = null;
        last = null;
    }                          // construct an empty deque

    public boolean isEmpty() {
        return size == 0;
    }                // is the deque empty?

    public int size() {
        return size;
    }                       // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        size++;
        if (first == null) {
            first = new Node();
            last = first;
            first.item = item;
            return;
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        oldFirst.prev = first;
    }         // add the item to the front

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        size++;
        if (last == null) {
            first = new Node();
            last = first;
            first.item = item;
            return;
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        oldLast.next = last;
    }          // add the item to the end

    public Item removeFirst() {
        if (first == null) throw new NoSuchElementException();
        size--;
        Item result = first.item;
        first = first.next;
        if (first == null) {
            last = null;
            return result;
        }
        first.prev = null;
        return result;
    }               // remove and return the item from the front

    public Item removeLast() {
        if (last == null) throw new NoSuchElementException();
        size--;
        Item result = last.item;
        last = last.prev;
        if (last == null) {
            first = null;
            return result;
        }
        last.next = null;
        return result;
    }                // remove and return the item from the end

    private class DequeIterator implements Iterator<Item> {
        Node p = first;

        @Override
        public boolean hasNext() {
            return p != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item result = p.item;
            p = p.next;
            return result;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }        // return an iterator over items in order from front to end

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addLast(4);
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());
        deque.removeFirst();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        Iterator<Integer> iterator2 = deque.iterator();
        while (iterator2.hasNext())
            System.out.println(iterator2.next());
    } // unit testing (optional)
}
