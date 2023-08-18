/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectog5;

import estructuras.Trie;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Ariana
 */
public class EstadisticasController implements Initializable {

    @FXML
    private TextArea paneLetras;
    @FXML
    private TextArea paneHistograma;
    @FXML
    private Label lblConteo;
    @FXML
    private ComboBox<String> diccionarioCb;
    @FXML
    private Button calcular;
    
    @FXML
    private TableView<String[]> letrasTableView;
    @FXML
    private TableColumn<String[], String> letraColumn;
    @FXML
    private TableColumn<String[], String> cantidadPalabrasColumn;

    @FXML
    private TableView<String[]> longitudesTableView;
    @FXML
    private TableColumn<String[], String> longitudColumn;
    @FXML
    private TableColumn<String[], String> cantidadLongitudColumn; //cantidadLongitudColumn
    @FXML
    private Button backBtn;
    
    private List<String> diccionarios;
    private Trie diccionario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Hi");
        diccionarios = new ArrayList<>();
        diccionarios.add("General");
        diccionarios.add("Marketing");
        diccionarios.add("Tecnologico");
        diccionarios.add("Moda");
        
        diccionarioCb.getItems().addAll(diccionarios);
        
        letraColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        cantidadPalabrasColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        longitudColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        cantidadLongitudColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        

    }    
    

    private void showStatistics() {
        int totalWords = diccionario.getTotalWords();
        int[] wordsPerLetter = new int[26];
        Map<Integer, Integer> histograma = new HashMap<>();

        for (String palabra : diccionario.getPalabras()) {
            char firstChar = Character.toLowerCase(palabra.charAt(0));
            if (firstChar >= 'a' && firstChar <= 'z') {
                wordsPerLetter[firstChar - 'a']++;
            }

            int wordLength = palabra.length();
            histograma.put(wordLength, histograma.getOrDefault(wordLength, 0) + 1);
        }

        Platform.runLater(() -> {
            letrasTableView.getItems().clear();
            System.out.println("Letras");
            for (int i = 0; i < 26; i++) {
                char letra = (char) ('A' + i);
                letrasTableView.getItems().add(new String[]{String.valueOf(letra), String.valueOf(wordsPerLetter[i])});
                System.out.println(Arrays.toString(new String[]{String.valueOf(letra), String.valueOf(wordsPerLetter[i])}));

            }
            longitudesTableView.getItems().clear();
            System.out.println("Longitudes");
            for (Map.Entry<Integer, Integer> entry : histograma.entrySet()) {
                String key = String.valueOf(entry.getKey());
                String value = String.valueOf(entry.getValue());
                longitudesTableView.getItems().add(new String[]{key, value});
                System.out.println("[" + key + ", " + value + "]");
            }

            letraColumn.setText("Letra");
            cantidadPalabrasColumn.setText("Palabras");

            longitudColumn.setText("Longitud");
            cantidadLongitudColumn.setText("Palabras");

            lblConteo.setText(totalWords + " Palabras");
        });
    }

    private void loadDictionary() {
        diccionario = new Trie();
        Diccionario.cargarDiccionario();
        diccionario = Diccionario.getDiccionario();
    }

    @FXML
    private void calcularEstadisticas(ActionEvent event) {
        Diccionario.setNombre("Diccionario "+diccionarioCb.getValue()+".txt");
        System.out.println("Diccionario: "+diccionarioCb.getValue());
        loadDictionary(); // Cargar el diccionario seleccionado
        //paneLetras.clear();
        //paneHistograma.clear();
        showStatistics();
    }
    
    @FXML
    private void cambiar() throws IOException {
        App.setRoot("menu");
    }

}
