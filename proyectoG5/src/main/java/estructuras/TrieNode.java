package estructuras;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ariana
 */
public class TrieNode {
    private char caracter;
    private boolean isEnd; //es true cuando es una hoja
    int count;
    List<TrieNode> childList; //representa los enlaces de los nodos
    
    //Constructor
    public TrieNode(char caracter){
        childList = new LinkedList<>();
        isEnd = false;
        this.caracter = caracter;
        count = 0;
    }
    
    public TrieNode subNode(char caracter){
        if(childList != null){
            for(TrieNode child : childList){
                if(child.caracter == caracter){
                    return child;
                }
            }
        }
        return null;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TrieNode> getChildList() {
        return childList;
    }

    public void setChildList(List<TrieNode> childList) {
        this.childList = childList;
    }
    
}
