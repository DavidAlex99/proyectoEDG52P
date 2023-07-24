module com.mycompany.proyectog5 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.proyectog5 to javafx.fxml;
    exports com.mycompany.proyectog5;
}
