package main.java.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DynamicHashSet<T> {
    private record Entry<E>(E value, Entry next){}
    private Entry<T>[] entries;
    private int size;

    @SuppressWarnings("unchecked")
    public DynamicHashSet() {
        entries =  (Entry<T>[]) new Entry<?>[8] ;

    }

    private void grow() {
        entries = Arrays.copyOf(entries, entries.length * 2 );
    }

    public void add(T value) {
        if(size > entries.length / 2) {
           grow();
        }
        var curr = entries[hash(value, entries.length)];
        while (curr != null) {
            if(curr.value.equals(value)) {
                return;
            }
         curr = curr.next;
        }

        entries[hash(value, entries.length)] = new Entry(value, entries[hash(value, entries.length)]);
        size++;

    }

    public void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        for (Entry<T> e : entries) {
            Entry<T> curr = e;
            while (curr != null) {
                consumer.accept(curr.value);
                curr = curr.next;
            }
        }
    }

    public boolean contains(Object obj) {
        Objects.requireNonNull(obj);
        return entries[hash(obj, entries.length)] != null;
    }

    public void addAll(Collection<? extends T> values)  {
        for (var value: values) {
            add(value);
        }
    }

    public int size() {
        return size;
    }

    public int hash(Object key,int tabLenth) {
        return key.hashCode() & (tabLenth-1);
    }

    public static void main(String[] args) {
        record Cage<T>(T value){}
        class Animal {}
        class Cat extends Animal {
            public void meow() {
                System.out.println("meow !");
            }
        }
        class Dog extends Animal {
            public void bark() {
                System.out.println("bark !");
            }
        }

        var toto = new Cage<?>[2];
        toto[0] = new Cage<>(new Cat());
        var titi = (Cage<Dog>[]) toto;
        titi[0].value().bark();



    }
}
