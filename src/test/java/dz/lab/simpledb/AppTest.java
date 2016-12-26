package dz.lab.simpledb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.util.Scanner;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Test running app with some basic data
     */
    public void testRun() {
        // prepare input
        String[] bulks = new String[] {
                "SET ex 10"+"\n"+"GET ex"+"\n"+"UNSET ex"+"\n"+"GET ex"+"\n"+"END"+"\n",
                "SET a 10"+"\n"+"SET b 10"+"\n"+"NUMEQUALTO 10"+"\n"+"NUMEQUALTO 20"+"\n"+"SET b 30"+"\n"+"NUMEQUALTO 10"+"\n"+"END"+"\n",
                "BEGIN"+"\n"+"SET a 10"+"\n"+"GET a"+"\n"+"BEGIN"+"\n"+"SET a 20"+"\n"+"GET a"+"\n"+"ROLLBACK"+"\n"+"GET a"+"\n"+"ROLLBACK"+"\n"+"GET a"+"\n"+"END"+"\n",
                /*"BEGIN"+"\n"+*/"SET a 30"+"\n"+"BEGIN"+"\n"+"SET a 40"+"\n"+"COMMIT"+"\n"+"GET a"+"\n"+"ROLLBACK"+"\n"+"COMMIT"+"\n"+"END"+"\n",
                "SET a 50"+"\n"+"BEGIN"+"\n"+"GET a"+"\n"+"SET a 60"+"\n"+"BEGIN"+"\n"+"UNSET a"+"\n"+"GET a"+"\n"+"ROLLBACK"+"\n"+"GET a"+"\n"+"COMMIT"+"\n"+"GET a"+"\n"+"END"+"\n",
                "SET a 10"+"\n"+"BEGIN"+"\n"+"NUMEQUALTO 10"+"\n"+"BEGIN"+"\n"+"UNSET a"+"\n"+"NUMEQUALTO 10"+"\n"+"ROLLBACK"+"\n"+"NUMEQUALTO 10"+"\n"+"COMMIT"+"\n"+"END"+"\n",
        };
        String[] results = new String[] {
                "10"+"\n"+"NULL"+"\n",
                "2"+"\n"+"0"+"\n"+"1"+"\n",
                "10"+"\n"+"20"+"\n"+"10"+"\n"+"NULL"+"\n",
                "40"+"\n"+"NO TRANSACTION"+"\n"+"NO TRANSACTION"+"\n",
                "50"+"\n"+"NULL"+"\n"+"60"+"\n"+"60"+"\n",
                "1"+"\n"+"0"+"\n"+"1"+"\n",
        };
        // process each element of the bulk query
        for(int i=0; i<bulks.length; i++) {
            // prepare input
            String commands = bulks[i];
            Scanner in = new Scanner(commands);
            // prepare output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(baos, true);
            // run app
            App app = new App();
            app.run(in, out);
            assertEquals("Output should match expected", results[i], baos.toString());
        }
    }

    /**
     * Test parsing and executing commands.
     */
    public void testParse() {
        App app = new App();

        // set a variable
        Object setResult = app.parse("SET name value");
        assertNull("No result is expected from a SET command", setResult);

        // check the variable's value after set
        String getResult = (String) app.parse("GET name");
        assertEquals("GET command should return the value of the variable", "value", getResult);

        // unset a variable
        Object unsetResult = app.parse("UNSET name");
        assertNull("No result is expected from an UNSET command", unsetResult);

        // check the variable's value after unset
        getResult = (String) app.parse("GET name");
        assertEquals("GET command should return NULL for a non existent variable", "NULL", getResult);

        // check number of variables with a given value
        Integer numequaltoResult = (Integer) app.parse("NUMEQUALTO value");
        assertEquals("NUMEQUALTO shoudl return 0 when no variable has given value", Integer.valueOf(0), numequaltoResult);

        // end program
        Object endResult = app.parse("END");
        assertNull("No result is expected from a END command", endResult);
        // check if app is stopped
        assertFalse("App should be stopped after receiving END command", app.isRunning());
    }
}
