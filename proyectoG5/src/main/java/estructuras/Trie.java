package estructuras;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Ariana
 */
public class Trie {
    private TrieNode root;
      
    //Constructor
    public Trie(){
        this.root = new TrieNode('\0'); //caracter nulo
    }
    
    public boolean insert(String word, String significado){
        if(word== null || significado == null){
            return false;
        }
        TrieNode nodoActual = this.root;
        for(char caracter : word.toCharArray()){
            TrieNode child = nodoActual.getChild(caracter);
            if(child == null){
                nodoActual.addChild(caracter);
            }
            nodoActual = nodoActual.getChild(caracter);
        }
        nodoActual.setIsEnd(true);
        nodoActual.setSignificado(significado);//Cada palabra tiene su significado
        return true;
    }
    
    public boolean search(String word){
        TrieNode nodoActual = this.root;
        for(char caracter: word.toCharArray()){
            nodoActual = nodoActual.getChild(caracter);
            if(nodoActual == null){
                return false; //significa que word no esta en el Trie
            }
        }
        return nodoActual.isIsEnd();
    }
    
    public boolean remove(String word){
        TrieNode nodoActual = this.root;
        Stack<TrieNode> stack = new Stack<>();
        for(char caracter: word.toCharArray()){
            nodoActual = nodoActual.getChild(caracter);
            if(nodoActual == null){
                return false;
            }
            stack.push(nodoActual);
        }
        if(!nodoActual.isIsEnd()){
            return false; //significa que word no está en el Trie
        }
        nodoActual.setIsEnd(false);
        
        while(!stack.isEmpty()){
            nodoActual = stack.pop(); //recuperamos
            if(nodoActual.getChildren().isEmpty() && !nodoActual.isIsEnd()){
                TrieNode padre;
                if (stack.isEmpty()) {
                    padre = this.root;
                } else {
                    padre = stack.peek();
                }
                char caracter = nodoActual.getCaracter();
                padre.getChildren().remove(caracter);
            }
        }
        return true;
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }
    
    public String getSignificado(String palabra) {
        TrieNode nodoActual = this.root;
        for (char caracter : palabra.toCharArray()) {
            nodoActual = nodoActual.getChild(caracter);
            if (nodoActual == null) {
                return null; // La palabra no está en el Trie
            }
        }
        return nodoActual.getSignificado(palabra); // Devuelve el significado de la palabra
    }
    
    /*
    Muestra la lista de sugerencias de palabras conforme se ingresa prefijos
    */
    public List<String> buscarPorPrefijo(String prefijo){
        List<String> sugerencias = new ArrayList<>();
        TrieNode nodoActual = this.root;
        for(char caracter: prefijo.toCharArray()){
            nodoActual = nodoActual.getChild(caracter);
            if(nodoActual == null){
                return sugerencias;
            }
        }
        buscarDesdeNodo(prefijo, nodoActual, sugerencias);
        return sugerencias;
    }
    
    /*
    metodo recursivo que recorre el subarbol apartir de un nodo, obteniendo todas las palabras
    */
    private void buscarDesdeNodo(String prefijo, TrieNode nodo, List<String> sugerencias){
        if(nodo.isIsEnd()){
            sugerencias.add(prefijo);
        }
        for(TrieNode child: nodo.getChildren().values()){
            buscarDesdeNodo(prefijo + child.getCaracter(), child, sugerencias);
        }
    }

    public void clear() {
        this.root = new TrieNode('\0'); // se reinicia el trie
    }

}
