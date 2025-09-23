package pe.edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.entity.Usuario;
import pe.edu.util.Conexion;

public class UsuarioDao implements DaoCrud<Usuario> {

    @Override
    public LinkedList<Usuario> listar() {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            LinkedList<Usuario> lista = new LinkedList<>();
            String query = "Select * from usuario;";
            Statement sentencia = cnx.createStatement();
            ResultSet resultado = sentencia.executeQuery(query);

            while (resultado.next()) {
                Usuario u = new Usuario();
                u.setId(resultado.getInt("id"));
                u.setPassword(resultado.getString("password"));
                u.setNombre(resultado.getString("nombre"));
                u.setRol(resultado.getString("rol"));
                lista.add(u);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void insertar(Usuario obj) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            String query = "insert into usuario values(?,?,?,?)";
            PreparedStatement sentencia = cnx.prepareStatement(query);
            sentencia.setInt(1, obj.getId());
            sentencia.setString(2, obj.getPassword());
            sentencia.setString(3, obj.getNombre());
            sentencia.setString(4, obj.getRol());
            sentencia.executeUpdate();
            sentencia.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Usuario leer(int id) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            Usuario u = new Usuario();
            String query = "Select * from usuario ";
            query += " where id='" + id + "'";
            Statement sentencia = cnx.createStatement();
            ResultSet resultado = sentencia.executeQuery(query);

            resultado.next();
            u.setId(resultado.getInt("id"));
            u.setPassword(resultado.getString("password"));
            u.setNombre(resultado.getString("nombre"));

            return u;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void editar(Usuario obj) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            String query = "update usuario set password=?, nombre=?, where id=?";
            PreparedStatement sentencia = cnx.prepareStatement(query);
            sentencia.setString(1, obj.getPassword());
            sentencia.setString(2, obj.getNombre());
            sentencia.setInt(3, obj.getId());
            sentencia.executeUpdate();
            sentencia.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            String query = "delete from usuario where id=?";
            PreparedStatement sentencia = cnx.prepareStatement(query);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
            sentencia.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*public Usuario getLogueado(Usuario u) throws ClassNotFoundException {
        Usuario user = null;

        try {
            Conexion c = new Conexion();
            Connection cnx = c.conecta();

            String query = "SELECT * FROM usuario WHERE id = ? AND password = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, u.getId());
            ps.setString(2, u.getPassword());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Usuario();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre")); // Asegúrate de tener este campo
                user.setPassword(rs.getString("password"));
                user.setRol(rs.getString("rol")); // Este campo es importante
            }

            rs.close();
            ps.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error en getLogueado: " + e.getMessage());
        }

        return user; // Devuelve null si no encontró
    }*/
    public Usuario getLogueado(Usuario u) throws ClassNotFoundException {
        Usuario user = null;

        try {
            Connection cnx = Conexion.getInstancia().getConexion();

            // Usamos email en lugar de ID
            String query = "SELECT * FROM usuario WHERE email = ? AND password = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Usuario();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setEmail(rs.getString("email")); 
                user.setPassword(rs.getString("password"));
                user.setRol(rs.getString("rol"));
            }

            rs.close();
            ps.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error en getLogueado: " + e.getMessage());
        }

        return user;
    }
    
    
    // Puedes añadir este nuevo método en cualquier parte dentro de tu clase UsuarioDao
public Usuario buscarPorEmail(String email) {
    Usuario user = null;
    // La consulta SQL ahora solo busca por email
    String query = "SELECT * FROM usuario WHERE email = ?";

    // Usaremos try-with-resources para que la conexión y el PreparedStatement se cierren automáticamente
    try (Connection cnx = Conexion.getInstancia().getConexion();
         PreparedStatement ps = cnx.prepareStatement(query)) {

        ps.setString(1, email); // Asignamos el email al parámetro de la consulta

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                user = new Usuario();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setEmail(rs.getString("email"));
                
                // MUY IMPORTANTE: Aquí estamos obteniendo el HASH de la base de datos.
                // Tu clase Usuario usará el campo "password" para almacenar este hash.
                user.setPassword(rs.getString("password")); 
                
                user.setRol(rs.getString("rol"));
            }
        }
    } catch (SQLException e) {
        // Es una buena práctica registrar el error en lugar de solo imprimirlo
        Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, "Error al buscar usuario por email", e);
    }

    return user; // Devuelve el objeto usuario si se encontró, o null si no existe o hubo un error
}

}
