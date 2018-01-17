import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements JdbcTransactionTemplate {
    private static final ThreadLocal currentThread = new ThreadLocal();
    private static int threadCount = 0;
    private Connection connection;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void beginTransaction() throws TransactionException {
        if (connection != null) {
            try {
                threadCount = 1;
                connection.setAutoCommit(false);
                ThreadSupport threadSupport = new ThreadSupport(threadCount, connection);
                currentThread.set(threadSupport);
            } catch (SQLException e) {
                throw new TransactionException("Cannot begin the transaction - " + e, e);
            }
        } else {
            throw new TransactionException("Data source is null.");
        }
    }

    @Override
    public void commitTransaction() throws TransactionException {
        ThreadSupport threadSupport = (ThreadSupport) currentThread.get();
        if (threadSupport.getConnection() != null) {
            if (threadSupport.getCount() != 0) {
                try {
                    threadSupport.getConnection().commit();
                    threadCount = 0;
                } catch (SQLException e) {
                    throw new TransactionException("Cannot commit the transaction. - " + e, e);
                } finally {
                    endTransaction(threadSupport.getConnection());
                }
            } else {
                throw new TransactionException("No transaction to commit.");
            }
        } else {
            throw new TransactionException("Connection is null.");
        }
    }

    @Override
    public void rollbackTransaction() throws TransactionException {
        ThreadSupport threadSupport = (ThreadSupport) currentThread.get();
        if (threadSupport.getConnection() != null) {
            if (threadSupport.getCount() != 0) {
                try {
                    threadSupport.getConnection().rollback();
                    threadCount = 0;
                } catch (SQLException e) {
                    throw new TransactionException("Cannot rollback the transaction. - " + e, e);
                } finally {
                    endTransaction(threadSupport.getConnection());
                }
            } else {
                throw new TransactionException("No transaction to rollback.");
            }
        } else {
            throw new TransactionException("Connection is null.");
        }
    }

    private void endTransaction(Connection conn) throws TransactionException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new TransactionException("Cannot close the datasource connection. - " + e, e);
        }
    }
}

class ThreadSupport {
    private int count;
    private Connection connection;

    public ThreadSupport(int count, Connection connection) {
        this.count = count;
        this.connection = connection;
    }

    public int getCount() {
        return this.count;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
