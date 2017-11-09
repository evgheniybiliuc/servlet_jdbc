package jdbc.servlet.todo.pool;

public interface SimplePool<T> {

    T get();

    int size();

    void returnBack(T obj);
}

