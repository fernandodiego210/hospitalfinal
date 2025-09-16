package hospitalD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase para manejar la conexión a SQL Server
public class ConexionBD {
    private static final String SERVIDOR = "LAPTOP-QCFS02UI\\SQLExpress";
    private static final String PUERTO = "1433";
    private static final String BASE_DATOS = "HospitalDB";
    private static final String USUARIO = "java_user"; // Cambiar por tu usuario
    private static final String PASSWORD = "NuevaPassword123"; // Cambiar por tu contraseña
    
    private static final String URL = "jdbc:sqlserver://" + SERVIDOR + ":" + PUERTO + 
                                     ";databaseName=" + BASE_DATOS + 
                                     ";encrypt=false;trustServerCertificate=true";
    
    private static Connection conexion;
    
    // Método para obtener la conexión (Singleton)
    public static Connection getConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de SQL Server
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión exitosa a SQL Server");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de SQL Server no encontrado");
            System.err.println("Asegúrate de tener mssql-jdbc.jar en el classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            System.err.println("URL: " + URL);
            System.err.println("Usuario: " + USUARIO);
            e.printStackTrace();
        }
        return conexion;
    }
    
    // Método para cerrar la conexión
    public static void closeConnection() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada exitosamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión:");
            e.printStackTrace();
        }
    }
    
    // Método para probar la conexión
    public static boolean probarConexion() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}