package estructuras;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ariana
 */
public class TrieNode {
    private char caracter;
    private boolean isEnd; //es true cuando es una hoja
    private Map<Character, TrieNode> children;
    
    //Constructor
    public TrieNode(char caracter){
        this.caracter = caracter;
        this.isEnd = false;
        this.children = new HashMap<>();
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
}
