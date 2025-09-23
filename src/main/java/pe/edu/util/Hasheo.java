/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.util;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Personal
 */
public class Hasheo {
    
       public static void main(String[] args) {

        String passwordPlano = "04022004";

        // Generamos el hash usando BCrypt
        String hash = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());

        // Ahora esta línea es válida porque está dentro de un método
        System.out.println("El hash generado es: " + hash);
        System.out.println("Longitud " + hash.length());
    }
    
}
