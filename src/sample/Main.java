package sample;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import java.util.Locale;


public class Main extends Application
{
    private PerspectiveCamera camera;
    // 2 solution (to store rotate separately) for yaw/pitch degrees limit problem
    // yaw - isn't yaw but precession (прецессия), pitch isn't pitch but nutation (нутация).
    // It's means that body doesn't rotate around itself.
    private double yaw = 0d, pitch = 0d, roll = 0d;
    private double rotate = 5, step = 5;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Locale.setDefault(Locale.US);
        Box box = new Box(20, 20, 20); //100, 100, 100
        box.setMaterial(new PhongMaterial(Color.BLUE)); //3D Color set through material

        Sphere sphere = new Sphere(50);
        sphere.setMaterial(new PhongMaterial(Color.RED));
        sphere.setTranslateX(300);
        sphere.setTranslateY(40);
        sphere.setTranslateZ(50);

        // Light
        PointLight light = new PointLight();
        light.setTranslateZ(-80);

        Group root = new Group(sphere, box, light); //can't bind keyEvent to root.
        Scene scene = new Scene(root, 400, 300, true);
        scene.setOnKeyPressed(this::keyHandler);

        // default: FieldOfView = 30. the larger FOV, the more distortion.
        // NearClip = 0.1, FarClip = 100.0
        camera = new PerspectiveCamera(true); //def: false
        camera.setTranslateZ(-80); //300
        camera.setFarClip(10000);

        scene.setCamera(camera);

        primaryStage.setTitle("3D JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void keyHandler(KeyEvent keyEvent) {

        switch(keyEvent.getCode()){
            case LEFT:
                addRotate(camera, 0, -rotate, 0);
                System.out.printf("Yaw angle: %.0f°\n", yaw);
                break;
            case RIGHT:
                addRotate(camera, 0, rotate, 0);
                System.out.printf("Yaw angle: %.0f°\n", yaw);
                break;
            case UP: {
                double xz = Math.cos(Math.toRadians(pitch)) * step;
                double dx = xz * Math.sin(Math.toRadians(yaw));
                double dz = xz * Math.cos(Math.toRadians(yaw));
                double dy = Math.sin(Math.toRadians(pitch)) * -step;

                camera.setTranslateX(camera.getTranslateX() + dx);
                camera.setTranslateY(camera.getTranslateY() + dy);
                camera.setTranslateZ(camera.getTranslateZ() + dz);

                System.out.printf("X: %.2f, Z: %.2f, Y: %.2f\n", camera.getTranslateX(), camera.getTranslateZ(), camera.getTranslateY());
            } break;
            case DOWN: {
                double xz = Math.cos(Math.toRadians(pitch)) * -step;
                double dx = xz * Math.sin(Math.toRadians(yaw));
                double dz = xz * Math.cos(Math.toRadians(yaw));
                double dy = Math.sin(Math.toRadians(pitch)) * step;

                camera.setTranslateX(camera.getTranslateX() + dx);
                camera.setTranslateY(camera.getTranslateY() + dy);
                camera.setTranslateZ(camera.getTranslateZ() + dz);

                System.out.printf("X: %.2f, Z: %.2f, Y: %.2f\n", camera.getTranslateX(), camera.getTranslateZ(), camera.getTranslateY());
            } break;
            case W:
                addRotate(camera, 0, 0, rotate);
                System.out.printf("Pitch angle: %.0f°\n", pitch);
                break;
            case S:
                addRotate(camera, 0, 0, -rotate);
                System.out.printf("Pitch angle: %.0f°\n", pitch);
                break;
            // Roll doesn't work properly
            case Q:
                addRotate(camera, -rotate, 0, 0);
                System.out.printf("Roll angle: %.0f°\n", roll);
                break;
            case E:
                addRotate(camera, rotate, 0, 0);
                System.out.printf("Roll angle: %.0f°\n", roll);
                break;
        }
    }

// getPitch, getYaw, getRoll doesn't determine angle correctly, not used.
//    private double getPitch(Node n){
//        Transform T = n.getLocalToSceneTransform();
//        // -Math.acos(T.getMyy())
//        double p = Math.atan2(T.getMzy(),T.getMzz()); //f, roll(old) = pitch (new)
//        // simple workaround for yaw > 90 strange behavior
//        return p==Math.PI ? 0d : p;
//    }
//
//    private double getYaw(Node n){
//        Transform T = n.getLocalToSceneTransform();
//        double y = Math.atan2(-T.getMzx(),Math.sqrt(T.getMzy()*T.getMzy()+T.getMzz()*T.getMzz())); //0, pitch(old) = yaw (new)
//
//        // workaround for yaw > 90.
//        if(T.getMzz() < 0){
//            return (y<0?-Math.PI : Math.PI) - y;
//        } else return y;
//    }
//
//    private double getRoll(Node n){
//        Transform T = n.getLocalToSceneTransform();
//        return Math.atan2(T.getMyx(),T.getMxx()); //w, yaw(old) = roll (new)
//    }

    // angle in degree
    // alf: roll (z), bet: yaw (y), gam: pitch (x)
    private void addRotate(Node n, double alf, double bet, double gam){
        roll+=alf; yaw+=bet; pitch+=gam;
        roll%=360; yaw%=360; pitch%=360;

        if(roll > 180)        roll -= 360d;
        else if(roll < -180)  roll += 360d;
        if(yaw > 180)         yaw -= 360d;
        else if(yaw < -180)   yaw += 360d;
        if(pitch > 180)       pitch -= 360d;
        else if(pitch < -180) pitch += 360d;

        matrixRotateNode(n, Math.toRadians(roll), Math.toRadians(yaw), Math.toRadians(pitch));
    }

    // alf: roll (z), bet: yaw (y), gam: pitch (x) - for y-down system (javaFX default).
    // alf: yaw, bet: pitch, gam: roll (doesn't work properly) - for right-hand system.
    // en.wikipedia.org/wiki/Rotation_formalisms_in_three_dimensions#Conversion_formulae_between_formalisms
    // angle in radian
    private void matrixRotateNode(Node n, double alf, double bet, double gam){
        double A11=Math.cos(alf)*Math.cos(bet);
        double A12=Math.cos(alf)*Math.sin(bet)*Math.sin(gam)-Math.sin(alf)*Math.cos(gam);
        double A13=Math.cos(alf)*Math.sin(bet)*Math.cos(gam)+Math.sin(alf)*Math.sin(gam);
        double A21=Math.sin(alf)*Math.cos(bet);
        double A22=Math.sin(alf)*Math.sin(bet)*Math.sin(gam)+Math.cos(alf)*Math.cos(gam);
        double A23=Math.sin(alf)*Math.sin(bet)*Math.cos(gam)-Math.cos(alf)*Math.sin(gam);
        double A31=-Math.sin(bet);
        double A32=Math.cos(bet)*Math.sin(gam);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        double den = 2d*Math.sin(d);
        Point3D p = new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
        n.setRotationAxis(p);
        n.setRotate(Math.toDegrees(d));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
