import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int len;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        len = 0;
    }
    private void resize(int c) {
        Item[] temp = (Item[]) new Object[c];
        for (int i = 0; i < len; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }
    // is the randomized queue empty?
    public boolean isEmpty() {
        return len == 0;
    }
    // return the number of items on the randomized queue
    public int size() {
        return len;
    }   
    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (len == queue.length) {
            resize(2*len);
        }
        queue[len++] = item;
    }   
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(len);
        Item item = queue[index];
        queue[index] = queue[len-1];
        queue[len-1] = null;
        len--;
        if (len > 0 && len == queue.length/4)
            resize(queue.length/2);
        return item;
    }   
    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(len);
        Item item = queue[index];

        return item;
    }                     
    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        class RandomizedQueueIterator implements Iterator<Item> {
            private int index = 0;
            private final int[] radomIndex = StdRandom.permutation(len);
            @Override
            public boolean hasNext() {
                return index < len;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item item = queue[radomIndex[index++]];
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        }
        return new RandomizedQueueIterator();
    }         
    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<String>(); 
        while (!StdIn.isEmpty()) { 
            String item = StdIn.readString(); 
            if (!item.equals("-")) 
                deque.enqueue(item); 
            else if (!deque.isEmpty())
                deque.dequeue(); 
            for (String str : deque) 
                StdOut.print(str + " "); 
            StdOut.println("\t" + deque.isEmpty());
        }
    } 

}
