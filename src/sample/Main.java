package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;



public class Main extends Application
{
    private PerspectiveCamera camera;
    private Rotate rotateY, rotateX;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Box box = new Box(20, 20, 20); //100, 100, 100
        box.setMaterial(new PhongMaterial(Color.BLUE)); //3D Color set through material

        Sphere sphere = new Sphere(50);
        sphere.setMaterial(new PhongMaterial(Color.RED));
        sphere.setTranslateX(300);
        sphere.setTranslateY(110);
        sphere.setTranslateZ(50);

        // Точка освещения
        PointLight light = new PointLight();
        light.setTranslateZ(-80);

        Group root = new Group(sphere, box, light); //Doesn't bind keyEvent to root.
        Scene scene = new Scene(root, 400, 300, true);
        scene.setOnKeyPressed(this::keyHandler);

        // Установка камеры для обзора трехмерных фигур
        // default: FieldOfView = 30. the larger fov, the more distortion.
        // NearClip = 0.1, FarClip = 100.0
        camera = new PerspectiveCamera(true); //def: false
        camera.setTranslateZ(-80); //300
        camera.setFarClip(10000);
        camera.getTransforms().addAll(rotateX = new Rotate(0, Rotate.X_AXIS), rotateY = new Rotate(0, Rotate.Y_AXIS));

        scene.setCamera(camera);

        primaryStage.setTitle("3D JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void keyHandler(KeyEvent keyEvent) {

        switch(keyEvent.getCode()){
            case LEFT:
                rotateY.setAngle(rotateY.getAngle() - 1);
                System.out.println("Y angle: "+ rotateY.getAngle()%360);
                break;
            case RIGHT:
                rotateY.setAngle(rotateY.getAngle() + 1);
                System.out.println("Y angle: "+ rotateY.getAngle()%360);
                break;
            case UP:
                camera.setTranslateZ(camera.getTranslateZ()+5);
                System.out.println(camera.getTranslateZ());
                break;
            case DOWN:
                camera.setTranslateZ(camera.getTranslateZ()-5);
                System.out.println("Z:" + camera.getTranslateZ());
                break;
            case W:
                rotateX.setAngle(rotateX.getAngle() - 5);
                System.out.println("X angle: "+ rotateX.getAngle()%360);
                break;
            case S:
                rotateX.setAngle(rotateX.getAngle() + 5);
                System.out.println("X angle: "+ rotateX.getAngle()%360);
                break;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
