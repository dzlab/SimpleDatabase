package dz.lab.simpledb;

import dz.lab.simpledb.internal.MapTransaction;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Simple Database!
 */
public class App {
    /**
     *
     */
    public enum Command {
        // Data commands
        GET,   // GET name – Print out the value of the variable name, or NULL if that variable is not set.
        SET,   // SET name value – Set the variable name to the value value. Neither variable names nor values will contain spaces.
        UNSET, // UNSET name – Unset the variable name, making it just like that variable was never set.
        NUMEQUALTO, //NUMEQUALTO value – Print out the number of variables that are currently set to value. If no variables equal that value, print 0.
        END,    // END – Exit the program
        // Transaction commands
        BEGIN,
        ROLLBACK,
        COMMIT,
    }

    private volatile boolean stop = false;
    private Transaction scope = null;

    public App() {
        scope = new MapTransaction();
    }

    /**
     * Run app and process commands infinitely until an END command is received.
     * @param in the source of commands
     */
    public void run(Scanner in, PrintStream out) {
        while (!stop) {
            Object result = parse(in.nextLine());
            if(result != null) {
                out.println(result);
            }
        }
    }

    /**
     * Check whether the app is running.
     * @return <code>true</code> if accepting commands, <code>false</code> otherwise.
     */
    public boolean isRunning() {
        return !stop;
    }

    /**
     * Parse a command, execute it and return the corresponding result.
     * @param commandString the command string.
     * @return the result of executing the command.
     */
    public Object parse(String commandString) {
        String[] command = commandString.split(" ");
        Command cmd = Command.valueOf(command[0]);
        Transaction newscope = null;
        switch (cmd) {
            case GET:
                Object value = scope.get(command[1]);
                return (value!=null? value:"NULL");
            case SET:
                scope.set(command[1], command[2]);
                break;
            case UNSET:
                scope.unset(command[1]);
                break;
            case NUMEQUALTO:
                return scope.numequalto(command[1]);
            case END:
                stop = true;
                break;
            case BEGIN:
                scope = scope.begin();
                break;
            case ROLLBACK:
                newscope = scope.rollback();
                if(newscope==null) return "NO TRANSACTION";
                scope = newscope;
                break;
            case COMMIT:
                newscope = scope.commit();
                if(newscope==null) return "NO TRANSACTION";
                scope = newscope;
                break;
            default:
                System.out.println("Unknown command " + cmd);
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PrintStream output = System.out;
        App app = new App();
        app.run(input, output);
    }
}
