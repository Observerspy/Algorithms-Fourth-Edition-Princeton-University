import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int len;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node pre;
        private Node next;
    }
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        len = 0;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }
    // return the number of items on the deque
    public int size() {
        return len;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        node.next = first;
        if (first == null) {
            first = node;
            last = first;
        }
        else {
            first.pre = node;
            first = node;
        }
        len++;
    }
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        node.pre = last;
        if (last == null) {
            last = node;
            first = last;
        }
        else {
            last.next = node;
            last = node;
        }
        len++;
    }
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (first == null) {
            last = null;
        }
        else {
            first.pre = null;
        }
        len--;
        return item;
    }
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        last = last.pre;
        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        len--;
        return item;
    }
    // return an iterator over items in order from front to end
    @Override
    public Iterator<Item> iterator() {
        class DequeIterator implements Iterator<Item> {
            private Node node = first;
            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item item = node.item;
                node = node.next;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        }
        return new DequeIterator();
    }
    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>(); 
        while (!StdIn.isEmpty()) { 
            String item = StdIn.readString(); 
            if (!item.equals("-")) 
                deque.addLast(item); 
            else if (!deque.isEmpty())
                deque.removeFirst(); 
            for (String str : deque) 
                StdOut.print(str + " "); 
            StdOut.println("\t" + deque.isEmpty());
        }
    }
}
