package com.pgu.wumpusworld;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application 
{
    public static final         TextArea logger = new TextArea();

    public static void main(String[] args) 
    {
        launch();        
    }
    
    @Override
    public void start(Stage stage) 
    {
        stage.setTitle("Example App (JavaFX)");
        // speed of movement

        int speed=3000;
        int number_of_pit=1,number_of_gold=1;
        //world size
        int n=4;
        JFXArena arena = new JFXArena(number_of_pit, number_of_gold, speed,n);
        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
        });
        
        ToolBar toolbar = new ToolBar();

        Label label = new Label("Wumpus World");
        toolbar.getItems().addAll(label);
        


        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}
