package pe.edu.controller;

// ¡IMPORTANTE! Asegúrate de importar la librería BCrypt
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.dao.UsuarioDao;
import pe.edu.entity.Usuario;

@WebServlet(name = "Autentica", urlPatterns = {"/Autentica"})
public class Autentica extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtenemos los datos del formulario (esto no cambia)
        String email = request.getParameter("email");
        String pswPlano = request.getParameter("psw"); 

        UsuarioDao udao = new UsuarioDao();
        Usuario usuarioDeLaBD = null;

        

            usuarioDeLaBD = udao.buscarPorEmail(email);
       

        
        if (usuarioDeLaBD != null && BCrypt.checkpw(pswPlano, usuarioDeLaBD.getPassword())) {
            

            
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuarioDeLaBD); 

            if ("A".equalsIgnoreCase(usuarioDeLaBD.getRol())) {
                response.sendRedirect("HabitacionController"); 
            } else {
                response.sendRedirect("HabitacionController"); 
            }
        } else {
            // Si el usuario es nulo O la contraseña no coincide, el login falla.
            response.sendRedirect("index.html?error=login_incorrecto");
        }
    }
}