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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.entity.Cliente;
import pe.edu.util.Conexion;

/**
 *
 * @author LENOVO
 */
public class ClienteDao implements DaoCrud<Cliente> {

    @Override
    public LinkedList<Cliente> listar() {
        LinkedList<Cliente> lista = new LinkedList<>();
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            CallableStatement cs = cnx.prepareCall("{call sp_cliente_listar()}");
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setDni(rs.getString("dni"));
                c.setSexo(rs.getString("sexo"));
                lista.add(c);
            }

            rs.close();
            cs.close();
            cnx.close();
        } catch (Exception e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void insertar(Cliente obj) {
        try {
        Connection cnx = Conexion.getInstancia().getConexion();
        CallableStatement cs = cnx.prepareCall("{call sp_cliente_insertar(?, ?, ?, ?, ?)}");
        cs.setString(1, obj.getNombre());
        cs.setString(2,obj.getApellido());
        cs.setString(3, obj.getDni());
        cs.setString(4, obj.getSexo());
        cs.setInt(5, obj.getid_usuario());
        cs.execute();
        cs.close();
        cnx.close();
    } catch (Exception e) {
        System.out.println("Error al insertar cliente: " + e.getMessage());
    }
    }

    @Override
    public Cliente leer(int id) {
        return null;
    }

    @Override
    public void editar(Cliente obj) {
    }

    @Override
    public void eliminar(int id) {
    }

}
