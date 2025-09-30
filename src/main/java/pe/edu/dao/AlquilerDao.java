/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.entity.Alquiler;
import pe.edu.entity.Cliente;
import pe.edu.entity.Habitacion;
import pe.edu.util.Conexion;

/**
 *
 * @author LENOVO
 */
public class AlquilerDao implements DaoCrud<Alquiler> {

    @Override
    public void insertar(Alquiler a) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_alquiler_insertar(?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, a.getCliente().getId_cliente());
            cs.setInt(2, a.getHabitacion().getId_hab());
            cs.setDate(3,(Date)a.getFecha_inicio());
            cs.setDate(4,(Date)a.getFecha_fin());
            cs.setInt(5, a.getHoras());
            cs.setString(6,a.getEstado());
            cs.setInt(7,a.getId_user());

            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en insertarSP Alquiler: " + e.getMessage());
        }
    }

    public void activar(int id_alq) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_activar_alquiler(?)}");
            cs.setInt(1, id_alq);
            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en activar alquiler: " + e.getMessage());
        }
    }
   //modificar por aca
    public void finalizar(int id_alq) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_finalizar_alquiler(?)}");
            cs.setInt(1, id_alq);
            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en finalizar alquiler: " + e.getMessage());
        }
    }

    public Alquiler obtenerDetalle(int id) {
        Alquiler a = null;

        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_alquiler_detalle(?)}");
            cs.setInt(1, id);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                a.setHoras(rs.getInt("horas"));
                a.setTotal(rs.getDouble("total"));
                a.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
                a.setFecha_fin(rs.getTimestamp("fecha_fin"));
                a.setEstado(rs.getString("estado"));

                Cliente c = new Cliente();
                c.setNombre(rs.getString("nombre_cliente"));
                a.setCliente(c);

                Habitacion h = new Habitacion();
                h.setNum_hab(rs.getInt("num_hab"));
                h.setTipo_nombre(rs.getString("tipo_nombre")); 
                a.setHabitacion(h);
            }

            rs.close();
            cs.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error en obtenerDetalle alquiler: " + e.getMessage());
        }

        return a;
    }

    @Override
    public Alquiler leer(int id) {
       
        return null;
    }

    @Override
    public void editar(Alquiler obj) {
        
    }

    @Override
    public void eliminar(int id) {
       
    }

    @Override
public LinkedList<Alquiler> listar() {
    LinkedList<Alquiler> lista = new LinkedList<>();
    
    // Usamos try-with-resources para asegurar que la conexión, 
    // el statement y el resultset se cierren automáticamente.
    try (Connection cnx = Conexion.getInstancia().getConexion();
         CallableStatement cs = cnx.prepareCall("{call sp_alquiler_listar()}");
         ResultSet rs = cs.executeQuery()) {

        // El bucle ahora es seguro y eficiente
        while (rs.next()) {
            // 1. Crear el objeto principal de Alquiler
            Alquiler a = new Alquiler();
            a.setId_alq(rs.getInt("id_alq"));
            a.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
            a.setFecha_fin(rs.getTimestamp("fecha_fin"));
            a.setHoras(rs.getInt("horas"));
            a.setEstado(rs.getString("estado"));

            // 2. Crear y llenar el objeto Cliente embebido (con el nombre del JOIN)
            Cliente c = new Cliente();
            c.setNombre(rs.getString("nombre_cliente"));
            a.setCliente(c);

            // 3. Crear y llenar el objeto Habitacion embebido (con el número del JOIN)
            Habitacion h = new Habitacion();
            h.setNum_hab(rs.getInt("numero_habitacion"));
            a.setHabitacion(h);

            // 4. Añadir el objeto Alquiler (ya completo) a la lista
            lista.add(a);
        }
    } catch (SQLException e) {
        System.out.println("Error en listarSP alquiler:");
        // Esto imprimirá el error completo en los logs de tu servidor
        e.printStackTrace(); 
    }
    
    return lista;
}
    
public int RetornarNumeroHab(int id) {
    // Variable para guardar el resultado, con un valor por defecto en caso de no encontrar nada.
    int numeroHabitacion = 0; 

    // El bloque try-with-resources se encarga de cerrar todo automáticamente.
    try (Connection cnx = Conexion.getInstancia().getConexion();
         CallableStatement cs = cnx.prepareCall("{call sp_numhab(?)}")) {

        cs.setInt(1, id);

        // El ResultSet también debe estar dentro del try-with-resources
        try (ResultSet rs = cs.executeQuery()) {
            
            // Verificamos si la consulta devolvió al menos una fila
            if (rs.next()) {
                // Leemos el valor de la primera columna del resultado
                numeroHabitacion = rs.getInt(1); 
            }
        }
    } catch (SQLException e) {
        // Manejamos cualquier posible error de SQL
        System.out.println("Error al obtener el número de habitación: " + e.getMessage());
        e.printStackTrace();
    }
    
    // Devolvemos el número encontrado o el valor por defecto (0 o -1)
    return numeroHabitacion;
}
    

    public LinkedList<Alquiler> listarPorEstado(String estado) {
        LinkedList<Alquiler> lista = new LinkedList<>();

        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_alquiler_listar_por_estado(?)}");
            cs.setString(1, estado);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                a.setHoras(rs.getInt("horas"));
                a.setTotal(rs.getDouble("total"));
                a.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
                a.setFecha_fin(rs.getTimestamp("fecha_fin"));
                a.setEstado(rs.getString("estado"));

                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                a.setCliente(c);

                Habitacion h = new Habitacion();
                h.setNum_hab(rs.getInt("num_hab"));
                a.setHabitacion(h);

                lista.add(a);
            }

            rs.close();
            cs.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error en listarPorEstado: " + e.getMessage());
        }

        return lista;
    }

}
