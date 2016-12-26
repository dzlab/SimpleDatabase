package dz.lab.simpledb.internal;

import dz.lab.simpledb.Transaction;

import java.util.Map;

/**
 * An implementation of {@link Transaction} that represents the memory block with an array of bytes.
 * Created by dzlab on 25/12/2016.
 */
public class BytesTransaction implements Transaction {

    private static class Node {
        String key;
        short offset;
        Node left;
        Node right;

        public Node(String key, short offset) {
            this.key = key;
            this.offset = offset;
            this.left = null;
            this.right = null;
        }

        /**
         * Add the key-offset pair to this index or update the index if the key was already added.
         * @param key the key
         * @param offset the key's offset
         */
        public void add(String key, short offset) {
            int diff = this.key.compareTo(key);
            if(diff==0) {
                this.offset = offset;
            } else if(diff>0) {
                if(this.left == null) {
                    this.left = new Node(key, offset);
                }else {
                    this.left.add(key, offset);
                }
            }else {
                if(this.right == null) {
                    this.right = new Node(key, offset);
                }else {
                    this.right.add(key, offset);
                }
            }
        }

        /**
         * Find the offset of the given key.
         * @param key the key to lookout for.
         * @return the key offset or <code>-1</code> if the key doesn't exists.
         */
        public short find(String key) {
            int diff = this.key.compareTo(key);
            if(diff==0) return offset;
            if(left != null && diff > 0) return left.find(key);
            if(right != null && diff < 0) return right.find(key);
            return -1;
        }

        public void remove(String key) {

        }
    }

    public static final int DEFAULT_CAPACITY = 1024; //Integer.MAX_VALUE

    /**
     * An array of bytes that hold key-value pairs.
     * It contains a sequence of records: crc (4 bytes), tstamp (2 bytes), ksz (2 bytes), value_sz (2 bytes), key (variable), value (variable).
     */
    private byte[] memory;
    /**
     * the position for writing next record.
     */
    private int cursor;
    /**
     * An array of bytes that hold value-occurrences pairs.
     */
    private byte[] index;
    /**
     * A btree of sparse key-memory block (address of memory containing keys greater or equal current one) for a fast search.
     */
    private Node root;

    public BytesTransaction() {
        this.memory = new byte[DEFAULT_CAPACITY];
        this.cursor = 0;
        this.index = new byte[DEFAULT_CAPACITY];
    }

    public Object get(String key) {
        return null;
    }

    public void set(String key, Object value) {
        byte[] kb = key.getBytes();
        Short ksz = (short) kb.length;
        ksz.byteValue();
        byte[] vb = null; // serialize the value
        short vsz = (short) vb.length;
        //System.arraycopy(kb, 0, memory, cursor);
        cursor += /*4 + 2 +*/ 2 + 2 + ksz + vsz;
    }

    public void unset(String key) {

    }

    public int numequalto(Object value) {
        return 0;
    }

    public Transaction begin() {
        return null;
    }

    public Transaction rollback() {
        return null;
    }

    public Transaction commit() {
        return null;
    }
}
