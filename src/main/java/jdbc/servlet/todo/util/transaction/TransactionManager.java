package jdbc.servlet.todo.util.transaction;

import java.sql.Connection;

public interface TransactionManager {


    <V> V doInTransaction(TransactionExecutor<V> executor) throws Exception;

    interface TransactionExecutor<T> {
        T execute(Connection connection);
    }
}

