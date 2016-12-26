package dz.lab.simpledb.internal;

import dz.lab.simpledb.Transaction;

/**
 * Wrap an instance of {@link Transaction} and report the time it took to execute each of the Transaction operations.
 * Created by dzlab on 25/12/2016.
 */
public class TransactionWrapper implements Transaction {

    private final Transaction scope;

    public TransactionWrapper(Transaction scope) {
        this.scope = scope;
    }

    public Object get(String key) {
        long before = System.nanoTime();
        Object value = scope.get(key);
        long after = System.nanoTime();
        long diff = after - before;
        return value;
    }

    public void set(String key, Object value) {
        long before = System.nanoTime();
        scope.set(key, value);
        long after = System.nanoTime();
        long diff = after - before;
    }

    public void unset(String key) {
        long before = System.nanoTime();
        scope.unset(key);
        long after = System.nanoTime();
        long diff = after - before;
    }

    public int numequalto(Object value) {
        long before = System.nanoTime();
        int result = scope.numequalto(value);
        long after = System.nanoTime();
        long diff = after - before;
        return result;
    }

    public Transaction begin() {
        long before = System.nanoTime();
        Transaction newscope = scope.begin();
        long after = System.nanoTime();
        long diff = after - before;
        return new TransactionWrapper(newscope);
    }

    public Transaction rollback() {
        long before = System.nanoTime();
        Transaction newscope = scope.rollback();
        long after = System.nanoTime();
        long diff = after - before;
        return new TransactionWrapper(newscope);
    }

    public Transaction commit() {
        long before = System.nanoTime();
        Transaction newscope = scope.commit();
        long after = System.nanoTime();
        long diff = after - before;
        return new TransactionWrapper(newscope);
    }
}
