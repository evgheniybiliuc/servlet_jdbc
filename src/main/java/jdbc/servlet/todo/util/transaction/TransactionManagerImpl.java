package jdbc.servlet.todo.util.transaction;

import jdbc.servlet.todo.pool.ExpireGenericPool;
import jdbc.servlet.todo.util.ConnectionManager;
import jdbc.servlet.todo.util.JdbcUtils;

import java.sql.Connection;

public enum TransactionManagerImpl implements TransactionManager {

    INSTANCE;

    private static ExpireGenericPool<Connection> connectionPool;

    static {
        connectionPool = new ExpireGenericPool.Builder<Connection>()
                .setMaxObjectsInPool(20)
                .setExpireTime(3000)
                .setFactory(ConnectionManager::getConnection)
                .build();
    }

    private static void setConnectionPool(ExpireGenericPool<Connection> expireGenericPool) {
        connectionPool = expireGenericPool;
    }

    @Override
    public <V> V doInTransaction(TransactionExecutor<V> executor) {
        Connection connection = connectionPool.get();
        try {
            V result = executor.execute(connection);
            connection.commit();
            return result;
        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(connection);
            throw new RuntimeException(e);
        } finally {
            connectionPool.returnBack(connection);
        }
    }
}
