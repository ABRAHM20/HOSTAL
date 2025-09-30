package pe.edu.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
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
        AlquilerDao alquilerDao = new AlquilerDao();

        if ("ver".equals(accion)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Alquiler detalle = alquilerDao.obtenerDetalle(id);

                // Preparamos los atributos para que el JSP abra el modal con los datos
                request.setAttribute("detalle", detalle);
                request.setAttribute("modal", "ver");

            } catch (NumberFormatException e) {
                System.err.println("Error: ID para 'ver' no es un número válido. ID=" + request.getParameter("id"));
            }
        }
        
        // Al final, siempre cargamos los datos de la página principal y redirigimos
        cargarDatosYRedirigir(request, response, alquilerDao);
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
                String idHabStr = request.getParameter("id_hab");
                String fecha_inicio_str = request.getParameter("fecha_inicio");
                String fecha_fin_str = request.getParameter("fecha_salida");
                int horas = Integer.parseInt(request.getParameter("horas"));

                // La lógica de negocio se aplica en el servidor: un nuevo alquiler es siempre "check-in"
                String estado = "check-in";

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaLocalDateInicio = LocalDate.parse(fecha_inicio_str, formatter);
                LocalDate fechaLocalDateFin = LocalDate.parse(fecha_fin_str, formatter);

                java.sql.Date fechaSqlInicio = java.sql.Date.valueOf(fechaLocalDateInicio);
                java.sql.Date fechaSqlFin = java.sql.Date.valueOf(fechaLocalDateFin);

                HttpSession session = request.getSession();
                Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

                Alquiler a = new Alquiler();
                a.setHoras(horas);
                a.setFecha_inicio(fechaSqlInicio);
                a.setFecha_fin(fechaSqlFin);
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
                System.err.println("Error al crear nuevo alquiler: " + e.getMessage());
                e.printStackTrace();
            }
        } else if ("checkout".equals(accion)) {
            // Lógica para manejar el check-out desde el modal
            try {
                int id = Integer.parseInt(request.getParameter("id_alquiler"));
                dao.finalizar(id); // Este método debe poner el estado 'check-out' en la BD
            } catch (NumberFormatException e) {
                System.err.println("Error: ID para 'checkout' no es un número válido. ID=" + request.getParameter("id_alquiler"));
            }
        }
        
        // Usamos Post-Redirect-Get para evitar reenvíos de formulario
        response.sendRedirect("AlquilerController");
    }

    /**
     * Método auxiliar para cargar los datos necesarios para el JSP y realizar el forward.
     * Evita la duplicación de código en el método doGet.
     */
    private void cargarDatosYRedirigir(HttpServletRequest request, HttpServletResponse response, AlquilerDao alquilerDao)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

        // Cargar clientes para el modal de creación
        ClienteDao clienteDao = new ClienteDao();
        request.setAttribute("clientes", clienteDao.listar());
        
        // Cargar habitaciones disponibles para el modal de creación
        HabitacionDao habitacionDao = new HabitacionDao();
        request.setAttribute("habitaciones", habitacionDao.listarDisponibles());

        // Cargar lista de alquileres (filtrada o completa)
        String estadoFiltro = request.getParameter("estado");
        LinkedList<Alquiler> lista;

        if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
            lista = alquilerDao.listarPorEstado(estadoFiltro);
            request.setAttribute("estadoSeleccionado", estadoFiltro);
        } else {
            lista = alquilerDao.listar();
        }
        request.setAttribute("lista", lista);

        // Redirigir según el rol del usuario
        if (u != null && "A".equalsIgnoreCase(u.getRol())) {
            request.getRequestDispatcher("2-administrador/2-alquiler.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("3-recepcionista/2-alquiler.jsp").forward(request, response);
        }
    }
}
