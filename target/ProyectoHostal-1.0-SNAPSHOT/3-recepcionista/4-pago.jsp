<%-- 
    Document   : 4-pago
    Created on : 7 jul. 2025, 6:00:44 a. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, pe.edu.entity.Pago, pe.edu.entity.Alquiler, pe.edu.entity.Cliente, pe.edu.entity.Habitacion"%>
<%@page session="true"%>

<%
    LinkedList<Pago> lista = (LinkedList<Pago>) request.getAttribute("lista");
    String metodoSeleccionado = (String) request.getAttribute("metodoSeleccionado");
    Integer clienteSeleccionado = (Integer) request.getAttribute("clienteSeleccionado");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pagos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/1-estilos/estilos.css" />
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:FILL@1" rel="stylesheet" />
</head>
<body>
    <%@include file="../navbarRecep.jsp" %>

    <div class="titulo-contenido">
        <header class="barra-superior">
            <h2 class="titulo-seccion">GESTIONAR PAGOS</h2>
            <div class="usuario">
                <span class="material-symbols-rounded">manage_accounts</span>
                Recepcionista
            </div>
        </header>
    </div>

    <div class="tarjeta">
        <div class="acciones-superiores">
            <div class="filtros">
                <form method="get" action="<%= request.getContextPath() %>/PagoController">
                    <label for="metodo">Método de pago:</label>
                    <select name="metodo" id="metodo" onchange="this.form.submit()">
                        <option value="">-- Todos --</option>
                        <option value="efectivo" <%= "efectivo".equals(metodoSeleccionado) ? "selected" : "" %>>Efectivo</option>
                        <option value="tarjeta" <%= "tarjeta".equals(metodoSeleccionado) ? "selected" : "" %>>Tarjeta</option>
                        <option value="yape" <%= "yape".equals(metodoSeleccionado) ? "selected" : "" %>>Yape</option>
                    </select>
                </form>
            </div>
        </div>

        <div class="contenido">
            <table>
                <thead>
                    <tr>
                        <th>ID Pago</th>
                        <th>Cliente</th>
                        <th>DNI</th>
                        <th>Habitación</th>
                        <th>Método</th>
                        <th>Tipo Comprobante</th>
                        <th>Monto Pagado</th>
                        <th>Fecha</th>
                        <th>Comprobante</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (lista != null) {
                        for (Pago p : lista) { %>
                            <tr>
                                <td><%= p.getId_pago() %></td>
                                <td><%= p.getAlq().getCliente().getNombre() %></td>
                                <td><%= p.getAlq().getCliente().getDni() %></td>
                                <td><%= p.getAlq().getHabitacion().getNum_hab() %></td>
                                <td><%= p.getMetodo_pago() %></td>
                                <td><%= p.getTipo_comprobante() %></td>
                                <td>S/ <%= p.getMonto_pagado() %></td>
                                <td><%= p.getFecha_pago() %></td>
                                <td>
                                    <form method="post" action="<%= request.getContextPath() %>/PagoController">
                                        <input type="hidden" name="accion" value="descargar">
                                        <input type="hidden" name="id_alq" value="<%= p.getAlq().getId_alq() %>">
                                        <button class="icono-boton" type="submit">
                                            <span class="material-symbols-rounded">description</span>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                    <%  }
                    } else { %>
                        <tr><td colspan="9">No se encontraron pagos.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
