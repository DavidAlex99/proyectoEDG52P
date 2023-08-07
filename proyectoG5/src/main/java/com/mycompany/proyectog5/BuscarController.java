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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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
    
    @FXML
    private Label nombreDiccionario;
    
    private boolean isGuardado;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Diccionario.cargarDiccionario();
        diccionario = Diccionario.getDiccionario();
        this.isGuardado = false;
    }
    
    @FXML
    private void cambiar() throws IOException {
        App.setRoot("menu");
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
            
             //sugerencia de palabras que ingrese Búsqueda inversa
            String terminacion = palabraIngresada;
            List<String> sugerenciasTerminacion = diccionario.buscarPorTerminacion(terminacion);
            sugerencias.addAll(sugerenciasTerminacion);
            
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
    public void cargar(ActionEvent event) {
        if (!isGuardado) {
            Alert alerta = new Alert(AlertType.CONFIRMATION);
            alerta.setTitle("Cargar diccionario");
            alerta.setHeaderText("¿Quieres continuar sin guardar los cambios?");
            alerta.setContentText("Perderás los cambios no guardados. ¿Deseas continuar?");

            ButtonType botonContinuar = new ButtonType("Continuar sin guardar");
            ButtonType botonCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            alerta.getButtonTypes().setAll(botonContinuar, botonCancelar);

            Optional<ButtonType> resultado = alerta.showAndWait();
            if (resultado.isPresent() && resultado.get() == botonContinuar) {
                // Continuar sin guardar
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Seleccione un archivo de diccionario");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                // Ruta absoluta de la carpeta "Diccionarios"
                String rutaDiccionarios = System.getProperty("user.dir") + "/Diccionarios";
                File archivoDiccionarios = new File(rutaDiccionarios);
                if (archivoDiccionarios.exists() && archivoDiccionarios.isDirectory()) {
                    fileChooser.setInitialDirectory(archivoDiccionarios);
                }

                File archivoSeleccionado = fileChooser.showOpenDialog(new Stage());
                if (archivoSeleccionado != null) {
                    cargarDiccionario(archivoSeleccionado);
                }
            }
        } else {
            // Si el diccionario está guardado
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione un archivo de diccionario");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            // Ruta absoluta de la carpeta "Diccionarios"
            String rutaDiccionarios = System.getProperty("user.dir") + "/Diccionarios";
            File archivoDiccionarios = new File(rutaDiccionarios);
            if (archivoDiccionarios.exists() && archivoDiccionarios.isDirectory()) {
                fileChooser.setInitialDirectory(archivoDiccionarios);
            }

            File archivoSeleccionado = fileChooser.showOpenDialog(new Stage());
            if (archivoSeleccionado != null) {
                cargarDiccionario(archivoSeleccionado);
            }
        }
    }

    private void cargarDiccionario(File archivo) {
        // Limpiar el Trie antes de cargar el nuevo diccionario
        limpiarCampos();
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
        nombreDiccionario.setText(archivo.getName().replace(".txt", ""));
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error al cargar el archivo");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo cargar el archivo seleccionado.");
            alerta.showAndWait();
        }
    }
    

    @FXML
    public void guardar(ActionEvent event) throws IOException, URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar diccionario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt"));

        // Ruta absoluta de la carpeta "Diccionarios"
        String rutaDiccionarios = System.getProperty("user.dir") + "/Diccionarios";
        File carpetaDiccionarios = new File(rutaDiccionarios);
        if (!carpetaDiccionarios.exists() || !carpetaDiccionarios.isDirectory()) {
            carpetaDiccionarios.mkdirs(); // Crear la carpeta si no existe
        }

        fileChooser.setInitialDirectory(carpetaDiccionarios);

        File archivoGuardar = fileChooser.showSaveDialog(new Stage());
        if (archivoGuardar != null) {
            String rutaAbsolutaArchivo = archivoGuardar.toPath().toAbsolutePath().toString();
            if (!rutaAbsolutaArchivo.endsWith(".txt")) {
                Alert alertaError = new Alert(AlertType.ERROR);
                alertaError.setTitle("Error al guardar el archivo");
                alertaError.setHeaderText(null);
                alertaError.setContentText("El archivo debe tener una extensión .txt.");
                alertaError.showAndWait();
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoGuardar))) {
                Trie diccionario = Diccionario.getDiccionario(); // Obtener el diccionario
                System.out.println("palabras:"+diccionario.getPalabras());
                for (String palabra : diccionario.getPalabras()) {
                    String significado = diccionario.getSignificado(palabra);
                    if (significado != null) {
                        bw.write(palabra + " : " + significado);
                        bw.newLine();
                    }
                }
                bw.close();
                isGuardado = true;
                // Mostrar mensaje de éxito
                Alert alertaExito = new Alert(AlertType.INFORMATION);
                alertaExito.setTitle("Guardado Exitoso");
                alertaExito.setHeaderText(null);
                alertaExito.setContentText("El diccionario se ha guardado correctamente.");
                alertaExito.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Manejar el error 
                Alert alertaError = new Alert(AlertType.ERROR);
                alertaError.setTitle("Error al guardar el archivo");
                alertaError.setHeaderText(null);
                alertaError.setContentText("No se pudo guardar el archivo seleccionado.");
                alertaError.showAndWait();
            }
        }
    }



    public void limpiarCampos() {
        busquedaTF.clear();
        palabraBuscada.setText("");
        significado.setText("");
        sugerenciasListView.getItems().clear();
    }
    
    /*
    Se va a aprovechar del metodo para escoger una plaabra de las sugeridas
    */
   @FXML
    public void deleteWord(ActionEvent event) {
        // Tomando la palabra del textfield
        String palabraAEliminar = busquedaTF.getText().trim();
        if (palabraAEliminar.isEmpty()) {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error de búsqueda");
            alerta.setHeaderText(null);
            alerta.setContentText("Ingrese la palabra a buscar");
            alerta.showAndWait();
        } else {
            // Primera parte es la letra mayúscula, segunda parte es lo demás en minúscula
            String word = palabraAEliminar.substring(0, 1).toUpperCase() + palabraAEliminar.substring(1).toLowerCase();

            // Obtener el significado de la palabra
            String significadoPalabraAEliminar = diccionario.getSignificado(word);

            if (significadoPalabraAEliminar != null) {
                Alert confirmacion = new Alert(AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar eliminación");
                confirmacion.setHeaderText(null);
                confirmacion.setContentText("¿Estás seguro que deseas eliminar la palabra '" + word + "' del diccionario?");

                Optional<ButtonType> respuesta = confirmacion.showAndWait();
                if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                    // Eliminacion confirmada
                    if (diccionario.remove(word)) {
                        limpiarCampos();
                        // La palabra fue eliminada correctamente
                        Alert alerta = new Alert(AlertType.INFORMATION);
                        alerta.setTitle("Palabra eliminada");
                        alerta.setHeaderText(null);
                        alerta.setContentText("La palabra '" + word + "' fue eliminada del diccionario.");
                        alerta.showAndWait();

                        String ruta = System.getProperty("user.dir") + "/Diccionarios/" + nombreDiccionario.getText() + ".txt";
                        // Arreglo para guardar las líneas del archivo
                        List<String> lines = new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
                            String linea;
                            while ((linea = br.readLine()) != null) {
                                // Si la línea no empieza con la palabra a eliminar, la guardamos
                                if (!linea.startsWith(word + ":")) {
                                    lines.add(linea);
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // Se reescribe el archivo con las líneas que quedaron
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
                            for (String line : lines) {
                                bw.write(line);
                                bw.newLine();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
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

}