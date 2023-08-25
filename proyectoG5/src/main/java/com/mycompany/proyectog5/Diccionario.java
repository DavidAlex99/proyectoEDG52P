package com.mycompany.proyectog5;

import estructuras.Trie;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Ariana
 */
public class Diccionario {
    private static Trie diccionario = new Trie();
    private static String nombreDiccionario = "Diccionario General.txt";
    public static Trie getDiccionario() {
        return diccionario;
    }
    
    public static void cargarDiccionario() {
        diccionario = new Trie();
        System.out.println("nombre: "+Diccionario.nombreDiccionario);
        String rutaDiccionarios = System.getProperty("user.dir") + "/Diccionarios/"+Diccionario.nombreDiccionario;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaDiccionarios))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 2) {
                    String word = partes[0].trim();
                    String significado = partes[1].trim();
                    diccionario.insert(word, significado);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void setNombre(String nombre){
        Diccionario.nombreDiccionario = nombre;
    }

}
