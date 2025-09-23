<%-- 
    Document   : navbarRecep
    Created on : 27 jun. 2025, 11:32:20 p. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Botón hamburguesa visible solo en móvil -->
<button class="hamburger" onclick="toggleMenu()">
    <span class="material-symbols-rounded">menu</span>
</button>
<div class="container-menu">
    <nav>
        <h1>Hostal Mario</h1><br>
        <a href="<%= request.getContextPath() %>/HabitacionController"><span class="material-symbols-rounded">king_bed</span>Habitaciones</a> <br>           
        <a href="<%= request.getContextPath() %>/ClienteController"><span class="material-symbols-rounded">groups</span>Clientes</a><br>
        <a href="<%= request.getContextPath() %>/AlquilerController"><span class="material-symbols-rounded">key</span>Alquileres</a><br>
        <a href="<%= request.getContextPath() %>/PagoController"><span class="material-symbols-rounded">Payments</span>Pagos</a><br>
    </nav>
    <div class="logout">
        <a href="<%= request.getContextPath() %>/index.html"><span class="material-symbols-rounded">logout</span> Cerrar sesión</a>
    </div>
</div>

