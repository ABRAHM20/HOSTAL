/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pe.edu.dao;

import java.util.LinkedList;

/**
 *
 * @author LENOVO
 
 */
public interface DaoCrud <T>{
    public LinkedList<T> listar();
    public void insertar(T obj);
    public T leer(int id);
    public void editar(T obj);
    public void eliminar(int id);
    
}
