package Engine;

import org.joml.*;

import java.lang.Math;

public class Camera {

    private Vector3f direction;
    private Vector3f position;
    private Vector3f right;
    private Vector2f rotation;
    private Vector3f up;
    private Matrix4f viewMatrix;

    private float pitch = 20;
    private float yaw = 0;
    private float roll;
    private Player player;
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;



    public Camera() {
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();

    }

    public void addRotation(float x, float y) {
        rotation.add(x, y);
        check();
        recalculate();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void moveBackwards(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        check();
        recalculate();
    }

    public Vector3f getDirection() {
//        System.out.println(direction);
        return direction;
    }

    public void moveDown(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        check();
        recalculate();
    }

    public void moveForward(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        check();
        recalculate();
    }

    public void moveLeft(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        check();
        recalculate();
    }

    public void moveRight(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        check();
        recalculate();
    }

    public void moveUp(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.add(up);
        check();
        recalculate();
    }

    private void recalculate() {
        viewMatrix.identity()
                // pitch
                .rotateX(rotation.x)
                // yaw
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }

    public void check(){
        if (position.y < 0){
            position.y = 0 + 0.1f;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void move(){
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
//        recalculate();
    }

    private void calculateCameraPosition(float horizontal, float vertical){
        float teta = player.getRotation().y + angleAroundPlayer;
        float offsetX = (float) (horizontal * Math.sin(Math.toRadians(teta)));
        float offsetZ = (float) (horizontal * Math.cos(Math.toRadians(teta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + vertical;
    }
    public float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    public float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }


    public void calculateZoom(float input){
        distanceFromPlayer -= (input);
    }

    public void calculatePitch(float input){
//        float pitchChange = input;
        pitch -= input;
    }

    public void calculateAngleAroundPlayer(float input){
//        float angleChange = input;
        angleAroundPlayer -= input;
    }

}