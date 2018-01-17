import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLException;

public class TestClass {
    private static BasicDataSource dataSource;

    public static void main(String[] args) {
        initDataSource();
        try {
            JdbcTransaction jdbcTransaction=new JdbcTransaction(dataSource.getConnection());
            jdbcTransaction.beginTransaction();
            jdbcTransaction.commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }

    private static void initDataSource(){
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("userName");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:h2:mem:testDB");
    }
}
