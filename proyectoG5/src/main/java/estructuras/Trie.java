package estructuras;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

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
    
    //Inserta caracter por caracter al trie
    public boolean insert(String word, String significado){
        if(word== null || significado == null){
            return false;
        }
        TrieNode nodoActual = this.root;
        //se recorre cada caracter de la palabra a insertar
        for(char caracter : word.toCharArray()){
            // Se verifica si ya existe un nodo hijo para el carácter actual
            TrieNode child = nodoActual.getChild(caracter);
            if(child == null){
                nodoActual.addChild(caracter); //si no existe se agrega un nodo hijo a ese caracter
            }
            nodoActual = nodoActual.getChild(caracter);//se actualiza al nodo siguiente
        }
        nodoActual.setIsEnd(true); //como ya es una palabra, es true
        nodoActual.setSignificado(significado);//Cada palabra tiene su significado
        return true;
    }
    
    
    public boolean search(String word){
        TrieNode nodoActual = this.root;
        //se recorre cada caracter de esa palabra
        for(char caracter: word.toCharArray()){
            //se verifica si ya existe un nodo hijo para ese caracter
            nodoActual = nodoActual.getChild(caracter);
            if(nodoActual == null){
                return false; //significa que word no esta en el Trie
            }
        }
        //al terminar de recorrer la palabrar(estar al final) se retorna si es una palabra o no
        return nodoActual.isIsEnd();
    }
    
    public boolean remove(String word){
        TrieNode nodoActual = this.root;
        Stack<TrieNode> stack = new Stack<>(); //se almacenan los nodos
        for(char caracter: word.toCharArray()){
            //se almacena el nodo hijo del primer caracter de la palabra
            nodoActual = nodoActual.getChild(caracter);
            if(nodoActual == null){
                return false;
            }
            stack.push(nodoActual);
        }
        //si el nodoFinal no representa el final de la palabra...
        if(!nodoActual.isIsEnd()){
            return false; //significa que word no está en el Trie
        }
        
        //se marca la palabra como no final para proceder a eliminar la palabra del trie
        nodoActual.setIsEnd(false);
        
        while(!stack.isEmpty()){
            nodoActual = stack.pop(); //recuperamos
            //se verifica que no se tenga hijos y que no sea un nodo final (significa que el nodo se puede eliminar)
            if(nodoActual.getChildren().isEmpty() && !nodoActual.isIsEnd()){
                TrieNode padre;
                if (stack.isEmpty()) {
                    padre = this.root;
                } else {
                    padre = stack.peek();
                }
                char caracter = nodoActual.getCaracter(); //caracter del nodo actual
                padre.getChildren().remove(caracter); //se elimina el caracter y nodo del trie
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
    
    //metodo recursivo que recorre el subarbol apartir de un nodo, obteniendo todas las palabras
    private void buscarDesdeNodo(String prefijo, TrieNode nodo, List<String> sugerencias){
        //si el prefijo ya es una palabra se lo añade a la lista de sugerencias
        if(nodo.isIsEnd()){
            sugerencias.add(prefijo);
        }
        //si no, itera sobre los hijos del nodo
        for(TrieNode child: nodo.getChildren().values()){
            //buscar a partir del nuevo prefijo y nuevo hijo
            buscarDesdeNodo(prefijo + child.getCaracter(), child, sugerencias);
        }
    }

    public void clear() {
        this.root = new TrieNode('\0'); // se reinicia el trie
    }
    
    
    public List<String> getPalabras() {
        Set<String> palabras = new TreeSet<>();

        Stack<TrieNode> nodeStack = new Stack<>();
        Stack<String> wordStack = new Stack<>();

        nodeStack.push(this.root);
        wordStack.push("");

        while (!nodeStack.isEmpty()) {
            TrieNode nodoActual = nodeStack.pop();
            String palabraActual = wordStack.pop();

            if (nodoActual.isIsEnd()) {
                palabras.add(palabraActual);
            }

            for (TrieNode child : nodoActual.getChildren().values()) {
                nodeStack.push(child);
                wordStack.push(palabraActual + child.getCaracter());
            }
        }

        return new ArrayList<>(palabras); 
    }
    
    public List<String> buscarPorTerminacion(String terminacion) {
        List<String> sugerencias = new ArrayList<>();

        Queue<TrieNode> nodeQueue = new LinkedList<>();
        Queue<String> wordQueue = new LinkedList<>();

        nodeQueue.offer(this.root);
        wordQueue.offer("");

        while (!nodeQueue.isEmpty()) {
            TrieNode nodoActual = nodeQueue.poll();
            String palabraActual = wordQueue.poll();

            if (nodoActual.isIsEnd() && palabraActual.endsWith(terminacion)) {
                sugerencias.add(palabraActual);
            }
            //iteramos sobre los hijos 
            for (TrieNode child : nodoActual.getChildren().values()) {
                nodeQueue.offer(child);
                wordQueue.offer(palabraActual + child.getCaracter());
            }
        }

        return sugerencias;
    }

    
    public int getTotalWords() {
        int total = 0;

        Stack<TrieNode> nodeStack = new Stack<>();

        nodeStack.push(this.root);

        while (!nodeStack.isEmpty()) {
            TrieNode nodoActual = nodeStack.pop();

            if (nodoActual.isIsEnd()) {
                total++;
            }
            //iteramos sobre los hijos de ese nodo
            for (TrieNode child : nodoActual.getChildren().values()) {
                nodeStack.push(child);
            }
        }

        return total;
    }

    private int distanciaLevenshtein(String palabra1, String palabra2) {
        int m = palabra1.length();
        int n = palabra2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (palabra1.charAt(i - 1) == palabra2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }

        return dp[m][n];
    }

    public List<String> buscarAproximado(String consulta, int maxDistancia) {
        List<String> sugerencias = new ArrayList<>();

        Queue<TrieNode> nodeQueue = new LinkedList<>();
        Queue<String> wordQueue = new LinkedList<>();

        nodeQueue.offer(this.root);
        wordQueue.offer("");

        while (!nodeQueue.isEmpty()) {
            TrieNode nodoActual = nodeQueue.poll();
            String palabraActual = wordQueue.poll();

            if (nodoActual.isIsEnd() && palabraActual.length() == consulta.length() && distanciaLevenshtein(palabraActual, consulta) <= maxDistancia) {
                sugerencias.add(palabraActual);
            }

            for (TrieNode child : nodoActual.getChildren().values()) {
                nodeQueue.offer(child);
                wordQueue.offer(palabraActual + child.getCaracter());
            }
        }

        return sugerencias;
    }





}
