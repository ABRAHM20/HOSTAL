<%--
    Document   : 2-alquiler
    Created on : 6 jul. 2025, 4:08:09 a. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, pe.edu.entity.Alquiler"%>
<%@page import="pe.edu.entity.Cliente, pe.edu.entity.Habitacion"%>
<%@page import="pe.edu.dao.PagoDao"%>
<%@page session="true" %>

<%
    // --- OBTENCIÓN DE DATOS DESDE EL SERVLET ---
    // Lista de clientes para el modal de creación
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    if (clientes == null) clientes = new java.util.ArrayList<>();

    // Lista principal de alquileres a mostrar
    LinkedList<Alquiler> lista = (LinkedList<Alquiler>) request.getAttribute("lista");
    if (lista == null) lista = new java.util.LinkedList<>();

    // Lista de habitaciones disponibles para el modal de creación
    List<Habitacion> habitaciones = (List<Habitacion>) request.getAttribute("habitaciones");
    if (habitaciones == null) habitaciones = new java.util.ArrayList<>();
    
    // Objeto de detalle para el modal de "Ver y Check-out"
    Alquiler detalle = (Alquiler) request.getAttribute("detalle");
    
    // Bandera para saber si se debe abrir un modal al cargar la página
    String modal = (String) request.getAttribute("modal");

    // DAO para verificar el estado del pago
    PagoDao pagoDao = new PagoDao();
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Alquileres</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/1-estilos/estilos.css" />
        <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:FILL@1" rel="stylesheet" />
    </head>
    <body>
        <%@include file="../navbarRecep.jsp" %>

        <div class="titulo-contenido">
            <header class="barra-superior">
                <h2 class="titulo-seccion">GESTIONAR ALQUILERES</h2>
                <div class="usuario">
                    <span class="material-symbols-rounded">manage_accounts</span>
                    Administrador
                </div>
            </header>
        </div>

        <div class="tarjeta">
            <div class="acciones-superiores">
                <button class="btn-crear" onclick="abrirModalDesdeTemplate('modal-crear-alquiler')">
                    <span class="material-symbols-rounded">add</span>Registrar alquiler
                </button>
                <div class="filtros">
                    <form method="get" action="<%= request.getContextPath() %>/AlquilerController">
                        <label for="estado">Filtrar por estado:</label>
                        <select name="estado" id="estado" onchange="this.form.submit()">
                            <option value="">-- Todos --</option>
                            <option value="check-in" <%= "check-in".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Check-in</option>
                            <option value="check-out" <%= "check-out".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Check-out</option>
                        </select>
                    </form>
                </div>
            </div>

            <div class="contenido">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cliente</th>
                            <th>Habitación</th>
                            <th>Ingreso</th>
                            <th>Salida</th>
                            <th>Horas</th>
                            <th>Total</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                            <th>Pago</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Alquiler a : lista) { %>
                        <tr>
                            <td><%= a.getId_alq() %></td>
                            <td><%= a.getCliente().getNombre() %></td>
                            <td><%= a.getHabitacion().getNum_hab() %></td>
                            <td><%= a.getFecha_inicio() != null ? a.getFecha_inicio() : "-" %></td>
                            <td><%= a.getFecha_fin() != null ? a.getFecha_fin() : "-" %></td>
                            <td><%= a.getHoras() %></td>
                            <td>S/ <%= a.getTotal() %></td>
                            <td><%= a.getEstado() %></td>
                            <td>
                                <%-- ✅ Lógica de acción simplificada --%>
                                <%-- Si el alquiler está activo, muestra el botón para Ver/Editar --%>
                                <% if ("check-in".equalsIgnoreCase(a.getEstado())) { %>
                                    <form method="get" action="<%= request.getContextPath() %>/AlquilerController">
                                        <input type="hidden" name="accion" value="ver">
                                        <input type="hidden" name="id" value="<%= a.getId_alq() %>">
                                        <button class="icono-boton" type="submit" title="Ver Detalles y Check-out">
                                            <span class="material-symbols-rounded">visibility</span>
                                        </button>
                                    </form>
                                <% } else { %>
                                    <%-- Si ya está en check-out, no hay acción disponible --%>
                                    <span>-</span>
                                <% } %>
                            </td>
                            <td>
                                <%
                                    boolean yaPagado = pagoDao.obtenerPorAlquiler(a.getId_alq()) != null;
                                    if (!yaPagado) {
                                %>
                                <button class="icono-boton" type="button" onclick="abrirModalPago(<%= a.getId_alq() %>)" title="Registrar Pago">
                                    <span class="material-symbols-rounded">payments</span>
                                </button>
                                <% } else { %>
                                <span class="material-symbols-rounded" style="color: green;" title="Pagado">check_circle</span>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="modal" class="modal">
            <div class="modal-contenido">
                <span class="cerrar" onclick="cerrarModal()">&times;</span>
                <div id="modal-body"></div>
            </div>
        </div>

        <template id="modal-crear-alquiler">
            <form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/AlquilerController">
                <input type="hidden" name="accion" value="nuevo">
                <h3>Registrar Nuevo Alquiler</h3>

                <label>Buscar Cliente</label>
                <input list="clientes" id="cliente_input" oninput="setClienteId()" required placeholder="Buscar por nombre o DNI">
                <datalist id="clientes">
                    <% for (Cliente cli : clientes) { %>
                    <option data-id="<%= cli.getId_cliente() %>" value="<%= cli.getNombre() %> - <%= cli.getDni() %>"></option>
                    <% } %>
                </datalist>
                <input type="hidden" name="id_cliente" id="id_cliente_hidden">

                <br><label>Buscar Habitación Disponible</label>
                <input list="habitaciones" id="hab_input" oninput="setHabId()" required>
                <datalist id="habitaciones">
                    <% for (Habitacion h : habitaciones) { %>
                    <option data-id="<%= h.getId_hab() %>" value="Hab. <%= h.getNum_hab() %> - <%= h.getTipo_nombre() %>"></option>
                    <% } %>
                </datalist>
                <input type="hidden" name="id_hab" id="id_hab_hidden">

                <br><label>Horas</label><br>
                <input type="number" name="horas" required>
                
                <br><label>Fecha Ingreso</label>
                <input type="date" name="fecha_inicio" required>
                
                <br><label>Fecha Salida (Estimada)</label>
                <input type="date" name="fecha_salida" required>
                
                <input type="hidden" name="estado_alquiler" value="check-in">

                <br><br>
                <button type="submit">Guardar</button>
                <button type="button" onclick="cerrarModal()">Cancelar</button>
            </form>
        </template>

        <template id="modal-ver-y-checkout">
            <div class="formulario-modal">
                <h3>Detalles del Alquiler</h3>
                
                <label>Cliente:</label>
                <input type="text" id="detalle-cliente" readonly>
                
                <label>Habitación:</label>
                <input type="text" id="detalle-habitacion" readonly>
                
                <label>Fecha de Ingreso:</label>
                <input type="text" id="detalle-inicio" readonly>
                <hr>

                <form method="post" action="<%= request.getContextPath() %>/AlquilerController" onsubmit="return confirm('¿Está seguro de finalizar este alquiler?')">
                    <input type="hidden" name="accion" value="checkout">
                    <input type="hidden" name="id_alquiler" id="checkout_id_alquiler">
                    
                    <p>Para finalizar este alquiler, presione el botón de Check-out.</p>
                    
                    <button type="submit" class="btn-crear">Realizar Check-out</button>
                    <button type="button" onclick="cerrarModal()">Cancelar</button>
                </form>
            </div>
        </template>

        <template id="modal-pago">
            <form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/PagoController">
                <input type="hidden" name="accion" value="registrar">
                <input type="hidden" name="id_alq" id="pago_id_alq">

                <h3>Registrar Pago</h3>

                <label>Método de pago</label>
                <select name="metodo_pago" required>
                    <option value="efectivo">Efectivo</option>
                    <option value="tarjeta">Tarjeta</option>
                    <option value="yape">Yape</option>
                </select>
                
                <br>
                <label>Tipo de comprobante</label>
                <select name="tipo_comprobante" id="tipo_comprobante" onchange="mostrarRUC()">
                    <option value="boleta">Boleta</option>
                    <option value="factura">Factura</option>
                </select>

                <div id="campo_ruc" style="display: none;">
                    <label>RUC</label>
                    <input type="text" name="ruc" pattern="[0-9]{11}" placeholder="Ej. 20123456789">
                </div>

                <label>Monto pagado</label>
                <input type="number" name="monto_pagado" step="0.01" min="0" required>
                <br><br>
                <button type="submit">Registrar</button>
                <button type="button" onclick="cerrarModal()">Cancelar</button>
            </form>
        </template>

        <script>
            // Funciones genéricas para manejar modales
            function abrirModalDesdeTemplate(templateId) {
                const template = document.getElementById(templateId);
                if (template) {
                    document.getElementById("modal-body").innerHTML = template.innerHTML;
                    document.getElementById("modal").style.display = "flex";
                }
            }

            function cerrarModal() {
                document.getElementById("modal").style.display = "none";
                document.getElementById("modal-body").innerHTML = "";
            }

            window.onclick = function (event) {
                const modal = document.getElementById("modal");
                if (event.target === modal) {
                    cerrarModal();
                }
            }

            // Funciones para los datalists de creación
            function setHabId() {
                const input = document.getElementById("hab_input");
                const hidden = document.getElementById("id_hab_hidden");
                for (const option of document.getElementById("habitaciones").options) {
                    if (option.value === input.value) {
                        hidden.value = option.dataset.id;
                        return;
                    }
                }
                hidden.value = "";
            }

            function setClienteId() {
                const input = document.getElementById("cliente_input");
                const hidden = document.getElementById("id_cliente_hidden");
                for (const option of document.getElementById("clientes").options) {
                    if (option.value === input.value) {
                        hidden.value = option.dataset.id;
                        return;
                    }
                }
                hidden.value = "";
            }

            // Funciones para el modal de pago
            function abrirModalPago(idAlq) {
                abrirModalDesdeTemplate('modal-pago');
                document.getElementById("pago_id_alq").value = idAlq;
            }

            function mostrarRUC() {
                const tipo = document.getElementById("tipo_comprobante").value;
                document.getElementById("campo_ruc").style.display = tipo === "factura" ? "block" : "none";
            }

            // ✅ Lógica para abrir el modal de "Ver y Check-out" al cargar la página
            <% if ("ver".equals(modal) && detalle != null) { %>
            window.onload = function () {
                abrirModalDesdeTemplate('modal-ver-y-checkout');

                // Rellenar campos visibles
                document.getElementById("detalle-cliente").value = "<%= detalle.getCliente().getNombre() %>";
                document.getElementById("detalle-habitacion").value = "<%= detalle.getHabitacion().getNum_hab() %>";
                document.getElementById("detalle-inicio").value = "<%= detalle.getFecha_inicio() != null ? detalle.getFecha_inicio() : "-" %>";
                
                // Rellenar el ID oculto en el formulario del modal
                document.getElementById("checkout_id_alquiler").value = "<%= detalle.getId_alq() %>";
            };
            <% } %>
        </script>
    </body>
</html>
