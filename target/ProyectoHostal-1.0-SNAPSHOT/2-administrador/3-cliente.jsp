<%-- 
    Document   : 3-cliente
    Created on : 7 jul. 2025, 6:00:31 a. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="pe.edu.entity.Cliente"%>
<%@page import="java.util.LinkedList"%>

<%
    LinkedList<Cliente> lista = (LinkedList<Cliente>) request.getAttribute("lista");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/1-estilos/estilos.css">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:FILL@1" rel="stylesheet" />
</head>
<body>
    <button class="hamburger">&#9776;</button>
    <%@include file="../navbarAdmin.jsp" %>

    <div class="titulo-contenido">
        <header class="barra-superior">
            <h2 class="titulo-seccion">GESTIONAR CLIENTES</h2>
            <div class="usuario">
                <span class="material-symbols-rounded">admin_panel_settings</span>
                Administrador
            </div>
        </header>
    </div>

    <div class="tarjeta">
        <div class="acciones-superiores">
            <button class="btn-crear" onclick="abrirModalDesdeTemplate('modal-crear-cliente')">
                <span class="material-symbols-rounded">add</span>Registrar cliente
            </button>
        </div>

        <div class="contenido">
            <div class="tabla-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>DNI</th>
                            <th>Sexo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (lista != null && !lista.isEmpty()) {
                            for (Cliente c : lista) { %>
                        <tr>
                            <td><%= c.getId_cliente() %></td>
                            <td><%= c.getNombre() %></td>
                            <td><%= c.getApellido() %></td>
                            <td><%= c.getDni() %></td>
                            <td><%= c.getSexo() %></td>
                        </tr>
                        <% }
                        } else { %>
                        <tr><td colspan="5">No hay clientes registrados.</td></tr>
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
    <template id="modal-crear-cliente">
        <form class="formulario-modal" method="post" action="<%= request.getContextPath() %>/ClienteController">
            <h3>Registrar cliente</h3>
            <input type="hidden" name="accion" value="nuevo">

            <label>Nombre:</label>
            <input type="text" name="nombre" required><br>
            
            <label>Apellido:</label>
            <input type="text" name="apellido" required><br>

            <label>DNI:</label>
            <input type="text" name="dni" pattern="[0-9]{8}" required><br>

            <label>Sexo:</label><br>
            <select name="sexo" required>
                <option value="M">Masculino</option>
                <option value="F">Femenino</option>
            </select><br>
            
            

            <button type="submit">Guardar</button>
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

        window.onclick = function (event) {
            const modal = document.getElementById("modal");
            if (event.target === modal)
                cerrarModal();
        }
    </script>
</body>
</html>
