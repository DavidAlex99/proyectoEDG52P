package com.mycompany.proyectog5;

import estructuras.Trie;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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
        /*
        Iniciaizacion del diccionario con las palabras
        */
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
    
    /*
    Se va a arovechar del metodo para escoger una plaabra de las sugeridas
    */
    @FXML
    public void deleteWord(ActionEvent event){
        //tomando la palabra del textfield
        String palabraAEliminar = busquedaTF.getText().trim();
        if(palabraAEliminar.isEmpty()){
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error de búsqueda");
            alerta.setHeaderText(null);
            alerta.setContentText("Ingrese la palabra a buscar");
            alerta.showAndWait();
        }else{
            //Primera parte es la letra mayuscula, segunda parte es lo sdemas en minuscula
            String word = palabraAEliminar.substring(0,1).toUpperCase() + palabraAEliminar.substring(1).toLowerCase();
            //diccionario es instancia de Trie
            //obtener el significado de la palabra
            String significadoPalabraAEliminar = diccionario.getSignificado(word);
            
            if (significadoPalabraAEliminar != null) {
                Alert confirmacion = new Alert(AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar eliminación");
                confirmacion.setHeaderText(null);
                confirmacion.setContentText("¿Estás seguro que deseas eliminar la palabra '" + word + "' del diccionario?");

                Optional<ButtonType> respuesta = confirmacion.showAndWait();
                if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                    // El usuario confirmó la eliminación
                    if (diccionario.remove(word)) {
                        // La palabra fue eliminada correctamente
                        Alert alerta = new Alert(AlertType.INFORMATION);
                        alerta.setTitle("Palabra eliminada");
                        alerta.setHeaderText(null);
                        alerta.setContentText("La palabra '" + word + "' fue eliminada del diccionario.");
                        alerta.showAndWait();
                        
                        //Paret para modificar el archivo, borrando la palabra que se elimino
                        String ruta = "Diccionario General.txt";
                         //Arreglo para guardar las lineas del archivo
                        List<String> lines = new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
                            String linea;
                            while ((linea = br.readLine()) != null) {
                                //Si la línea no empieza con la palabra a eliminar, la guardamos
                                if (!linea.startsWith(word + ":")) {  
                                    lines.add(linea);
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        //Se reescribe el archivo con las lineas que quedo
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
                            for (String line : lines) {
                                bw.write(line);
                                bw.newLine();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }   
                    } else {
                        // Ocurrió un error al intentar eliminar la palabra
                        Alert alerta = new Alert(AlertType.ERROR);
                        alerta.setTitle("Error al eliminar");
                        alerta.setHeaderText(null);
                        alerta.setContentText("Ocurrió un error al intentar eliminar la palabra '" + word + "' del diccionario.");
                        alerta.showAndWait();
                    }
                }
            } else {
                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Búsqueda");
                alerta.setHeaderText(null);
                alerta.setContentText("La palabra '" + word + "' no se encontró en el diccionario.");
                alerta.showAndWait(); // mostrar la alerta
            }
        }
    }

    public Trie getDiccionario() {
        return diccionario;
    }
    
    /*
    Cargar un nuevo archivo de diccionarios
    */
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
