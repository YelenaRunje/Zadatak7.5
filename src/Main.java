import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public static void main(String[] args) {
    DataSource dataSource = createDataSource();

    try (Connection connection = dataSource.getConnection()) {
        connection.setAutoCommit(false);
        System.out.println("Uspješno spojeni na bazu podataka");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu + 10 WHERE IDStavka = 8");
            stmt.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu - 10 WHERE IDStavka = 9");
            connection.commit();
            System.out.println("Transakcija uspješno izvršena.");

        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Greška u transakciji. Promjene poništene.");
            e.printStackTrace();
        }

    } catch (SQLException e) {
        System.err.println("Greška pri spajanju na bazu:");
        e.printStackTrace();
    }
}

private static DataSource createDataSource() {
    SQLServerDataSource ds = new SQLServerDataSource();
    ds.setServerName("localhost");
    ds.setDatabaseName("AdventureWorksOBP");
    ds.setUser("sa");
    ds.setPassword("SQL");
    ds.setEncrypt(false);
    return ds;
}

//SQL:
/*


BEGIN TRY
    BEGIN TRANSACTION;
    UPDATE Stavka  SET CijenaPoKomadu = CijenaPoKomadu + 10 WHERE IDStavka = 8;
    UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu - 10 WHERE IDStavka = 9;
    COMMIT;
    PRINT 'Transakcija uspješna';

END TRY
BEGIN CATCH
    ROLLBACK;
    PRINT 'Greška u transakciji';
END CATCH;

*/