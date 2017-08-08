package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Узнать про fxml позже, после Git
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Box box = new Box(100, 100, 100);
        //box.setTranslateX(150);
        box.setTranslateY(0);
        //box.setTranslateZ(400);

        Sphere sphere = new Sphere(50);
        //sphere.setTranslateX(300);
        //sphere.setTranslateY(-5);
        //sphere.setTranslateZ(400);
        sphere.setTranslateX(300);
        sphere.setTranslateY(110);
        sphere.setTranslateZ(50);

        // Точка освещения
        PointLight light = new PointLight();
        light.setTranslateZ(-80);

        Group root = new Group(sphere, light); //Doesn't bind keyEvent to root.
        Scene scene = new Scene(root, 400, 300, true);
        scene.setOnKeyPressed(this::keyHandler);

        // Установка камеры для обзора трехмерных фигур
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(100);
        camera.setTranslateY(-50);
        camera.setTranslateZ(300);
        scene.setCamera(camera);

        primaryStage.setTitle("3D JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void keyHandler(KeyEvent keyEvent) {
        switch(keyEvent.getCode()){
            case LEFT:
                System.out.println("Left"); break;
            case RIGHT:
                System.out.println("Right"); break;
            case UP:
                System.out.println("Up"); break;
            case DOWN:
                System.out.println("Down"); break;
            case W:
                System.out.println("W"); break;
            case S:
                System.out.println("S"); break;
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}
