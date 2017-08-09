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
import javafx.scene.transform.Transform;
import javafx.stage.Stage;



public class Main extends Application
{
    private PerspectiveCamera camera;

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

        scene.setCamera(camera);

        System.out.println(camera.getLocalToSceneTransform());

        primaryStage.setTitle("3D JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void keyHandler(KeyEvent keyEvent) {

        switch(keyEvent.getCode()){
            case LEFT:
                addRotate(camera, 0, 0, 5);
                System.out.printf("Yaw angle: %.0f°\n", Math.toDegrees(getYaw(camera)));

                //rotateY.setAngle(rotateY.getAngle() - 1);
                //System.out.println("Y angle: "+ rotateY.getAngle()%360);
                break;
            case RIGHT:
                addRotate(camera, 0, 0, -5);
                System.out.printf("Yaw angle: %.0f°\n", Math.toDegrees(getYaw(camera)));

                //rotateY.setAngle(rotateY.getAngle() + 1);
                //System.out.println("Y angle: "+ rotateY.getAngle()%360);
                break;
            case UP: {
                //camera.setTranslateZ(camera.getTranslateZ()+5);

//                double xz = Math.cos(Math.toRadians(rotateX.getAngle())) * 10;
//                double dx = xz * Math.sin(Math.toRadians(rotateY.getAngle()));
//                double dz = xz * Math.cos(Math.toRadians(rotateY.getAngle()));
//                double dy = Math.sin(Math.toRadians(rotateX.getAngle())) * -10;
//
//                camera.setTranslateX(camera.getTranslateX() + dx);
//                camera.setTranslateY(camera.getTranslateY() + dy);
//                camera.setTranslateZ(camera.getTranslateZ() + dz);

                System.out.println("X:" + camera.getTranslateX()+ " |Z:" + camera.getTranslateZ()+ " |Y:" + camera.getTranslateY());
            } break;
            case DOWN: {
//                double xz = Math.cos(Math.toRadians(rotateX.getAngle())) * -10;
//                double dx = xz * Math.sin(Math.toRadians(rotateY.getAngle()));
//                double dz = xz * Math.cos(Math.toRadians(rotateY.getAngle()));
//                double dy = Math.sin(Math.toRadians(rotateX.getAngle())) * 10;
//
//                camera.setTranslateX(camera.getTranslateX() + dx);
//                camera.setTranslateY(camera.getTranslateY() + dy);
//                camera.setTranslateZ(camera.getTranslateZ() + dz);

                System.out.println("X:" + camera.getTranslateX()+ " |Z:" + camera.getTranslateZ()+ " |Y:" + camera.getTranslateY());
            } break;
            case W: {
//                rotateX.setAngle(rotateX.getAngle() + 5);
                addRotate(camera, 0, -5, 0);
                System.out.printf("Pitch angle: %.0f°\n", Math.toDegrees(getPitch(camera)));
            } break;
            case S: {
//                rotateX.setAngle(rotateX.getAngle() - 5);

                addRotate(camera, 0, 5, 0);
                System.out.printf("Pitch angle: %.0f°\n", Math.toDegrees(getPitch(camera)));
            }break;
        }
    }

    // Adapted José Pereda solution.
    private double getPitch(Node n){
        Transform T = n.getLocalToSceneTransform();
        return Math.atan2(-T.getMzy(), T.getMzz());
    }

    private double getRoll(Node n){
        Transform T = n.getLocalToSceneTransform();
        return Math.atan2(-T.getMyx(), T.getMxx());
    }

    private double getYaw(Node n){
        Transform T = n.getLocalToSceneTransform();
        return Math.atan2(T.getMzx(), Math.sqrt(T.getMzy() * T.getMzy() + T.getMzz() * T.getMzz()));
    }

    // input in degree
    private void addRotate(Node n, double alf, double bet, double gam){
        matrixRotateNode(n, getRoll(n) + Math.toRadians(alf), getPitch(n) + Math.toRadians(bet), getYaw(n) + Math.toRadians(gam));
    }

    //alf - roll (z), bet - pitch (x), gam - yaw (y).
    private void matrixRotateNode(Node n, double alf, double bet, double gam){
        double A11=Math.cos(alf)*Math.cos(gam);
        double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A21=-Math.cos(gam)*Math.sin(alf);
        double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A31=Math.sin(gam);
        double A32=-Math.cos(gam)*Math.sin(bet);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        double den=2d*Math.sin(d);
        Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
        n.setRotationAxis(p);
        n.setRotate(Math.toDegrees(d));
    }



    public static void main(String[] args) {
        launch(args);
    }
}
