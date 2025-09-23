/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import pe.edu.dao.PagoDao;
import pe.edu.entity.Alquiler;
import pe.edu.entity.Pago;
import pe.edu.entity.Usuario;
import pe.edu.util.GeneradorBoleta;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "PagoController", urlPatterns = {"/PagoController"})
public class PagoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        PagoDao dao = new PagoDao();

        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

        if ("ver".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id_pago"));
            Pago p = dao.obtenerPorId(id);
            request.setAttribute("detalle", p);
            request.setAttribute("modal", "ver");
        }

        //FILTROSS
        String metodo = request.getParameter("metodo");
        String clienteIdStr = request.getParameter("cliente");
        LinkedList<Pago> lista;

        if (metodo != null && !metodo.isEmpty()) {
            lista = dao.listarPorMetodo(metodo);
            request.setAttribute("metodoSeleccionado", metodo);
        } else if (clienteIdStr != null && !clienteIdStr.isEmpty()) {
            int idCliente = Integer.parseInt(clienteIdStr);
            lista = dao.listarPorCliente(idCliente);
            request.setAttribute("clienteSeleccionado", idCliente);
        } else {
            lista = dao.listar();
        }

        request.setAttribute("lista", lista);

        if (u != null && "A".equalsIgnoreCase(u.getRol())) {
            request.getRequestDispatcher("2-administrador/4-pago.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("3-recepcionista/4-pago.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        PagoDao dao = new PagoDao();

        if ("registrar".equals(accion)) {
            try {
                int id_alq = Integer.parseInt(request.getParameter("id_alq"));
                String metodo = request.getParameter("metodo_pago");
                String tipo = request.getParameter("tipo_comprobante");
                double monto = Double.parseDouble(request.getParameter("monto_pagado"));
                String ruc = request.getParameter("ruc");

                if (!"factura".equalsIgnoreCase(tipo)) {
                    ruc = null;
                }

                Alquiler a = new Alquiler();
                a.setId_alq(id_alq);

                Pago p = new Pago();
                p.setAlq(a);
                p.setMetodo_pago(metodo);
                p.setTipo_comprobante(tipo);
                p.setMonto_pagado(monto);
                p.setRuc(ruc);

                dao.insertar(p);

                Pago pagoRegistrado = dao.obtenerPorAlquiler(id_alq);

                //Generar archivo de la boleta
                String ruta = GeneradorBoleta.generarBoleta(pagoRegistrado);
                java.io.File archivo = new java.io.File(ruta);

                if (archivo.exists()) {
                    response.setContentType("text/plain");
                    response.setHeader("Content-Disposition", "attachment;filename=" + archivo.getName());

                    java.nio.file.Files.copy(archivo.toPath(), response.getOutputStream());
                    response.getOutputStream().flush();
                } else {
                    response.sendRedirect("AlquilerController?error=Error al generar archivo de boleta");
                }

                return;

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("AlquilerController?error=Error al registrar el pago");
                return;
            }
        } else if ("descargar".equals(accion)) {
            try {
                int idAlq = Integer.parseInt(request.getParameter("id_alq"));
                Pago pago = dao.obtenerPorAlquiler(idAlq);

                if (pago != null) {
                    String ruta = GeneradorBoleta.generarBoleta(pago);
                    java.io.File archivo = new java.io.File(ruta);

                    if (archivo.exists()) {
                        response.setContentType("text/plain");
                        response.setHeader("Content-Disposition", "attachment;filename=" + archivo.getName());

                        java.nio.file.Files.copy(archivo.toPath(), response.getOutputStream());
                        response.getOutputStream().flush();
                        return; // Importante: no hacer redirect después
                    } else {
                        response.sendRedirect("PagoController?error=Archivo no encontrado");
                        return;
                    }
                } else {
                    response.sendRedirect("PagoController?error=No se encontró el pago");
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("PagoController?error=Error al generar boleta");
                return;
            }
        }
    }

}
