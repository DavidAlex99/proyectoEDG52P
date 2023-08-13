/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectog5;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Ariana
 */
public class GameOptionsController implements Initializable {
    @FXML
    private Button jugarBtn;

    @FXML
    private ComboBox<Integer> longitudCb;

    @FXML
    private ComboBox<Integer> tiempoCb;
    
    @FXML
    private ComboBox<String> categoriaCb;
    
    @FXML
    private BorderPane root;
    
    private List<Integer> tiempo;
    private List<Integer> longitud;
    private List<String> categorias;
    
    private static int tiempoConfigurado;
    private static int longitudConfigurada;
    private static String categoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tiempo = new ArrayList<>();
        tiempo.add(30);
        tiempo.add(40);
        tiempo.add(60);
        tiempo.add(70);
        tiempo.add(80);
        tiempo.add(90);
        tiempo.add(100);
        tiempo.add(110);
        tiempo.add(120);
        
        longitud = new ArrayList<>();
        longitud.add(4);
        longitud.add(5);
        longitud.add(6);
        longitud.add(7);
        
        categorias = new ArrayList<>();
        categorias.add("General");
        categorias.add("Marketing");
        categorias.add("Tecnologico");
        categorias.add("Moda");

        categoriaCb.getItems().addAll(categorias);
        tiempoCb.getItems().addAll(tiempo);
        longitudCb.getItems().addAll(longitud);
    }    
    
    public int getSelectedTime(){
        return tiempoCb.getValue();
    }
    
    public int getSelectedlength(){
        return longitudCb.getValue();
    }
    
    @FXML
    private void jugar(ActionEvent event){
        //Se verifica que se hayan seleccionado las opciones en el cb
        if(tiempoCb.getValue() != null && longitudCb.getValue() != null && categoriaCb.getValue() != null){
            GameOptionsController.tiempoConfigurado = tiempoCb.getValue();
            GameOptionsController.longitudConfigurada = longitudCb.getValue();
            GameOptionsController.categoria = categoriaCb.getValue();
            Diccionario.setNombre(getCategoria());
            changeView("game.fxml");
            
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione las opciones del juego");
            alert.showAndWait();
        }  
    }
    
    // Getter para obtener el tiempo configurado
    public static int getTiempoConfigurado() {
        return tiempoConfigurado;
    }

    // Getter para obtener la longitud configurada
    public static int getLongitudConfigurada() {
        return longitudConfigurada;
    }
    
    public static String getCategoria(){
        System.out.println("Diccionario "+categoria+".txt");
        return "Diccionario "+categoria+".txt";
    }
    
    @FXML
    public void changeView(String fxmlName){
        try{
            Node view = FXMLLoader.load(getClass().getResource(fxmlName));
            root.getChildren().setAll(view);
        }catch(IOException ex){
            System.out.println("no se puedo cargar la vista: "+fxmlName);
        }
    }
}
