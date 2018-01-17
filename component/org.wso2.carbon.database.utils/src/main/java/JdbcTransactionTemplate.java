public interface JdbcTransactionTemplate {
    public void beginTransaction() throws TransactionException;
    public void commitTransaction() throws TransactionException;
    public void rollbackTransaction() throws TransactionException;
}
