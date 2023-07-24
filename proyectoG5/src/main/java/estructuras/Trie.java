package estructuras;

/**
 *
 * @author Ariana
 */
public class Trie {
    private TrieNode root;
      
    //Constructor
    public Trie(){
        this.root = new TrieNode(' '); 
    }
    
    public void insert(String word) {
        TrieNode nodoActual = this.root;
        for (char caracter : word.toCharArray()) {
            TrieNode child = nodoActual.subNode(caracter);
            if (child != null) {
                nodoActual = child;
            } else {
                nodoActual.childList.add(new TrieNode(caracter));
                nodoActual = nodoActual.subNode(caracter);
            }
            nodoActual.count++;
        }
        nodoActual.setIsEnd(true);
    }
    
    public boolean search(String word){
        TrieNode nodoActual = this.root;
        for(char caracter : word.toCharArray()){
            if(nodoActual.subNode(caracter) == null){
                return false;
            }else{
                nodoActual = nodoActual.subNode(caracter);
            }
        }
        if(nodoActual.isIsEnd()){
            return true;
        }
        return false;
    }
    
    public void remove(String word){
        if(search(word) == false){
            System.out.println(word+", no existe en el Trie");
        }else{
            TrieNode nodoActual = this.root;
            for(char caracter : word.toCharArray()){
                TrieNode child = nodoActual.subNode(caracter);
                if(child.count == 1){
                    nodoActual.childList.remove(child);
                    return;
                }else{
                    child.count--;
                    nodoActual = child;
                }
            }
            nodoActual.setIsEnd(false);
        }
    }
}
