/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectog5;

import estructuras.Trie;

/**
 *
 * @author Ariana
 */
public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();

        // Palabras con prefijos comunes para insertar
        trie.insert("preparar");
        trie.insert("prepara");
        trie.insert("preparación");
        trie.insert("preparado");
        trie.insert("preparador");

        // Otras palabras para insertar
        trie.insert("manzana");
        trie.insert("banana");
        trie.insert("computadora");
        trie.insert("teléfono");
        trie.insert("automóvil");

        // Prueba del método search
        System.out.println("Buscando 'preparar': " + trie.search("preparar")); // Debe imprimir true
        System.out.println("Buscando 'computadora': " + trie.search("computadora")); // Debe imprimir true
        System.out.println("Buscando 'manzana': " + trie.search("manzana")); // Debe imprimir true
        System.out.println("Buscando 'perro': " + trie.search("perro")); // Debe imprimir false

        // Prueba del método remove
        trie.remove("prepara");
        trie.remove("manzana");

        System.out.println("Buscando 'prepara' después de eliminarlo: " + trie.search("prepara")); // Debe imprimir false
        System.out.println("Buscando 'manzana' después de eliminarlo: " + trie.search("manzana")); // Debe imprimir false
        System.out.println("Buscando 'preparado' después de eliminar 'prepara': " + trie.search("preparado")); // Debe imprimir true
        System.out.println("Buscando 'computadora' después de eliminar 'manzana': " + trie.search("computadora")); // Debe imprimir true

        // Prueba del método insert nuevamente
        trie.insert("gato");
        trie.insert("galleta");
        System.out.println("Buscando 'gato' después de insertarlo nuevamente: " + trie.search("gato")); // Debe imprimir true
        System.out.println("Buscando 'galleta' después de insertarlo nuevamente: " + trie.search("galleta")); // Debe imprimir true
    }
}
