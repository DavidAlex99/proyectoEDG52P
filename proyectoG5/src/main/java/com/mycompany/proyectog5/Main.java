package com.mycompany.proyectog5;

import estructuras.Trie;

/**
 *
 * @author Ariana
 */
public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();

        // Palabras con prefijos comunes para insertar con sus significados
        trie.insert("preparar", "Hacer los arreglos necesarios para algo.");
        trie.insert("prepara", "Hacer los arreglos iniciales para algo.");
        trie.insert("preparación", "Acción y efecto de preparar o prepararse.");
        trie.insert("preparado", "Listo o dispuesto para algo.");
        trie.insert("preparador", "Que prepara.");

        // Otras palabras para insertar con sus significados
        trie.insert("manzana", "Fruto comestible de forma redonda o acorazonada.");
        trie.insert("banana", "Fruto alargado de piel gruesa que se pela.");
        trie.insert("computadora", "Máquina electrónica para procesar y almacenar información.");
        trie.insert("teléfono", "Dispositivo para transmitir sonidos a distancia.");
        trie.insert("automóvil", "Vehículo de cuatro ruedas propulsado por un motor.");

        // Buscar palabras en el Trie
        System.out.println("¿La palabra 'preparar' está en el Trie? " + trie.search("preparar")); // Debería imprimir true
        System.out.println("¿La palabra 'preparándose' está en el Trie? " + trie.search("preparándose")); // Debería imprimir false

        // Obtener significado de palabras
        System.out.println("Significado de 'manzana': " + trie.getSignificado("manzana"));
        System.out.println("Significado de 'preparación': " + trie.getSignificado("preparación"));
        System.out.println("Significado de 'computadora': " + trie.getSignificado("computadora"));

        // Eliminar palabras del Trie
        System.out.println("Eliminar 'banana' del Trie: " + trie.remove("banana")); // Debería imprimir true
        System.out.println("¿La palabra 'banana' está en el Trie después de eliminarla? " + trie.search("banana")); // Debería imprimir false
    }

}
