/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hyranasoftware.javagmr;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.hyranasoftware.javagmr.util.GMRLogger;
import nl.hyranasoftware.javagmr.util.JGMRConfig;

/**
 *
 * @author danny_000
 */
public class gui extends Application {

    @Override
    @java.lang.SuppressWarnings("squid:AVuVjDYZtpYg8Dj43Cce")
    public void start(Stage primaryStage) {
        GMRLogger.logLine("Opened jGMR");

        FXMLLoader loader = null;
        String url = null;
        url = getClass().getResource("views/fxml/jgmrGui.fxml").toString();
        System.out.println("  * url: " + url);
        loader = new FXMLLoader(getClass().getResource("views/fxml/jgmrGui.fxml"));

        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("eicon.png")));
        primaryStage.setTitle("Giant Multi Robot Java-Client");
        Object controller = loader.getController();
        scene.setUserData(controller);
        primaryStage.setScene(scene);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                Platform.setImplicitExit(!JGMRConfig.getInstance().isMinimizeToTray());
                primaryStage.hide();
            }
        });

        primaryStage.show();

        if (JGMRConfig.getInstance().isStartMinimized()) {
            Platform.setImplicitExit(false);

            /* XXX: This behavior leads to main window blinked. But avoiding
             * call `show` manages to a NullPointerException in
             * JgmrGuiController.initializeSystemtray. */
            primaryStage.hide();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
