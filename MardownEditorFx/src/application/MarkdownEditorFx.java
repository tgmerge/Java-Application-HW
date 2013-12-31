package application;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * JavaFX MarkdownEditor
 * @author tgmerge
 *
 */
public class MarkdownEditorFx extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // 建立Scene
        stage.setTitle("Markdown Editor");
        scene = new Scene(new Browser(),800,600, Color.web("#FFFFFF"));
        stage.setScene(scene);
        // 显示Scene
        stage.show();
    }
 
    public static void main(String[] args){
        launch(args);
    }
}

/**
 * JavaFx WebView的封装
 * @author tgmerge
 *
 */
class Browser extends Region {
 
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
     
    public Browser() {
        getStyleClass().add("browser");
        webEngine.load(MarkdownEditorFx.class.getResource("/markdown.html").toExternalForm());
        getChildren().add(browser);
 
    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 750;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}