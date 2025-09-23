<%-- 
    Document   : 1-habitaciones
    Created on : 27 jun. 2025, 11:15:26 p. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="pe.edu.dao.HabitacionDao"%>
<%@page import="pe.edu.entity.Habitacion"%>
<%@page import="java.util.LinkedList"%>
<%
    //HabitacionDao dao = new HabitacionDao();
    //LinkedList<Habitacion> lista = dao.listar();
    //LinkedList<Habitacion> lista = (LinkedList<Habitacion>) request.getAttribute("habitaciones"); 
    LinkedList<Habitacion> lista = (LinkedList<Habitacion>) request.getAttribute("lista");

    //List<Habitacion> lista = (List<Habitacion>) request.getAttribute("habitaciones");
    String estadoSeleccionado = (String) request.getAttribute("estadoSeleccionado");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/1-estilos/estilos.css">
        <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:FILL@1" rel="stylesheet" />
        <title>Habitaciones</title>
    </head>
    <body>
        <button class="hamburger">&#9776;</button>

        <%@include file="../navbarAdmin.jsp" %>

        <div class="titulo-contenido">
            <header class="barra-superior">
                <h2 class="titulo-seccion">GESTIONAR HABITACIONES</h2>
                <div class="usuario">
                    <span class="material-symbols-rounded">admin_panel_settings</span>
                    Recepcionista

                </div>
            </header>
        </div>

        <div class="tarjeta">
            <div>
                    <div class="acciones-derecha">
                        <div class="filtros">
                            <form method="get" action="<%= request.getContextPath() %>/HabitacionController">
                                <label for="estado">Filtrar por estado:</label>
                                <select name="estado" id="estado" onchange="this.form.submit()">
                                    <option value="">-- Todos --</option>
                                    <option value="disponible" <%= "disponible".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Disponible</option>
                                    <option value="ocupado" <%= "ocupado".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Ocupado</option>
                                    <option value="mantenimiento" <%= "mantenimiento".equals(request.getAttribute("estadoSeleccionado")) ? "selected" : "" %>>Mantenimiento</option>
                                </select>
                            </form>
                        </div>

                    </div>
                </div>

                <div class="contenido">
                    <div class="tabla-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>N°</th>
                                    <th>Tipo</th>
                                    <th>Precio</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (lista != null) {
           for (Habitacion h : lista) {
                                %>
                                <tr>
                                    <td><%= h.getNum_hab() %></td>
                                    <td><%= h.getTipo_nombre() %></td>
                                    <td>S/ <%= h.getPrecio() %></td>
                                    <td><%= h.getEstado() %></td>
                                </tr>
                                <%   }
       } else { %>
                                <tr><td colspan="6">No se encontraron habitaciones.</td></tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
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
            <template id="modal-crear-habitacion">
                <form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/HabitacionController">
                    <h3>Añadir habitación</h3>
                    <input type="hidden" name="accion" value="nuevo">
                    <label>Número de habitación:</label>
                    <input type="number" name="num_hab" required><br>
                    <label>Tipo:</label><br>
                    <select name="id_tipo_hab">
                        <option value="1">Simple</option>
                        <option value="2">Doble</option>
                        <option value="3">Matrimonial</option>
                        <option value="4">King</option>
                    </select><br>
                    <label>Estado:</label>
                    <select name="estado">
                        <option value="Disponible">Disponible</option>
                    </select>
                    <button type="submit">Guardar</button>
                </form>
            </template>
        </div>

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


            function abrirModalModificar(id, num, tipo, estado) {
                const html =
                        '<form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/HabitacionController">' +
                        '<h3>Modificar habitación</h3>' +
                        '<input type="hidden" name="accion" value="editar">' +
                        '<input type="hidden" name="id_hab" value="' + id + '">' +
                        '<label>Número de habitación:</label>' +
                        '<input type="number" name="num_hab" value="' + num + '" required><br>' +
                        '<label>Tipo:</label><br>' +
                        '<select name="id_tipo_hab">' +
                        '<option value="1"' + (tipo === 'Simple' ? ' selected' : '') + '>Simple</option>' +
                        '<option value="2"' + (tipo === 'Doble' ? ' selected' : '') + '>Doble</option>' +
                        '<option value="3"' + (tipo === 'Matrimonial' ? ' selected' : '') + '>Matrimonial</option>' +
                        '<option value="4"' + (tipo === 'King' ? ' selected' : '') + '>King</option>' +
                        '</select><br>' +
                        '<label>Estado:</label><br>' +
                        '<select name="estado">' +
                        '<option value="Disponible"' + (estado === 'Disponible' ? ' selected' : '') + '>Disponible</option>' +
                        '<option value="Ocupada"' + (estado === 'Ocupada' ? ' selected' : '') + '>Ocupada</option>' +
                        '</select><br>' +
                        '<button type="submit">Guardar</button>' +
                        '</form>';

                document.getElementById("modal-body").innerHTML = html;
                document.getElementById("modal").style.display = "flex";
            }

            function toggleMenu() {
                document.getElementById("sidebar").classList.toggle("active");
            }

            document.addEventListener("DOMContentLoaded", () => {
                const toggle = document.querySelector(".hamburger");
                const menu = document.querySelector(".container-menu");

                toggle.addEventListener("click", () => {
                    menu.classList.toggle("active");
                });
            });







            window.onclick = function (event)
            {
                const modal = document.getElementById("modal");
                if (event.target === modal)
                    cerrarModal();
            }

        </script>
    </body>
</html>

