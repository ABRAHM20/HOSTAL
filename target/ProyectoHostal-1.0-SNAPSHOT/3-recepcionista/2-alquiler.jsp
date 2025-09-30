<%-- 
    Document   : 2-alquiler
    Created on : 6 jul. 2025, 4:08:09 a. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, pe.edu.entity.Alquiler"%>
<%@page import="pe.edu.entity.Cliente, pe.edu.entity.Habitacion"%>
<%@page session="true" %>
<%@page import="pe.edu.dao.AlquilerDao"%>
<%@page import="pe.edu.entity.Alquiler"%>
<%@page import="java.util.LinkedList"%>

<%
    //AlquilerDao alquilerDao = new AlquilerDao();
    pe.edu.dao.ClienteDao clienteDao = new pe.edu.dao.ClienteDao();
    List<pe.edu.entity.Cliente> clientes = clienteDao.listar();
    LinkedList<Alquiler> lista = (LinkedList<Alquiler>) request.getAttribute("lista");
    List<Habitacion> habitaciones = (List<Habitacion>)  request.getAttribute("habitaciones");
    Alquiler detalle = (Alquiler) request.getAttribute("detalle");
    String modal = (String) request.getAttribute("modal");
    
    pe.edu.dao.PagoDao pagoDao = new pe.edu.dao.PagoDao();

    
%>

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
                     Recepcionista
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
                            <option value="pendiente" <%= "pendiente".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Pendiente</option>
                            <option value="activo" <%= "activo".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Activo</option>
                            <option value="finalizado" <%= "finalizado".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Finalizado</option>
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
                            <th>Ver</th>
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
                                <% if ("pendiente".equalsIgnoreCase(a.getEstado())) { %>
                                <form method="get" action="<%= request.getContextPath() %>/AlquilerController">
                                    <input type="hidden" name="accion" value="activar">
                                    <input type="hidden" name="id" value="<%= a.getId_alq() %>">
                                    <button class="icono-boton" type="submit" onclick="return alert('Alquiler activado.')">
                                        <span class="material-symbols-rounded">play_arrow</span>
                                    </button>
                                </form>
                                <% } else if ("activo".equalsIgnoreCase(a.getEstado())) { %>
                                <form method="get" action="<%= request.getContextPath() %>/AlquilerController">
                                    <input type="hidden" name="accion" value="finalizar">
                                    <input type="hidden" name="id" value="<%= a.getId_alq() %>">
                                    <button class="icono-boton" type="submit" onclick="return alert('Alquiler finalizado.')">
                                        <span class="material-symbols-rounded">stop</span>
                                    </button>
                                </form>
                                <% } else { %>
                                <span>-</span>
                                <% } %>
                            </td>
                            <td>
                                <form method="get" action="<%= request.getContextPath() %>/AlquilerController">
                                    <input type="hidden" name="accion" value="ver">
                                    <input type="hidden" name="id" value="<%= a.getId_alq() %>">
                                    <button class="icono-boton" type="submit">
                                        <span class="material-symbols-rounded">visibility</span>
                                    </button>
                                </form>
                            </td>
                            <td>
                                <%
                                    boolean yaPagado = pagoDao.obtenerPorAlquiler(a.getId_alq()) != null;
                                    if (!yaPagado) {
                                %>
                                <button class="icono-boton" type="button"
                                        onclick="abrirModalPago(<%= a.getId_alq() %>)">
                                    <span class="material-symbols-rounded">payments</span>
                                </button>
                                <%
                                    } else {
                                %>
                                <span class="material-symbols-rounded" style="color: gray;">check_circle</span>
                                <%
                                    }
                                %>
                            </td>

                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Modal -->
        <div id="modal" class="modal">
            <div class="modal-contenido">
                <span class="cerrar" onclick="cerrarModal()">&times;</span>
                <div id="modal-body"></div>
            </div>
        </div>

        <!-- Templates -->
        <template id="modal-crear-alquiler">
            <form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/AlquilerController">
                <input type="hidden" name="accion" value="nuevo">
                <h3>Registrar nuevo alquiler</h3>

                <label>Buscar Cliente</label>
                <input list="clientes" id="cliente_input" oninput="setClienteId()" required placeholder="Buscar por nombre o DNI">
                <datalist id="clientes">
                    <% for (pe.edu.entity.Cliente cli : clientes) { %>
                    <option data-id="<%= cli.getId_cliente() %>" value="<%= cli.getNombre() %> - <%= cli.getDni() %>"></option>
                    <% } %>
                </datalist>
                <input type="hidden" name="id_cliente" id="id_cliente_hidden">



                <br><label>Buscar Habitación</label>
                <input list="habitaciones" id="hab_input" oninput="setHabId()" required>
                <datalist id="habitaciones">
                    <% for (pe.edu.entity.Habitacion h : habitaciones) { %>
                    <option data-id="<%= h.getId_hab() %>" value="Hab. <%= h.getNum_hab() %> - <%= h.getTipo_nombre() %>"></option>
                        <% } %>
                </datalist>
                <input type="hidden" name="id_hab" id="id_hab_hidden">

                <br><label>Horas</label><br>
                <input type="number" name="horas" required>
                
                <br><label>Fecha Ingreso</label>
                <input type="date" name="fecha_inicio">
                
                <br><label>Fecha Salida</label>
                <input type="date" name="fecha_salida">
                
                <br><label>Estado</label>
                    <select name="estado_alquiler">
                        <option value="1">Check in</option>
                        <option value="2">Check out</option>
                    </select><br>



                <button type="submit">Guardar</button>
                <button type="button" onclick="cerrarModal()">Cancelar</button>
            </form>
        </template>

        <template id="modal-ver-alquiler">
            <form class="formulario-modal">
                <h3>Detalle de alquiler</h3>

                <label>Cliente</label>
                <input type="text" id="detalle-cliente" readonly>

                <label>Habitación</label>
                <input type="text" id="detalle-habitacion" readonly>

                <label>Tipo de habitación</label>
                <input type="text" id="detalle-tipo" readonly>

                <label>Horas</label>
                <input type="text" id="detalle-horas" readonly>

                <label>Fecha de ingreso</label>
                <input type="text" id="detalle-inicio" readonly>

                <label>Fecha de salida</label>
                <input type="text" id="detalle-fin" readonly>

                <label>Total</label>
                <input type="text" id="detalle-total" readonly>

                <button type="button" onclick="cerrarModal()">Cerrar</button>
            </form>
        </template>


        <template id="modal-pago">
            <form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/PagoController">
                <input type="hidden" name="accion" value="registrar">
                <input type="hidden" name="id_alq" id="pago_id_alq">

                <h3>Registrar pago</h3>

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

                <button type="submit">Registrar</button>
                <button type="button" onclick="cerrarModal()">Cancelar</button>
            </form>
        </template>



        <!-- Scripts -->
        <script>
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
                if (event.target === modal)
                    cerrarModal();
            }

            function setHabId() {
                const input = document.getElementById("hab_input");
                const datalist = document.getElementById("habitaciones");
                const hidden = document.getElementById("id_hab_hidden");
                const options = datalist.options;

                hidden.value = "";

                for (let i = 0; i < options.length; i++) {
                    if (options[i].value === input.value) {
                        hidden.value = options[i].dataset.id;
                        break;
                    }
                }
            }

            function setClienteId() {
                const input = document.getElementById("cliente_input");
                const datalist = document.getElementById("clientes");
                const hidden = document.getElementById("id_cliente_hidden");
                const options = datalist.options;

                hidden.value = "";

                for (let i = 0; i < options.length; i++) {
                    if (options[i].value === input.value) {
                        hidden.value = options[i].dataset.id;
                        break;
                    }
                }
            }

            function abrirModalPago(idAlq) {
                abrirModalDesdeTemplate('modal-pago');
                document.getElementById("pago_id_alq").value = idAlq;
            }

            function mostrarRUC() {
                var tipo = document.getElementById("tipo_comprobante").value;
                var campo = document.getElementById("campo_ruc");
                campo.style.display = tipo === "factura" ? "block" : "none";
            }




            <% if ("ver".equals(modal) && detalle != null) { %>
            window.onload = function () {
                abrirModalDesdeTemplate('modal-ver-alquiler');

                document.getElementById("detalle-cliente").value = "<%= detalle.getCliente().getNombre() %>";
                document.getElementById("detalle-habitacion").value = "<%= detalle.getHabitacion().getNum_hab() %>";
                document.getElementById("detalle-tipo").value = "<%= detalle.getHabitacion().getTipo_nombre() %>";
                document.getElementById("detalle-horas").value = "<%= detalle.getHoras() %>";
                document.getElementById("detalle-inicio").value = "<%= detalle.getFecha_inicio() != null ? detalle.getFecha_inicio() : "-" %>";
                document.getElementById("detalle-fin").value = "<%= detalle.getFecha_fin() != null ? detalle.getFecha_fin() : "-" %>";
                document.getElementById("detalle-total").value = "S/ <%= detalle.getTotal() %>";
            };
            <% } %>
            <% if ("Pago registrado".equals(request.getParameter("mensaje"))) { %>
            window.onload = function () {
                cerrarModal();
            };
            <% } %>
        </script>
    </body>
</html>
