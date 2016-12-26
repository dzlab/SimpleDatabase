package dz.lab.simpledb;

/**
 * Created by dzlab on 24/12/2016.
 */
public interface Transaction {
    /**
     * Get the value of a key
     * @param key
     * @return
     */
    Object get(String key);

    /**
     *
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     *
     * @param key
     */
    void unset(String key);

    /**
     *
     * @param value
     * @return
     */
    int numequalto(Object value);

    /**
     * Begin a new {@link Transaction} from the current one.
     * @return the newly created {@link Transaction}.
     */
    Transaction begin();

    /**
     * Rollback all changes made in the current {@link Transaction}.
     * @return the previous {@link Transaction} from which the current one was created.
     */
    Transaction rollback();

    /**
     * Commit all changes made in the current {@link Transaction}.
     * @return the previous {@link Transaction} from which the current one was created.
     */
    Transaction commit();
}
