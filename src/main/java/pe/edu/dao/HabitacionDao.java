/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.entity.Habitacion;
import pe.edu.util.Conexion;

/**
 *
 * @author LENOVO
 */
public class HabitacionDao implements DaoCrud<Habitacion> {

    @Override
    public void insertar(Habitacion obj) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_habitacion_insertar(?, ?, ?, ?)}");

            cs.setInt(1, obj.getNum_hab());
            cs.setString(2, obj.getEstado());
            cs.setInt(3,obj.getId_usuario());
            cs.setInt(4,obj.getId_tipo_hab());
            
            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editar(Habitacion obj) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_habitacion_editar(?, ?, ?, ?)}");

            cs.setInt(1, obj.getId_hab());
            cs.setInt(2, obj.getId_tipo_hab());
            cs.setInt(3, obj.getNum_hab());
            cs.setString(4,obj.getEstado());
            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en editarSP: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id_hab) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_habitacion_eliminar(?)}");

            cs.setInt(1, id_hab);

            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en eliminarSP: " + e.getMessage());
        }
    }

    @Override
    public Habitacion leer(int id_hab) {
        Habitacion h = null;
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_habitacion_leer(?)}");

            cs.setInt(1, id_hab);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                h = new Habitacion();
                h.setId_hab(rs.getInt("id_hab"));
                h.setNum_hab(rs.getInt("num_hab"));
                h.setId_tipo_hab(rs.getInt("id_tipo_hab"));
                h.setTipo_nombre(rs.getString("tipo_nombre")); //de join
                h.setPrecio(rs.getDouble("precio"));//de join
                h.setEstado(rs.getString("estado"));
            }
            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en leerSP: " + e.getMessage());
        }
        return h;
    }

    @Override
    public LinkedList<Habitacion> listar() {
        LinkedList<Habitacion> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_habitacion_listar()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setId_hab(rs.getInt("id_hab"));
                h.setNum_hab(rs.getInt("num_hab"));
                //ss.setId_tipo_hab(rs.getInt("id_tipo_hab"));
                h.setTipo_nombre(rs.getString("tipo_nombre"));
                h.setPrecio(rs.getDouble("precio"));
                h.setEstado(rs.getString("estado"));
                lista.add(h);
            }
            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en listarSP: " + e.getMessage());
        }
        return lista;
    }

    public LinkedList<Habitacion> listarPorEstado(String estado) {
        LinkedList<Habitacion> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_habitacion_listar_por_estado(?)}");
            cs.setString(1, estado);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setId_hab(rs.getInt("id_hab"));
                h.setNum_hab(rs.getInt("num_hab"));
                h.setTipo_nombre(rs.getString("tipo_nombre"));
                h.setPrecio(rs.getDouble("precio"));
                h.setEstado(rs.getString("estado"));
                lista.add(h);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en listarPorEstado: " + e.getMessage());
        }
        return lista;
    }

    public Habitacion obtenerPorId(int id) {
        Habitacion obt = null;
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            String sql = "SELECT * FROM habitacion WHERE id_hab = ?";
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                obt = new Habitacion();
                obt.setId_hab(rs.getInt("id_hab"));
                obt.setId_tipo_hab(rs.getInt("id_tipo_hab"));
                obt.setNum_hab(rs.getInt("num_hab"));
                obt.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
        }
        return obt;
    }

    public LinkedList<Habitacion> listarDisponibles() {
        LinkedList<Habitacion> lista = new LinkedList<>();

        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_listardisponibles()}");
            ResultSet rs =cs.executeQuery();

            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setId_hab(rs.getInt("id_hab"));
                h.setNum_hab(rs.getInt("num_hab"));
                h.setTipo_nombre(rs.getString("tipo_nombre")); // asegúrate de tener este campo en la entidad
                lista.add(h);
            }

            rs.close();
            cs.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error al listar habitaciones disponibles: " + e.getMessage());
        }

        return lista;
    }

}
