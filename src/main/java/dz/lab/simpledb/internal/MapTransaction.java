package dz.lab.simpledb.internal;

import dz.lab.simpledb.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of {@link Transaction} that represents the memory block with a simple {@link Map}.
 * Created by dzlab on 24/12/2016.
 */
public class MapTransaction implements Transaction {

    /**
     * Parent transaction
     */
    private final Transaction parent;
    /**
     * Memory bloc managed by this transaction;
     */
    private Map<String, Object> memory;
    /**
     * Set of unset keys.
     */
    private Set<String> unsets;
    /**
     * Inverted index: values to their occurrences count.
     */
    private Map<Object, Integer> index;
    /**
     * A flag that indicates whether changes to the memory block managed by the current {@link Transaction} is possible or not.
     */
    private boolean frozen;

    public MapTransaction() {
        this(null); // default transaction
    }

    public MapTransaction(Transaction parent) {
        this.parent = parent;
        this.memory = new HashMap<String, Object>();
        this.unsets = new HashSet<String>();
        this.index = new HashMap<Object, Integer>();
        this.frozen = false;
    }

    public Object get(String key) {
        if(unsets.contains(key)) {
            return null;
        }
        Object value = memory.get(key);
        if(value==null && parent!=null) {
            return parent.get(key);
        }
        return value;
    }

    public void set(String key, Object value) {
        Object oldVal = memory.get(key);
        if(oldVal!=null && oldVal!=value) {
            decrement(oldVal);
        }
        memory.put(key, value);
        increment(value);
        if(unsets.contains(key)) {
            unsets.remove(key);
        }
    }

    public void unset(String key) {
        if(unsets.contains(key)) return;
        Object value = memory.get(key);
        if(value==null && parent!=null) {
            value = parent.get(key);
        }
        if(value==null) return;
        decrement(value);
        memory.remove(key);
        unsets.add(key);
    }

    public int numequalto(Object value) {
        Integer count = index.get(value);
        if(count==null && parent!=null) {
            count = parent.numequalto(value);
        }
        return (count!=null? count:0);
    }

    public Transaction begin() {
        // if any changes were made then create a new transaction
        return new MapTransaction(this);
    }

    public Transaction rollback() {
        return parent;
    }

    public Transaction commit() {
        if(parent!=null && parent instanceof MapTransaction) {
            ((MapTransaction) parent).updateMemory(memory, unsets, index);
        }
        return parent;
    }

    private void updateMemory(Map<String, Object> memory, Set<String> unsets, Map<Object, Integer> index) {
        assert memory!=null;
        assert unsets!=null;
        assert index!=null;
        this.memory = memory;
        this.unsets = unsets;
        this.index = index;
    }

    private void increment(Object value) {
        int count = 1;
        if(index.containsKey(value)) {
            count = index.get(value) + 1;
        }
        index.put(value, count);
    }
    private void decrement(Object value) {
        int count = 0;
        if(index.containsKey(value)) {
            count = index.get(value) - 1;
        }
        index.put(value, count);
    }
}
