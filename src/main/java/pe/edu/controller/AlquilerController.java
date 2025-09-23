/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.controller;

/**
 *
 * @author LENOVO
 */
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.List;
import pe.edu.dao.AlquilerDao;
import pe.edu.dao.ClienteDao;
import pe.edu.dao.HabitacionDao;
import pe.edu.entity.Alquiler;
import pe.edu.entity.Cliente;
import pe.edu.entity.Habitacion;
import pe.edu.entity.Usuario;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "AlquilerController", urlPatterns = {"/AlquilerController"})
public class AlquilerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        AlquilerDao dao = new AlquilerDao();


        if("activar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.activar(id);
        } else if ("finalizar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.finalizar(id);
        } else if ("ver".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Alquiler detalle = dao.obtenerDetalle(id);
            LinkedList<Alquiler> lista = dao.listar();

            request.setAttribute("detalle", detalle);
            request.setAttribute("modal", "ver"); //para abrir modal
            request.setAttribute("lista", lista);

            HttpSession session = request.getSession();
            Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

            if (u != null && "A".equalsIgnoreCase(u.getRol())) {
                request.getRequestDispatcher("2-administrador/2-alquiler.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("3-recepcionista/2-alquiler.jsp").forward(request, response);
            }
            return;
        }
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        HabitacionDao habitacionDao = new HabitacionDao();
        List<Habitacion> habitacionesDisponibles = habitacionDao.listarDisponibles();
        request.setAttribute("habitaciones", habitacionesDisponibles);

        String estado = request.getParameter("estado");
        LinkedList<Alquiler> lista;

        if (estado != null && !estado.isEmpty()) {
            lista = dao.listarPorEstado(estado);
            request.setAttribute("estadoSeleccionado", estado);
        } else {
            lista = dao.listar();
        }

        request.setAttribute("lista", lista);

        if (u != null && "A".equalsIgnoreCase(u.getRol())) {
            request.getRequestDispatcher("2-administrador/2-alquiler.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("3-recepcionista/2-alquiler.jsp").forward(request, response);
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        AlquilerDao dao = new AlquilerDao();
        

        if ("nuevo".equals(accion)) {
            try {
                
             
                int id_cliente = Integer.parseInt(request.getParameter("id_cliente"));
                int id_hab = Integer.parseInt(request.getParameter("id_hab"));
                String fecha_inicio = request.getParameter("fecha_inicio");
                String fecha_fin= request.getParameter("fecha_salida");
                int horas = Integer.parseInt(request.getParameter("horas"));
                String estado=request.getParameter("estado");
                if("1".equals(estado)){
                     //Hay que modificar el estado de la reserva 
                     estado="check-in";
                }
                else{
                
                     estado="check-out";
                }
                
                DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaLocalDate = LocalDate.parse(fecha_inicio, formateador);
                LocalDate fechaLocalfin=LocalDate.parse(fecha_fin,formateador);
                    
                    
                java.sql.Date fechaSql = java.sql.Date.valueOf(fechaLocalDate);
                java.sql.Date fechaSql1 = java.sql.Date.valueOf(fechaLocalfin);
                
                HttpSession session = request.getSession();
                Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
                
                Alquiler a = new Alquiler();
                a.setHoras(horas);
                a.setFecha_inicio(fechaSql);
                a.setFecha_fin(fechaSql1);
                a.setEstado(estado);
                a.setId_user(u.getId());
                
               

                Cliente c = new Cliente();
                c.setId_cliente(id_cliente);
                
                a.setCliente(c);

                Habitacion h = new Habitacion();
                h.setId_hab(id_hab);
                a.setHabitacion(h);

                dao.insertar(a);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        response.sendRedirect("AlquilerController");       
    }
}
