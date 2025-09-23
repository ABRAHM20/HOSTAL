package pe.edu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;
    private Connection cnx;

    private final String url = "jdbc:mysql://localhost:3306/hostal_db";
    private final String usuario = "root";
    private final String clave = "04022004";

    private Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(url, usuario, clave);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Conexion getInstancia() {
        try {
            if (instancia == null || instancia.getConexion() == null || instancia.getConexion().isClosed()) {
                instancia = new Conexion();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instancia;
    }

    public Connection getConexion() {
        return cnx;
    }

    public int pruebaConexion() {
        try {
            return (cnx != null && !cnx.isClosed()) ? 1 : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
