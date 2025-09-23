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
import pe.edu.dao.HabitacionDao;
import pe.edu.entity.Habitacion;
import pe.edu.entity.Usuario;

@WebServlet(name = "HabitacionController", urlPatterns = {"/HabitacionController"})
public class HabitacionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pagina = request.getParameter("pagina");
        String id_hab = request.getParameter("id");

        HabitacionDao dao = new HabitacionDao();

        
        if ("leer".equals(pagina) && id_hab != null) {
            Habitacion h = dao.leer(Integer.parseInt(id_hab));
            request.setAttribute("habitacion", h);
            request.getRequestDispatcher("habitacion/leer.jsp").forward(request, response);
            return;
        } else if ("editar".equals(pagina) && id_hab != null) {
            Habitacion h = dao.leer(Integer.parseInt(id_hab));
            request.setAttribute("habitacion", h);
            request.getRequestDispatcher("habitacion/editar.jsp").forward(request, response);
            return;
        }

        //carga la lista siempre
        String estado = request.getParameter("estado");
        System.out.println(">>> Estado recibido: " + estado);
        
       
        LinkedList<Habitacion> lista=dao.listarDisponibles();
        LinkedList<Habitacion> lista1;
        request.setAttribute("lista", lista);
        
        


        if (estado != null && !estado.isEmpty()) {
            lista1 = dao.listarPorEstado(estado.trim().toLowerCase());
            // Método con stored procedure

            request.setAttribute("estadoSeleccionado", estado);
        } else {
            lista1 = dao.listar();
        }
        
        request.setAttribute("lista", lista1);
        

        

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null && "A".equalsIgnoreCase(usuario.getRol())) {
            request.getRequestDispatcher("2-administrador/1-habitaciones.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("3-recepcionista/1-habitaciones.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HabitacionDao dao = new HabitacionDao();
        Habitacion h = new Habitacion();
        String accion = request.getParameter("accion");

        int id_hab = 0;
        if ("editar".equals(accion) || "eliminar".equals(accion)) {
            try {
                id_hab = Integer.parseInt(request.getParameter("id_hab"));
            } catch (NumberFormatException e) {
                Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, "ID de habitación inválido", e);
            }
        }

        if (!"eliminar".equals(accion)) {
            int id_tipo_hab = Integer.parseInt(request.getParameter("id_tipo_hab"));
            int num_hab = Integer.parseInt(request.getParameter("num_hab"));
            String estado = request.getParameter("estado");

            h.setId_tipo_hab(id_tipo_hab);
            h.setNum_hab(num_hab);
            h.setEstado(estado);
        }

        switch (accion) {
            case "nuevo":
                HttpSession session = request.getSession(false);
                Usuario usuarioLogeado = (Usuario) session.getAttribute("usuarioLogueado");
                h.setId_usuario(usuarioLogeado.getId()); // Asignamos el ID
                dao.insertar(h);
                break;
            case "editar":
                h.setId_hab(id_hab);
                dao.editar(h);
                break;
            case "eliminar":
                dao.eliminar(id_hab);
                break;
        }

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        //request.getRequestDispatcher("HabitacionController").forward(request, response);
        //response.sendRedirect("HabitacionController?pagina=listar");
        response.sendRedirect("HabitacionController");

        /*if (usuario != null && "admin".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect("2-administrador/1-habitaciones.jsp");
        } else {
            response.sendRedirect("3-recepcionista/1-habitaciones.jsp");
        }*/
    }
}
