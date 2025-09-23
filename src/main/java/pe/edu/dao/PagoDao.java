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
import pe.edu.entity.Alquiler;
import pe.edu.entity.Cliente;
import pe.edu.entity.Habitacion;
import pe.edu.entity.Pago;
import pe.edu.util.Conexion;

/**
 *
 * @author LENOVO
 */
public class PagoDao implements DaoCrud<Pago> {

    /*@Override
    public LinkedList<Pago> listar() {
        LinkedList<Pago> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call listar_pagos()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                p.setAlq(a);
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setTipo_comprobante(rs.getString("tipo_comprobante"));
                p.setMonto_pagado(rs.getDouble("monto_pagado"));
                p.setVuelto(rs.getDouble("vuelto"));
                p.setRuc(rs.getString("ruc"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));

                lista.add(p);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en listarPagos: " + e.getMessage());
        }
        return lista;
    }*/
    @Override
    public LinkedList<Pago> listar() {
        LinkedList<Pago> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_pago_listar()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setTipo_comprobante(rs.getString("tipo_comprobante"));
                p.setMonto_pagado(rs.getDouble("monto_pagado"));
                p.setVuelto(rs.getDouble("vuelto"));
                p.setRuc(rs.getString("ruc"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));

                // Datos del cliente
                Cliente c = new Cliente();
                c.setNombre(rs.getString("nombre_cliente"));
                c.setDni(rs.getString("dni"));

                // Datos de la habitación
                Habitacion h = new Habitacion();
                h.setNum_hab(rs.getInt("num_hab"));

                // Alquiler
                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                a.setCliente(c);
                a.setHabitacion(h);

                p.setAlq(a);

                lista.add(p);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en listarPagos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void insertar(Pago obj) {
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call registrar_pago(?, ?, ?, ?, ?)}");
            cs.setInt(1, obj.getAlq().getId_alq());
            cs.setString(2, obj.getMetodo_pago());
            cs.setString(3, obj.getTipo_comprobante());
            cs.setDouble(4, obj.getMonto_pagado());
            cs.setString(5, obj.getRuc());
            cs.executeUpdate();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en insertar pago: " + e.getMessage());
        }
    }

    @Override
    public Pago leer(int id) {
        return null;
    }

    @Override
    public void editar(Pago obj) {
    }

    @Override
    public void eliminar(int id) {
    }

    public LinkedList<Pago> listarPorCliente(int idCliente) {
        LinkedList<Pago> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call listar_pagos_por_cliente(?)}");
            cs.setInt(1, idCliente);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                p.setAlq(a);
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setTipo_comprobante(rs.getString("tipo_comprobante"));
                p.setMonto_pagado(rs.getDouble("monto_pagado"));
                p.setVuelto(rs.getDouble("vuelto"));
                p.setRuc(rs.getString("ruc"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));

                lista.add(p);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en listarPagosPorCliente: " + e.getMessage());
        }
        return lista;
    }

    //Filtrar por método de pago
    public LinkedList<Pago> listarPorMetodo(String metodo) {
        LinkedList<Pago> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_pago_filtrar_por_metodo(?)}");
            cs.setString(1, metodo);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setTipo_comprobante(rs.getString("tipo_comprobante"));
                p.setMonto_pagado(rs.getDouble("monto_pagado"));
                p.setVuelto(rs.getDouble("vuelto"));
                p.setRuc(rs.getString("ruc"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));

                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));

                // Cliente
                Cliente c = new Cliente();
                c.setNombre(rs.getString("nombre_cliente"));
                c.setDni(rs.getString("dni"));

                // Habitación
                Habitacion h = new Habitacion();
                h.setNum_hab(rs.getInt("num_hab"));

                // Asignar cliente y habitación al alquiler
                a.setCliente(c);
                a.setHabitacion(h);

                // Asignar alquiler al pago
                p.setAlq(a);

                lista.add(p);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en listarPagosPorMetodo: " + e.getMessage());
        }
        return lista;
    }

    //Obtener detalles para imprimir boleta por ID de pago
    public Pago obtenerPorId(int idPago) {
        Pago p = null;
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call obtener_pago(?)}");
            cs.setInt(1, idPago);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setTipo_comprobante(rs.getString("tipo_comprobante"));
                p.setMonto_pagado(rs.getDouble("monto_pagado"));
                p.setVuelto(rs.getDouble("vuelto"));
                p.setRuc(rs.getString("ruc"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));

                // Alquiler embebido
                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                a.setHoras(rs.getInt("horas"));
                a.setTotal(rs.getDouble("total"));
                a.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
                a.setFecha_fin(rs.getTimestamp("fecha_fin"));

                // Cliente embebido
                Cliente c = new Cliente();
                c.setNombre(rs.getString("cliente_nombre"));
                c.setDni(rs.getString("dni"));
                a.setCliente(c);

                // Habitación embebida
                Habitacion h = new Habitacion();
                h.setNum_hab(rs.getInt("num_hab"));
                h.setTipo_nombre(rs.getString("tipo_habitacion"));
                a.setHabitacion(h);

                p.setAlq(a);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en obtenerPagoPorId: " + e.getMessage());
        }
        return p;
    }

    public Pago obtenerPorAlquiler(int idAlq) {
        Pago p = null;
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call obtener_pago_por_alquiler(?)}");
            cs.setInt(1, idAlq);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setTipo_comprobante(rs.getString("tipo_comprobante"));
                p.setMonto_pagado(rs.getDouble("monto_pagado"));
                p.setVuelto(rs.getDouble("vuelto"));
                p.setRuc(rs.getString("ruc"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));

                //Alquiler embebido
                Alquiler a = new Alquiler();
                a.setId_alq(rs.getInt("id_alq"));
                a.setHoras(rs.getInt("horas"));
                a.setTotal(rs.getDouble("total"));
                a.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
                a.setFecha_fin(rs.getTimestamp("fecha_fin"));

                //Cliente embebido
                Cliente c = new Cliente();
                c.setNombre(rs.getString("cliente_nombre"));
                c.setDni(rs.getString("dni"));
                a.setCliente(c);

                //Habitación embebida
                Habitacion h = new Habitacion();
                h.setNum_hab(rs.getInt("num_hab"));
                h.setTipo_nombre(rs.getString("tipo_habitacion"));
                a.setHabitacion(h);

                p.setAlq(a);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (SQLException e) {
            System.out.println("Error en obtenerPorAlquiler: " + e.getMessage());
        }
        return p;
    }

}
