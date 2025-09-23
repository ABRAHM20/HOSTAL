/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import pe.edu.entity.Pago;
import java.text.SimpleDateFormat;

public class GeneradorBoleta {

    public static String generarBoleta(Pago p) {
        String carpeta = "boletas";
        File dir = new File(carpeta);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String tipo = p.getTipo_comprobante().toUpperCase(); //BOLETA - FACTURA
        String nombreArchivo = tipo + "_PAGO_" + p.getId_pago() + ".txt";
        String rutaCompleta = carpeta + File.separator + nombreArchivo;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            writer.write("=========================================\n");
            writer.write("       " + tipo + " DE PAGO - HOSTAL MARIO\n");
            writer.write("=========================================\n");
            writer.write("ID de pago     : " + p.getId_pago() + "\n");
            writer.write("Fecha de pago  : " + sdf.format(p.getFecha_pago()) + "\n");
            writer.write("Método de pago : " + p.getMetodo_pago() + "\n");
            writer.write("-----------------------------------------\n");
            writer.write("Cliente        : " + p.getAlq().getCliente().getNombre() + "\n");
            writer.write("DNI            : " + p.getAlq().getCliente().getDni() + "\n");

            if ("FACTURA".equalsIgnoreCase(tipo)) {
                writer.write("RUC            : " + p.getRuc() + "\n");
            }

            writer.write("-----------------------------------------\n");
            writer.write("Habitación     : N°" + p.getAlq().getHabitacion().getNum_hab()
                          + " (" + p.getAlq().getHabitacion().getTipo_nombre() + ")\n");
            writer.write("Horas          : " + p.getAlq().getHoras() + "\n");
            writer.write("Total a pagar  : S/ " + p.getAlq().getTotal() + "\n");
            writer.write("Pagado         : S/ " + p.getMonto_pagado() + "\n");
            writer.write("Vuelto         : S/ " + p.getVuelto() + "\n");
            writer.write("=========================================\n");
            writer.write("Gracias por su preferencia. ¡Vuelva pronto!\n");

            return rutaCompleta;

        } catch (IOException e) {
            System.out.println("Error al generar boleta: " + e.getMessage());
            return null;
        }
    }
}


