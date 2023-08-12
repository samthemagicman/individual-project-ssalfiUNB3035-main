module ca.unb.cs3035.project {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.unb.cs3035.project to javafx.fxml;
    exports ca.unb.cs3035.project;
}