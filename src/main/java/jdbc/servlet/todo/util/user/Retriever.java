package jdbc.servlet.todo.util.user;

import java.util.Optional;

public interface Retriever<V, T> {
    Optional<V> retrieveFrom(T t);
}
