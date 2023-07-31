package com.mycompany.proyectog5;

import estructuras.Trie;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private BorderPane root;

    @FXML
    private Label significado;

    @FXML
    private ListView<String> sugerenciasListView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        diccionario = new Trie();
        String ruta = "Diccionario General.txt";
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
    
    //asociado con el boton Buscar
    //busca la palabra ingresada en el textfield y muestra su significado
    @FXML
    public void searchWord(ActionEvent event) {
        String palabraRecuperada = busquedaTF.getText().trim();
        if(palabraRecuperada.isEmpty()){
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error de búsqueda");
            alerta.setHeaderText(null);
            alerta.setContentText("Ingrese la palabra a buscar");
            alerta.showAndWait();
        }else{
            String word = palabraRecuperada.substring(0,1).toUpperCase() + palabraRecuperada.substring(1).toLowerCase();
            //diccionario es instancia de Trie
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
    }
    
    //se muestran las sugerencias conforme a lo que teclee el usuario
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
    
    //cambia al fxml insert
    @FXML
    public void insertar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insert.fxml"));
        Parent parent = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.showAndWait(); 
    }

    public Trie getDiccionario() {
        return diccionario;
    }
    
    
    @FXML
    public void cargar(ActionEvent event) throws URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione un archivo de diccionario");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Obtener la ruta absoluta de la carpeta "diccionarios"
        URL directorioDiccionarios = getClass().getResource("/Diccionarios");
        if (directorioDiccionarios != null) {
            String rutaDiccionarios = Paths.get(directorioDiccionarios.toURI()).toString();
            File archivoDiccionarios = new File(rutaDiccionarios);
            if (archivoDiccionarios.exists() && archivoDiccionarios.isDirectory()) {
                fileChooser.setInitialDirectory(archivoDiccionarios);
            }
        }

        File archivoSeleccionado = fileChooser.showOpenDialog(new Stage());
        if (archivoSeleccionado != null) {
            cargarDiccionario(archivoSeleccionado);
        }
    }

    private void cargarDiccionario(File archivo) {
        // Limpiar el Trie antes de cargar el nuevo diccionario
        diccionario.clear(); 
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
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
            // Manejar el error si ocurre algún problema al cargar el archivo
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error al cargar el archivo");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo cargar el archivo seleccionado.");
            alerta.showAndWait();
        }
    }
}