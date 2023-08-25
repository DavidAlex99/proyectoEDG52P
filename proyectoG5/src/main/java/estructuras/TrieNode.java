package estructuras;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Ariana
 */
public class TrieNode implements Comparable<TrieNode>{
    private char caracter;
    private boolean isEnd; //es true cuando es una hoja
    private Map<Character, TrieNode> children; //almacena los nodos hijos
    private String significado;
    
    //Constructor
    public TrieNode(char caracter){
        this.caracter = caracter;
        this.isEnd = false;
        this.children = new TreeMap<>();
        this.significado = null;
    }

    public char getCaracter() {
        return caracter;
    }

    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    public boolean isIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, TrieNode> children) {
        this.children = children;
    }
    
    public TrieNode getChild(char caracter){
        return this.children.get(caracter);
    }
    
    public void addChild(char caracter){
        this.children.put(caracter, new TrieNode(caracter));
    }

    public String getSignificado(String word) {
    if (this.isIsEnd()) {
        return this.significado;
    } else {
        return null;
    }
}

    public void setSignificado(String significado) {
        this.significado = significado;
    }

    @Override
    public int compareTo(TrieNode other) {
        return this.caracter - other.caracter;
    }
    
}
