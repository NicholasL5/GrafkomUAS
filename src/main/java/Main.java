import Engine.*;
import Engine.Object;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window =
        new Window(1600,1600,
                "Hello World");
    ArrayList<Object> objects
            = new ArrayList<>();
    ArrayList<Sphere> grass = new ArrayList<>();
    Random random = new Random();
    Camera camera = new Camera();
    Skybox sk;
    Terrain terrain;
    Player player;
    boolean TPP;
    boolean freeCamera;
    boolean FPP;
    Projection projection = new Projection(window.getWidth(),window.getHeight());
    public void init(){
        window.init();
        GL.createCapabilities();

//        camera.setRotation((float)Math.toRadians(0.0f),(float)Math.toRadians(30.0f));
        //code

        this.sk = new Skybox(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/skybox.vert"
                                , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/skybox.frag"
                                , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.0f,1.0f,0.0f,1.0f)
        );

        terrain = new Terrain(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/terrain.vert"
                                , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/terrain.frag"
                                , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.0f,1.0f,0.0f,1.0f),400 ,400
        );
//        terrain.translateObject(400f, 0f ,400f);

        //stall
        objects.add(new Sphere(
            Arrays.asList(
                new ShaderProgram.ShaderModuleData(
                        "resources/shaders/scene.vert"
                        , GL_VERTEX_SHADER),
                new ShaderProgram.ShaderModuleData(
                        "resources/shaders/scene.frag"
                        , GL_FRAGMENT_SHADER)
            ),
            new ArrayList<>(
                List.of(
                    new Vector3f(-0.5f,0.5f,0.0f),
                    new Vector3f(-0.5f,-0.5f,0.0f),
                    new Vector3f(0.5f,-0.5f,0.0f),
                    new Vector3f(0.5f,0.5f,0.0f)
                )
            ),
            // warna
            new Vector4f(0.0f,1.0f,0.0f,1.0f),
                //centerpoint
                Arrays.asList(0.0f,0.0f,0.0f),
                0.125f,
                0.125f,
                0.125f,
                "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\stall.obj",
                "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\stallTexture.png"
        ));
        objects.get(0).rotateObject((float) Math.toRadians(180f),0f,  1f, 0f);
        objects.get(0).translateObject(-4.240E+2f,  0.000E+0f, -3.081E+2f);

//        objects.get(0).scaleObject(0.1f,0.1f,0.1f);

        //rumah tengah
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\rumah5.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\Farmhouse_Texture.png"
                )
        );
        objects.get(1).translateObject(-418f, 0f, -220f);

        //rumah jelek
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\rumah_low.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\TEX\\color_palette.png"
                )
        );
        objects.get(2).rotateObject((float) Math.toRadians(135),0.0f,1.0f,0.0f);
        objects.get(2).translateObject(-380f, 0f, -240f);

        //lampu 1
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\lamp.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\lampu\\lamp.png"
                )
        );
        objects.get(3).translateObject(-4.355E+2f,  0f, -3.147E+2f);

        //api unggun
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\campfirejadi3.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\lampu\\camfire.png"
                )
        );
        objects.get(4).translateObject(-418f,  0.000E+0f, -2.689E+2f);

        //rumah 2 lantai
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\rumah7.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\rumah7.png"
                )
        );

        objects.get(5).scaleObject(2.5f,  2.5f, 2.5f);
        objects.get(5).rotateObject((float) Math.toRadians(45),0.0f,1.0f,0.0f);
        objects.get(5).translateObject(-456f,  0.000E+0f, -240f);

        //lampu 3
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\lamp.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\lampu\\lamp.png"
                )
        );
        objects.get(6).translateObject(-3.341E+2f,  0f, -2.557E+2f);

        //lampu 4
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\lamp.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\lampu\\lamp.png"
                )
        );
        objects.get(7).translateObject(-4.445E+2f,  0f, -1.915E+2f);

        //rumah tukang
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\rumahtukang.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\rumahtukang.png"
                )
        );

        objects.get(8).scaleObject(7f,  7f, 7f);
        objects.get(8).rotateObject((float) Math.toRadians(60),0.0f,1.0f,0.0f);
        objects.get(8).translateObject(-456f,  6.0f, -2.836E+2f);

        //rumah 2 lantai ke 2
        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\rumah7.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\rumah7.png"
                )
        );

        objects.get(9).scaleObject(2.5f,  2.5f, 2.5f);
        objects.get(9).rotateObject((float) Math.toRadians(225),0.0f,1.0f,0.0f);
        objects.get(9).translateObject(-380f,  0.0f, -2.836E+2f);

        objects.add(
                new Sphere(
                        Arrays.asList(
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData(
                                        "resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                        ),
                        new ArrayList<>(
                                List.of(
                                        new Vector3f(-0.5f,0.5f,0.0f),
                                        new Vector3f(-0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,-0.5f,0.0f),
                                        new Vector3f(0.5f,0.5f,0.0f)
                                )
                        ),
                        // warna
                        new Vector4f(0.0f,1.0f,0.0f,1.0f),
                        //centerpoint
                        Arrays.asList(0.0f,0.0f,0.0f),
                        0.125f,
                        0.125f,
                        0.125f,
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\kerenv2.obj",
                        "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\Base_Color_Black.png"
                )
        );

        objects.get(10).scaleObject(2.5f,2.5f,2.5f);
        objects.get(10).translateObject(-4.240E+2f,  0.000E+0f, -3.101E+2f);

        //titan
//        player = new Player(
//                Arrays.asList(
//                        new ShaderProgram.ShaderModuleData(
//                                "resources/shaders/scene.vert"
//                                , GL_VERTEX_SHADER),
//                        new ShaderProgram.ShaderModuleData(
//                                "resources/shaders/scene.frag"
//                                , GL_FRAGMENT_SHADER)
//                ),
//                new ArrayList<>(
//                        List.of(
//                                new Vector3f(-0.5f,0.5f,0.0f),
//                                new Vector3f(-0.5f,-0.5f,0.0f),
//                                new Vector3f(0.5f,-0.5f,0.0f),
//                                new Vector3f(0.5f,0.5f,0.0f)
//                        )
//                ),
//                // warna
//                new Vector4f(0.0f,1.0f,0.0f,1.0f),
//                //centerpoint
//                Arrays.asList(0.0f,0.0f,0.0f),
//                0.125f,
//                0.125f,
//                0.125f, "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\manusia1.obj",
//                "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\textureManusia.PNG"
//        );

        player = new Player(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/scene.vert"
                                , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/scene.frag"
                                , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(
                        List.of(
                                new Vector3f(-0.5f,0.5f,0.0f),
                                new Vector3f(-0.5f,-0.5f,0.0f),
                                new Vector3f(0.5f,-0.5f,0.0f),
                                new Vector3f(0.5f,0.5f,0.0f)
                        )
                ),
                // warna
                new Vector4f(0.0f,1.0f,0.0f,1.0f),
                //centerpoint
                Arrays.asList(0.0f,0.0f,0.0f),
                0.125f,
                0.125f,
                0.125f, "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\objects\\kerenv2.obj",
                "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\Base_Color_Black.png"
        );

        player.scaleObject(2.5f, 2.5f,2.5f);
//        player.rotateObject((float) Math.toRadians(180f), 0f,1f,0f);
//        player.translateObject(-10f,0f,4.2f);

        camera.setPosition(player.getPosition().x, player.getPosition().y+5, player.getPosition().z-7);
        camera.addRotation(0f,(float) Math.toRadians(180f));
        camera.setPlayer(player);
        player.translateObject(-4.204E+2f,  0.000E+0f, -2.724E+2f);

        Vector3f objek = player.updateCenterPoint();
        camera.setPosition(objek.x, objek.y + 5, objek.z - 8);
        camera.moveUp(0.1f);
        camera.moveBackwards(0.1f);
        TPP = true;
        freeCamera = false;
        FPP = false;
    }
    public void input() {
        float move = 0.5f;
        if (window.isKeyPressed(GLFW_KEY_3)) {
            Vector3f objek = player.updateCenterPoint();
//            camera.setPosition(objek.x, objek.y + 15, objek.z - 18);
            camera.setPosition(objek.x, objek.y + 5, objek.z - 8);

            TPP = true;
            freeCamera = false;
            FPP = false;
        }
        if (window.isKeyPressed(GLFW_KEY_1)) {
            TPP = false;
            freeCamera = false;
            FPP = true;
        }
        if (window.isKeyPressed(GLFW_KEY_2)) {
            TPP = false;
            freeCamera = true;
            FPP = false;
        }
        if (TPP) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                float distance = 1.5f;
                float dx = (float) (distance * Math.sin(Math.toRadians(player.getRotation().y)));
                float dz = (float) (distance * Math.cos(Math.toRadians(player.getRotation().y)));
                player.translateObject(dx * 0.1f, 0f, dz * 0.1f);
                camera.setPosition(camera.getPosition().x + dx * 0.1f, camera.getPosition().y + 0f, camera.getPosition().z + dz * 0.1f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                float distance = -1.5f;
                float dx = (float) (distance * Math.sin(Math.toRadians(player.getRotation().y)));
                float dz = (float) (distance * Math.cos(Math.toRadians(player.getRotation().y)));
                player.translateObject(dx * 0.1f, 0f, dz * 0.1f);
                camera.setPosition(camera.getPosition().x + dx * 0.1f, camera.getPosition().y + 0f, camera.getPosition().z + dz * 0.1f);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                Vector3f tempCenterPoint1 = player.updateCenterPoint();

                player.translateObject(-tempCenterPoint1.x, -tempCenterPoint1.y, -tempCenterPoint1.z);
                player.rotateObject((float) Math.toRadians(2), 0.0f, 1.0f, 0.0f);
                player.translateObject(tempCenterPoint1.x, tempCenterPoint1.y, tempCenterPoint1.z);

                Vector3f temp = new Vector3f(0, 2f, 0);
                player.addRotation(temp);

            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                Vector3f tempCenterPoint1 = player.updateCenterPoint();
                player.translateObject(-tempCenterPoint1.x, -tempCenterPoint1.y, -tempCenterPoint1.z);
                player.rotateObject((float) Math.toRadians(2), 0.0f, -1.0f, 0.0f);
                player.translateObject(tempCenterPoint1.x, tempCenterPoint1.y, tempCenterPoint1.z);

                Vector3f temp = new Vector3f(0, -2f, 0);
                player.addRotation(temp);

            }
        } else if (freeCamera) {
            if (!window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {

                if (window.isKeyPressed(GLFW_KEY_W)) {
                    float distance = 1.5f;
                    float dx = (float) (distance * Math.sin(Math.toRadians(player.getRotation().y)));
                    float dz = (float) (distance * Math.cos(Math.toRadians(player.getRotation().y)));
                    player.translateObject(dx * 0.1f, 0f, dz * 0.1f);

                }
                if (window.isKeyPressed(GLFW_KEY_S)) {
                    float distance = -1.5f;
                    float dx = (float) (distance * Math.sin(Math.toRadians(player.getRotation().y)));
                    float dz = (float) (distance * Math.cos(Math.toRadians(player.getRotation().y)));
                    player.translateObject(dx * 0.1f, 0f, dz * 0.1f);

                }
                if (window.isKeyPressed(GLFW_KEY_A)) {
                    Vector3f tempCenterPoint1 = player.updateCenterPoint();

                    player.translateObject(-tempCenterPoint1.x, -tempCenterPoint1.y, -tempCenterPoint1.z);
                    player.rotateObject((float) Math.toRadians(2), 0.0f, 1.0f, 0.0f);
                    player.translateObject(tempCenterPoint1.x, tempCenterPoint1.y, tempCenterPoint1.z);

                    Vector3f temp = new Vector3f(0, 2f, 0);
                    player.addRotation(temp);
                }
                if (window.isKeyPressed(GLFW_KEY_D)) {
                    Vector3f tempCenterPoint1 = player.updateCenterPoint();
                    player.translateObject(-tempCenterPoint1.x, -tempCenterPoint1.y, -tempCenterPoint1.z);
                    player.rotateObject((float) Math.toRadians(2), 0.0f, -1.0f, 0.0f);
                    player.translateObject(tempCenterPoint1.x, tempCenterPoint1.y, tempCenterPoint1.z);

                    Vector3f temp = new Vector3f(0, -2f, 0);
                    player.addRotation(temp);
                }
            } else {

                if (window.isKeyPressed(GLFW_KEY_W) && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                    camera.moveForward(move);
                }
                if (window.isKeyPressed(GLFW_KEY_S) && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                    camera.moveBackwards(move);
                }
                if (window.isKeyPressed(GLFW_KEY_A) && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                    camera.moveLeft(move);
                }
                if (window.isKeyPressed(GLFW_KEY_D) && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                    camera.moveRight(move);
                }
                if (window.isKeyPressed(GLFW_KEY_Q) && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                    camera.moveUp(move);
                }
                if (window.isKeyPressed(GLFW_KEY_E) && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                    camera.moveDown(move);
                }
            }
        } else if (FPP) {
            Vector3f objek = player.updateCenterPoint();
            camera.setPosition(objek.x, objek.y + 5, objek.z);

            TPP = false;
            freeCamera = false;
            FPP = true;

            if (window.isKeyPressed(GLFW_KEY_W)) {
                float distance = 1.5f;
                float dx = (float) (distance * Math.sin(Math.toRadians(player.getRotation().y)));
                float dz = (float) (distance * Math.cos(Math.toRadians(player.getRotation().y)));
                player.translateObject(dx * 0.1f, 0f, dz * 0.1f);
                camera.setPosition(camera.getPosition().x + dx * 0.1f, camera.getPosition().y + 0f, camera.getPosition().z + dz * 0.1f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                float distance = -1.5f;
                float dx = (float) (distance * Math.sin(Math.toRadians(player.getRotation().y)));
                float dz = (float) (distance * Math.cos(Math.toRadians(player.getRotation().y)));
                player.translateObject(dx * 0.1f, 0f, dz * 0.1f);
                camera.setPosition(camera.getPosition().x + dx * 0.1f, camera.getPosition().y + 0f, camera.getPosition().z + dz * 0.1f);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                Vector3f tempCenterPoint1 = player.updateCenterPoint();

                player.translateObject(-tempCenterPoint1.x, -tempCenterPoint1.y, -tempCenterPoint1.z);
                player.rotateObject((float) Math.toRadians(2), 0.0f, 1.0f, 0.0f);
                player.translateObject(tempCenterPoint1.x, tempCenterPoint1.y, tempCenterPoint1.z);

                Vector3f temp = new Vector3f(0, 2f, 0);
                player.addRotation(temp);


                camera.addRotation(0f, (float) Math.toRadians(-2f));
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                Vector3f tempCenterPoint1 = player.updateCenterPoint();
                player.translateObject(-tempCenterPoint1.x, -tempCenterPoint1.y, -tempCenterPoint1.z);
                player.rotateObject((float) Math.toRadians(2), 0.0f, -1.0f, 0.0f);
                player.translateObject(tempCenterPoint1.x, tempCenterPoint1.y, tempCenterPoint1.z);

                Vector3f temp = new Vector3f(0, -2f, 0);
                player.addRotation(temp);
                camera.addRotation(0f, (float) Math.toRadians(2f));
            }

            if (window.getMouseInput().isLeftButtonPressed() && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));

            }

        }
        if (!FPP) {
            if (window.getMouseInput().isLeftButtonPressed() && window.isKeyPressed(GLFW_KEY_LEFT_ALT)) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));

            } else if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                camera.addRotation(0f, (float) Math.toRadians(displayVector.y * 0.1f));

                Vector3f target = player.updateCenterPoint();
                Vector3f sub = new Vector3f(camera.getPosition().x - target.x,
                        camera.getPosition().y - target.y, camera.getPosition().z - target.z);


                float xnow = (float) ((sub.x * Math.cos((float) -Math.toRadians(displayVector.y * 0.1f))) +
                        (sub.z * Math.sin((float) -Math.toRadians(displayVector.y * 0.1f))));
                float ynow = sub.y;
                float znow = (float) ((-sub.x * Math.sin((float) -Math.toRadians(displayVector.y * 0.1f))) +
                        (sub.z * Math.cos((float) -Math.toRadians(displayVector.y * 0.1f))));
                camera.setPosition(xnow + target.x, ynow + target.y, znow + target.z);
            }
        }

        if (window.isKeyPressed(GLFW_KEY_P)) {
            System.out.println(player.updateCenterPoint());
        }

        if (window.isKeyPressed(GLFW_KEY_T)) {
            if (player.updateCenterPoint().y > 0) {
                player.translateObject(0f, -move, 0f);
            }


        }
    }

    public void loop(){
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f,
                    0.0f, 0.0f,
                    0.0f);
            GL.createCapabilities();
            input();
            //code
            //..
            sk.draw(camera, projection);
            terrain.draw(camera, projection);
            player.draw(camera, projection);
            for(Object object:objects){
                object.draw(camera,projection);
            }
//            Sphere.disableCulling();
//            Sphere.setUseFakeLightning();
            for (Sphere obj:grass){
                obj.draw(camera, projection);
            }
//            Sphere.enableCulling();

            // Restore state
            glDisableVertexAttribArray(0);
            // Poll for window events.
            // The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    public void run() {

        init();
        loop();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public static void main(String[] args) {
        new Main().run();
    }
}