module com.mycompany.proyectog5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.proyectog5 to javafx.fxml;
    exports com.mycompany.proyectog5;
}
