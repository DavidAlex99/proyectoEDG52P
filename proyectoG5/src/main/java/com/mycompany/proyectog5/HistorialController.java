/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyectog5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Ariana
 */
public class HistorialController implements Initializable {

    @FXML
    private TextArea paneLetras;
    @FXML
    private TextArea paneHistograma;
    @FXML
    private Label lblConteo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Hi");
        showStatistics();
    }    
    
    private void showStatistics() {
        String ruta = "Diccionario General.txt";

        int totalWords = 0;
        //mismo indice e los nuemro n las letras del abecedario
        int[] wordsPerLetter = new int[26]; 
        Map<Integer, Integer> histograma = new HashMap<>();
        String resultWordsLetter = " ";
        String resultHistograma = " ";

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.contains(":")) {
                    totalWords++;

                    char firstChar = Character.toLowerCase(linea.charAt(0));
                    if (firstChar >= 'a' && firstChar <= 'z') {
                        wordsPerLetter[firstChar - 'a']++;
                    }
                    /*
                    Se realiza un SPLIT en cada linea, actuando como separador :, y al primer elemento se le hace un length
                    */
                    int wordLength = linea.split(":")[0].length();
                    //Se anade al diccionario. WSi no existe se agrega, si existe se adiciona
                    histograma.put(wordLength, histograma.getOrDefault(wordLength, 0) + 1);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Mostrar resultados
        System.out.println("Total de palabras: " + totalWords);
        for (int i = 0; i < 26; i++) {
            System.out.printf("Letra %c: %d palabras%n", (char) ('A' + i), wordsPerLetter[i]);
            resultWordsLetter += "Letra " + (char) ('A' + i) + " : " + wordsPerLetter[i] + " palabras\n";
            paneLetras.appendText(resultWordsLetter);
        }
        System.out.println("Todo el resultado: ");
        System.out.println(resultWordsLetter);

        System.out.println("\nHistograma de longitudes:");
        for (Map.Entry<Integer, Integer> entry : histograma.entrySet()) {
            System.out.println("Longitud " + entry.getKey() + ": " + entry.getValue() + "palabras\n");
            resultHistograma += "Longitud " + entry.getKey() + ": " + entry.getValue() + "palabras\n";
            paneHistograma.appendText(resultHistograma);
        }
        System.out.println("Todo el resultado: ");
        System.out.println(resultHistograma);
        
        lblConteo.setText(totalWords + " Palabras");
    }
    
}
