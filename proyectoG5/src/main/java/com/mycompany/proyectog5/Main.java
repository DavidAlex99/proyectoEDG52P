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

        // Buscar palabras en el Trie
        System.out.println("Buscando palabras en el Trie:");
        System.out.println("Buscar 'preparar': " + trie.search("preparar")); // true
        System.out.println("Buscar 'preparado': " + trie.search("preparado")); // true
        System.out.println("Buscar 'preparación': " + trie.search("preparación")); // true
        System.out.println("Buscar 'prepar': " + trie.search("prepar")); // false (no es palabra completa)

        System.out.println("Buscar 'manzana': " + trie.search("manzana")); // true
        System.out.println("Buscar 'banana': " + trie.search("banana")); // true
        System.out.println("Buscar 'computadora': " + trie.search("computadora")); // true
        System.out.println("Buscar 'teléfono': " + trie.search("teléfono")); // true
        System.out.println("Buscar 'automóvil': " + trie.search("automóvil")); // true

        // Eliminar palabras del Trie
        System.out.println("\nEliminando palabras del Trie:");
        System.out.println("Eliminar 'preparado': " + trie.remove("preparado")); // true
        System.out.println("Buscar 'preparado': " + trie.search("preparado")); // false (ya fue eliminada)

        System.out.println("Eliminar 'manzana': " + trie.remove("manzana")); // true
        System.out.println("Buscar 'manzana': " + trie.search("manzana")); // false (ya fue eliminada)

        System.out.println("Eliminar 'banana': " + trie.remove("banana")); // true
        System.out.println("Buscar 'banana': " + trie.search("banana")); // false (ya fue eliminada)

        System.out.println("Eliminar 'computadora': " + trie.remove("computadora")); // true
        System.out.println("Buscar 'computadora': " + trie.search("computadora")); // false (ya fue eliminada)

        System.out.println("Eliminar 'teléfono': " + trie.remove("teléfono")); // true
        System.out.println("Buscar 'teléfono': " + trie.search("teléfono")); // false (ya fue eliminada)

        System.out.println("Eliminar 'automóvil': " + trie.remove("automóvil")); // true
        System.out.println("Buscar 'automóvil': " + trie.search("automóvil")); // false (ya fue eliminada)
    }

}
