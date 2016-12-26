package dz.lab.simpledb;

import dz.lab.simpledb.internal.MapTransaction;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by dzlab on 24/12/2016.
 */
public class TransactionTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TransactionTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TransactionTest.class);
    }

    private Transaction[] scopes = new Transaction[] {new MapTransaction()};

    /**
     * Test Get() in Transaction
     */
    public void testGetInTransaction() {
        for(Transaction scope: scopes) {
            scope.set("a", 50);
            Transaction s1 = scope.begin();
            assertEquals("a value should be 50", 50, s1.get("a"));
            s1.set("a", 60);
            Transaction s2 = s1.begin();
            s2.unset("a");
            assertEquals("a value should be null", null, s2.get("a"));
        }
    }

    /**
     * Test Set()
     */
    public void testSet() {
        for(Transaction scope: scopes) {
            scope.set("a", 50);
            scope.unset("a");
            scope.set("a", 10);
            assertEquals(10, scope.get("a"));
            assertEquals(0, scope.numequalto(50));
            assertEquals(1, scope.numequalto(10));
        }
    }

    /**
     * Test numequalto()
     */
    public void testNumEqualTo() {
        for(Transaction scope: scopes) {
            scope.set("a", 10);
            scope.set("b", 10);
            scope.set("c", 10);
            assertEquals(0, scope.numequalto(20));
            scope.unset("c");
            assertEquals(2, scope.numequalto(10));
            scope.set("b", 30);
            assertEquals(1, scope.numequalto(10));
        }
    }


    /**
     * Test numequalto() in Transaction
     */
    public void testNumEqualToInTransation() {
        for (Transaction scope : scopes) {
            scope.set("a", 10);
            Transaction s1 = scope.begin();
            assertEquals("Number of occurrences of 10 should be 1", 1, s1.numequalto(10));
        }
    }
}
