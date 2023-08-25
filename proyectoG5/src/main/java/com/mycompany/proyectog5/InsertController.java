package com.mycompany.proyectog5;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ariana
 */
public class InsertController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button insertarBtn;

    @FXML
    private TextField palabra;

    @FXML
    private TextArea significado;
    
    private String word;
    private String meaning;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public String getWord(){
        return word;
    }
    
    public String getMeaning(){
        return meaning;
    }
    //guarda en el trie la palabra
    @FXML
    public void guardar() throws IOException {
        word = palabra.getText().trim();
        meaning = significado.getText().trim();
        System.out.println("word: "+word);
        System.out.println("meaning: "+meaning);
        if (word != null && !word.isEmpty() && meaning != null && !meaning.isEmpty()) {
            // Primera letra mayuscula, resto minusculas
            String palabraModificada = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            String significadoModificado = meaning.substring(0, 1).toUpperCase() + meaning.substring(1).toLowerCase();

            // Insertar la palabra en el diccionario
            Diccionario.getDiccionario().insert(palabraModificada, significadoModificado);
            System.out.println("Se inserto la palabra?: "+Diccionario.getDiccionario().insert(word, meaning));
            BuscarController.setIsGuardado(false); //cuando se inserta una palabra, el boolean cambia a no guardado
            // Mostrar la alerta
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Inserción realizada");
            alerta.setHeaderText(null);
            alerta.setContentText("La palabra se ha insertado correctamente en el diccionario");
            alerta.showAndWait();

            Stage stage = (Stage) palabra.getScene().getWindow();
            stage.close();
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de Inserción");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, llene todos los campos");
            alerta.showAndWait();
        }
    }

    
    @FXML
    public void cancelar(ActionEvent event){
        // Obtener el botón "Cancelar" 
        Node source = (Node) event.getSource();
        // Obtener la escena que contiene el nodo (botón)
        Scene scene = source.getScene();
        // Obtener la ventana (Stage) asociada a la escena
        Stage stage = (Stage) scene.getWindow();
        // Cerrar la ventana
        stage.close();
    }

    
    
}