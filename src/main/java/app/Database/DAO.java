/*
Referencia: Matias Luca Soto //todo:AGREGAR REFERENCIA?
*/
package app.Database;

import java.sql.*;

public  class DAO {

    protected Connection conexion = null;
    protected Statement sentencia = null;
    protected ResultSet resultado = null;

    private final String URL = "jdbc:sqlserver://localhost:1433;database=BDINTERCAMBIOS; encrypt = false;";
    //private final String URL = "jdbc:sqlserver://localhost:1433;database=BDINTERCAMBIOS; encrypt = false;";
    private final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String USER = "AdmIntercambio";
    private final String PASSWORD = "12345";

    // MÉTODOS

    /**
     * Método que conecta el Driver JDBC y luego establece una conexión con el objeto Connection usando la URL configurada para SQL Server.
     *
     * @throws Exception
     */
    public void conectarBase() throws Exception {
        try {
            Class.forName(DRIVER); // Conecta al driver SQL Server
            conexion = DriverManager.getConnection(URL, USER, PASSWORD); // Establece la conexión
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        }
    }
    /**
     * Método que verifica si mis Objetos de Conexión, Sentencia y Resultado tienen valores asignados. Si es así, se los devuelve a un estado 'null'
     * @throws Exception
     */
    protected void desconectarBase() throws Exception {
        try {
            if (conexion != null) {
                conexion.close();
            }
            if (sentencia != null) {
                sentencia.close();
            }
            if (resultado != null) {
                resultado.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Método que se conecta a la base, prepara el Objeto Statement y ejecuta con un método la query pasada como argumento
     * @param sql Sentencia query nativa de tipo INSERT, UPDATE, DELETE que no devuelve ninguna tabla
     * @throws Exception
     */
    protected void insertarModificarEliminar(String sql) throws Exception {
        try {
            conectarBase();
            sentencia = conexion.createStatement();
            sentencia.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            conexion.rollback();
            throw e;
        } finally {
            desconectarBase();
        }
    }

    /**
     * Método que se conecta a la base, prepara el Objeto Statement y luego guarda en el Objeto ResultSet las tablas devueltas al ejectuar la sentencia query que fue pasada como argumento
     * @param sql Sentencia query nativa de tipo consulta SELECT que devuelva una tabla
     * @throws Exception
     */
    protected void consultarBase(String sql) throws Exception {
        try {
            conectarBase();
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
        } catch (Exception e) {
            throw e;
        }
    }

}
