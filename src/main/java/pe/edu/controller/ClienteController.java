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
import java.util.LinkedList;
import pe.edu.dao.ClienteDao;
import pe.edu.entity.Cliente;
import pe.edu.entity.Usuario;

@WebServlet(name = "ClienteController", urlPatterns = {"/ClienteController"})
public class ClienteController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ClienteDao dao = new ClienteDao();
        LinkedList<Cliente> lista = dao.listar();
        request.setAttribute("lista", lista);

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        

        if (usuario != null && "A".equalsIgnoreCase(usuario.getRol())) {
            request.getRequestDispatcher("2-administrador/3-cliente.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("3-recepcionista/3-cliente.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuario usuario1 = (Usuario) session.getAttribute("usuarioLogueado");

        if ("nuevo".equals(accion)) {
            try {
                String nombre = request.getParameter("nombre");
                String apellido=request.getParameter("apellido");
                String dni = request.getParameter("dni");
                String sexo = request.getParameter("sexo");

                Cliente c = new Cliente();
                c.setNombre(nombre);
                c.setApellido(apellido);
                c.setDni(dni);
                c.setSexo(sexo);
                c.setid_usuario(usuario1.getId());

                ClienteDao dao = new ClienteDao();
                dao.insertar(c);

                response.sendRedirect("ClienteController?mensaje=Cliente registrado");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("ClienteController?error=Error al registrar cliente");
            }
        } else {
            response.sendRedirect("ClienteController");
        }
    }
}

