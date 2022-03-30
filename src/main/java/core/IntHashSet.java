package main.java.core;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class IntHashSet {
    private record Entry(int value, Entry next){}
    private Entry[] entries = new Entry[100];
    private int size;

    public void add(int value) {
        if(size < entries.length) {
            var current =  entries[hash(value)];
            while (current != null) {
                if(current.value == value) {
                    return;
                }
                current = current.next;
            }
            entries[hash(value)] = new Entry(value, entries[hash(value)]);
            size++;
        }
    }

    public void forEach(IntConsumer consumer) {
        Objects.requireNonNull(consumer);
        for (var e: entries) {
            var curr = e;
            while (curr != null) {
                consumer.accept(curr.value);
                curr = curr.next;
            }
        }
    }

   public boolean contains(int value) {
       for (var e : entries) {
           var curr = e;
           while (curr != null) {
               if(curr.value == value) {
                   return true;
               }
               curr = curr.next;
           }
       }
       return false;
    }

    public int hash(int value) {
        return value & (this.entries.length-1);
    }

    public int size() {
        return size;
    }

}
