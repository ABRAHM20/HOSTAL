<%-- 
    Document   : prueba
    Created on : 19 abr. 2025, 5:41:12 p. m.
    Author     : Estudiante
--%>

<%@page import="pe.edu.util.Conexion" %>
<%@page import="java.sql.Connection" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Prueba de conexión</title>
</head>
<body>
    <h1>Prueba de conexión</h1>

    <%
        String estado = "";
        try {
            Connection cnx = Conexion.getInstancia().getConexion();
            if (cnx != null) {
                estado = "✅ Conexión establecida con éxito.";
            } else {
                estado = "❌ Error: conexión nula.";
            }
        } catch (Exception e) {
            estado = "❌ Excepción: " + e.getMessage();
        }
    %>

    <p><strong>Estado de conexión:</strong> <%= estado %></p>
</body>
</html>
