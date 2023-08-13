package com.mycompany.proyectog5;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MenuController {

    @FXML
    private Button buscarBtn;

    @FXML
    private Button historialBtn;

    @FXML
    private Button juegoBtn;

    @FXML
    private Button marcadorBtn;

    @FXML
    private StackPane root;
    
    @FXML
    public void changeView(String fxmlName){
        try{
            Node view = FXMLLoader.load(getClass().getResource(fxmlName));
            root.getChildren().setAll(view);
        }catch(IOException ex){
            System.out.println("no se puedo cargar la vista: "+fxmlName);
        }
    }
    
    @FXML
    private void searchView(){
        changeView("buscar.fxml");
    }
    
    @FXML
    private void historialView(){
        changeView("historial.fxml");
    }
    
    @FXML
    private void bookmarkView(){
        changeView("bookmark.fxml");
    }
    
    @FXML
    private void gameView(){
        changeView("gameOptions.fxml");
    }
    
    
}
