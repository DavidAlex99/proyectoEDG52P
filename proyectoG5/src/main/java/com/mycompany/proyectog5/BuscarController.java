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
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Ariana
 */
public class BuscarController implements Initializable {

    private Trie diccionario;

    @FXML
    private Button buscarBtn;

    @FXML
    private TextField busquedaTF;

    @FXML
    private Button eliminarBtn;

    @FXML
    private Button insertarBtn;

    @FXML
    private Label palabraBuscada;

    @FXML
    private StackPane root;

    @FXML
    private Label significado;

    @FXML
    private ListView<String> sugerenciasListView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        diccionario = new Trie();
        String ruta = "diccionario.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 2) {
                    String word = partes[0].trim();
                    String significado = partes[1].trim();
                    diccionario.insert(word, significado);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void searchWord(ActionEvent event) {
        String palabraRecuperada = busquedaTF.getText().trim();
        String word = palabraRecuperada.substring(0,1).toUpperCase() + palabraRecuperada.substring(1).toLowerCase();
        String significadoPalabra = diccionario.getSignificado(word);
        if (significadoPalabra != null) {
            palabraBuscada.setText(word);
            significado.setText(significadoPalabra);
        } else {
            palabraBuscada.setText("Palabra no encontrada");
            significado.setText("");

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Búsqueda");
            alerta.setHeaderText(null);
            alerta.setContentText("La palabra no se encontró en el diccionario");
            alerta.showAndWait(); // mostrar la alerta
        }
    }

    @FXML
    public void showSuggestion(KeyEvent event) {
        String palabraIngresada = busquedaTF.getText().trim();
        if(!palabraIngresada.isEmpty()){
            String word = palabraIngresada.substring(0, 1).toUpperCase() + palabraIngresada.substring(1).toLowerCase();
            List<String> sugerencias = diccionario.buscarPorPrefijo(word);
            sugerenciasListView.getItems().clear();
            sugerenciasListView.getItems().addAll(sugerencias);

            sugerenciasListView.setVisible(!sugerencias.isEmpty());
        }else{
            sugerenciasListView.getItems().clear();
            sugerenciasListView.setVisible(false);
        }
        
    }

    // Método para seleccionar una sugerencia de autocompletado
    @FXML
    public void selectSuggestion() {
        String selectedSuggestion = sugerenciasListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            busquedaTF.setText(selectedSuggestion);
            sugerenciasListView.setVisible(false);
        }
    }
}
