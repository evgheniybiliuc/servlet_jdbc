package jdbc.servlet.todo.pool;


public interface ExpirePool<T> extends SimplePool<T> {
    boolean isExpired(T obj);

    long getExpirationTime();

    int busyObjectSize();

    int freeObjectSize();

}
