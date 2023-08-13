package com.mycompany.proyectog5;

import estructuras.Trie;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GameController implements Initializable {
    private List<String> palabrasJuego;
    private Trie diccionario;
    private List<Character> combinacionLista;
    private int score;
    private Trie palabras;
    private int tiempoConfigurado;
    private int longitudConfigurada;
    
    @FXML
    private Button buscar;

    @FXML
    private Label combinacionLabel;

    @FXML
    private TextField palabraIngresada;

    @FXML
    private Label puntaje;

    @FXML
    private TextArea resultado;

    @FXML
    private Label tiempo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeGame();
    }
    
    private void initializeGame() {
        loadDictionary();
        selectWordsForGame();
        generateCombination();
        startTimer();
    }

    private void loadDictionary() {
        diccionario = new Trie();
        Diccionario.cargarDiccionario();
        diccionario = Diccionario.getDiccionario();
        palabrasJuego = diccionario.getPalabras();
    }

    private void selectWordsForGame() {
        palabras = new Trie();
        List<String> palabrasSeleccionadas = new ArrayList<>();
        Random random = new Random();
        while (palabrasSeleccionadas.size() < 5) {
            int randomIndex = random.nextInt(palabrasJuego.size());
            String word = palabrasJuego.get(randomIndex);
            // Filtrar palabras por longitud configurada
            if (!palabrasSeleccionadas.contains(word) && word.length() == GameOptionsController.getLongitudConfigurada()) {
                palabrasSeleccionadas.add(word);
            }
        }
        for (String word : palabrasSeleccionadas) {
            palabras.insert(word, diccionario.getSignificado(word));
        }
    }

    private void generateCombination() {
        StringBuilder combinacion = new StringBuilder();
        for (String word : palabras.getPalabras()) {
            combinacion.append(word.toUpperCase());
        }
        
        Set<Character> combinacionFinal = new HashSet<>();
        for (char letra : combinacion.toString().toCharArray()) {
            combinacionFinal.add(letra);
        }
        
        combinacionLista = new ArrayList<>(combinacionFinal);
        Collections.shuffle(combinacionLista);
        String combinacionString = combinacionLista.stream().map(String::valueOf).collect(Collectors.joining());
        combinacionLabel.setText(combinacionString);
    }

    private void startTimer() {
        int time = GameOptionsController.getTiempoConfigurado(); // tiempo en segundos
        long startTime = System.currentTimeMillis();
        score = 0;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            long remainingTimeInMillis = TimeUnit.SECONDS.toMillis(time) - elapsedTime;

            if (remainingTimeInMillis <= 0) {
                timer.cancel();
                Platform.runLater(() -> {
                    tiempo.setText("Tiempo agotado");
                    System.out.println("Puntuación final: " + score);
                    System.out.println("Palabras: ");
                    System.out.println(palabras.getPalabras());

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Tiempo agotado");
                    alert.setHeaderText(null);
                    alert.setContentText("Se ha agotado el tiempo del juego.");
                    alert.showAndWait();

                    resultado.clear(); // Limpiar el contenido del TextArea
                    resultado.setText(palabras.getPalabras().toString());
                    combinacionLabel.setText(""); // Limpiar la combinación
                });
            } else {
                long remainingTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeInMillis);
                Platform.runLater(() -> {
                    tiempo.setText("Tiempo: " + remainingTimeInSeconds + " segundos");
                });
            }
        }
        }, 0, 1000);
    }

    @FXML
    public void buscarPalabra(ActionEvent event) {
        String palabra = palabraIngresada.getText().toUpperCase();
        if (isValidWord(palabra)) {
            evaluateWord(palabra);
        } else {
            resultado.appendText("Palabra inválida o letras no disponibles\n");
        }
        palabraIngresada.clear();
    }

    private boolean isValidWord(String palabra) {
        return palabra.length() > 0 && combinacionLista.containsAll(palabra.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    private void evaluateWord(String palabra) {
        palabra = Character.toUpperCase(palabra.charAt(0)) + palabra.substring(1).toLowerCase();
        if (palabras.search(palabra)) {
            score += palabra.length();
            puntaje.setText("Puntaje: " + score);
            resultado.appendText(palabra + ", es una palabra válida!\n");
        } else {
            resultado.appendText("No se encontró la palabra " + palabra + "\n");
        }
    }
}
