package estructuras;

import java.util.Stack;

/**
 *
 * @author Ariana
 */
public class Trie {
    private TrieNode root;
      
    //Constructor
    public Trie(){
        this.root = new TrieNode('\0'); 
    }
    
    public void insert(String word){
        TrieNode nodoActual = this.root;
        for(char caracter : word.toCharArray()){
            TrieNode child = nodoActual.getChild(caracter);
            if(child == null){
                nodoActual.addChild(caracter);
            }
            nodoActual = nodoActual.getChild(caracter);
        }
        nodoActual.setIsEnd(true);
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
    
}
