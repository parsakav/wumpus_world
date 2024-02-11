module com.pgu.wumpusworld {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pgu.wumpusworld to javafx.fxml;
    exports com.pgu.wumpusworld;
}