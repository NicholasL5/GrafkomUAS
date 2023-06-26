package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class Player extends Sphere{

    public static final float RUN_SPEED = 20;
    public static final float TURN_SPEED = 160;
    public static final float GRAVITY = -50;
    public static final float JUMP_POWER = 30;
    public static final float TERRAIN_HEIGHT = 0;
    private float currentSpeed = 0;
    private float upwardsSpeed = 0;
    private float currentTurnSpeed = 0;

    private boolean isInAir = false;
    private float scale = 1;
    private Vector3f position;
    private Vector3f rotation;

    public Player(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color,
                  List<Float> centerPoint, Float radiusX, Float radiusY, Float radiusZ, String filename, String texture) {
        super(shaderModuleDataList, vertices, color, centerPoint, radiusX, radiusY, radiusZ, filename, texture);
        position = new Vector3f(centerPoint.get(0), centerPoint.get(1), centerPoint.get(2));
        rotation = new Vector3f(0,0,0);
    }

    public void move(Terrain terrain){

        this.increaseRotation(0, currentTurnSpeed * Window.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * Window.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(this.rotation.y)));
        float dz = (float) (distance * Math.cos(Math.toRadians(this.rotation.y)));
        this.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * Window.getFrameTimeSeconds();
        increasePosition(0, upwardsSpeed*Window.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if(getPosition().y < terrainHeight){
            upwardsSpeed = 0;
            isInAir = false;
            getPosition().y = terrainHeight;
        }
        createTransformationMatrix();

    }

    public void jump(){
        if (!isInAir){
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setCurrentTurnSpeed(float currentTurnSpeed) {
        this.currentTurnSpeed = currentTurnSpeed;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void increaseRotation(float dx, float dy, float dz){
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.z += dz;
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void createTransformationMatrix(){
        // buat matrix transformasi dari pergerakan entity

        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(this.position)
                .rotateX((float) Math.toRadians(this.rotation.x))
                .rotateY((float) Math.toRadians(this.rotation.y))
                .rotateZ((float) Math.toRadians(this.rotation.z))
                .scale(this.scale);

        model = matrix;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addRotation(Vector3f rotate){
        rotation.add(rotate);
        System.out.println(rotation.y);
    }
}
