package com.mycompany.proyectog5;

import estructuras.Trie;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GameController implements Initializable {
    private List<String> palabrasJuego;
    private Trie diccionario;
    private List<Character> combinacionLista;
    private int score;
    private Trie palabras;
    private int tiempoConfigurado;
    private int longitudConfigurada;
    private Set<String> palabrasAcertadas = new HashSet<>();
    private Set<String> palabrasErradas = new HashSet<>();
    private Timer timer;
    
    @FXML
    private BorderPane root;
    
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
    
    @FXML
    private TextArea acertadas;
    
    @FXML
    private TextArea erradas;
    
    @FXML
    private ScrollPane acertadasSP;
    
    @FXML
    private ScrollPane erradasSP;

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
            if (!palabrasSeleccionadas.contains(word) && word.length() <= GameOptionsController.getLongitudConfigurada()) {
                palabrasSeleccionadas.add(word);
            }
        }
        for (String word : palabrasSeleccionadas) {
            palabras.insert(word, diccionario.getSignificado(word));
        }
        //AYUDA
        System.out.println("Palabras Seleccionadas: "+palabras.getPalabras());
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
        StringBuilder combinacionBuilder = new StringBuilder();
        for (Character c : combinacionLista) {
            combinacionBuilder.append(c);
            combinacionBuilder.append(" ");
        }
        String combinacionString = combinacionBuilder.toString();
        combinacionLabel.setText(combinacionString);
    }

    private void startTimer() {
        int time = GameOptionsController.getTiempoConfigurado(); // tiempo en segundos
        long startTime = System.currentTimeMillis();
        score = 0;
        timer = new Timer();
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

                    jugarOtraVez((Stage) root.getScene().getWindow());
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
            //resultado.appendText("Palabra inválida o letras no disponibles\n");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Palabra inválida");
            alert.setHeaderText(null);
            alert.setContentText("La palabra es invalida o las letras no están disponibles");
            alert.showAndWait();
        }
        palabraIngresada.clear();
    }

    private boolean isValidWord(String palabra) {
        return palabra.length() > 0 && combinacionLista.containsAll(palabra.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    private void evaluateWord(String palabra) {
        palabra = Character.toUpperCase(palabra.charAt(0)) + palabra.substring(1).toLowerCase();

        if (palabras.search(palabra)) {
            if (!palabrasAcertadas.contains(palabra)) {
                score += palabra.length();
                puntaje.setText("Puntaje: " + score);
                acertadas.appendText(palabra + "\n");
                acertadas.positionCaret(acertadas.getText().length());
                palabrasAcertadas.add(palabra);
            } else {
                mostrarAlerta("Palabra Repetida", "La palabra ya fue acertada previamente.");
            }
        } else {
            if (!palabrasErradas.contains(palabra)) {
                erradas.appendText(palabra + "\n");
                erradas.positionCaret(erradas.getText().length());
                palabrasErradas.add(palabra);
            } else {
                mostrarAlerta("Palabra Repetida", "La palabra ya fue errada previamente.");
            }
        }

        if (palabrasAcertadas.size() == palabras.getPalabras().size()) {
            mostrarVictoria((Stage) root.getScene().getWindow()); 

        }
    }


    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    
    private void mostrarVictoria(Stage stage) {
        // Cancelar el temporizador si el jugador gana antes de que el tiempo se agote
        if (timer != null) {
            timer.cancel();
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText(null);
        alert.setContentText("¡Has encontrado todas las palabras válidas!\nPuntaje final: " + score);
        alert.showAndWait();
        jugarOtraVez(stage);
    }

    public void changeView(String fxmlFileName, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent newRoot = loader.load();
            Scene newScene = new Scene(newRoot);

            Stage currentStage = (Stage) stage.getScene().getWindow();

            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void jugarOtraVez(Stage currentStage) {
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("¿Jugar otra vez?");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("¿Deseas jugar otra vez?");

        ButtonType buttonTypeYes = new ButtonType("Sí");
        ButtonType buttonTypeNo = new ButtonType("No");

        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            changeView("gameOptions.fxml", currentStage);
        } else {
            changeView("menu.fxml", currentStage);
        }
    }
    
    @FXML
    private void cambiar() throws IOException {
        timer.cancel();
        App.setRoot("menu");
    }
    
}
