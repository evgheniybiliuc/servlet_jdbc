package jdbc.servlet.todo.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private final static String DATASOURCE_CONTEXT = "java:comp/env/jdbc/WebDatasource";


    public static Connection getConnection() {


        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(DATASOURCE_CONTEXT);
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

}
