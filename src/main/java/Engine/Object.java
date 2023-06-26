package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Object extends ShaderProgram{
    List<Vector3f> vertices;
    int vao;
    int vbo;
    Vector4f color;
    UniformsMap uniformsMap;

    List<Vector3f> verticesColor;
    int vboColor;
    boolean hasTransparency = false;

    Matrix4f model;

    List<Object> childObject;

    public List<Object> getChildObject() {
        return childObject;
    }

    public void setChildObject(List<Object> childObject) {
        this.childObject = childObject;
    }

    public Vector3f updateCenterPoint(){
        Vector3f centerTemp = new Vector3f();
        model.transformPosition(0.0f,0.0f,0.0f,centerTemp);
        return centerTemp;
    }


    public Object(List<ShaderModuleData> shaderModuleDataList,
                  List<Vector3f> vertices,
                  Vector4f color) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.color = color;
        uniformsMap = new UniformsMap(getProgramId());
//        uniformsMap.createUniform(
//                "uni_color");
//        uniformsMap.createUniform(
//                "model");
//        uniformsMap.createUniform(
//                "view");
//        uniformsMap.createUniform(
//                "projection");
        model = new Matrix4f();
        childObject = new ArrayList<>();
    }
    public Object(List<ShaderModuleData> shaderModuleDataList,
                  List<Vector3f> vertices,
                  List<Vector3f> verticesColor) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.verticesColor = verticesColor;
        setupVAOVBOWithVerticesColor();
    }

    public void setupVAOVBO(){
        //set vao
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        //set vbo
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
    }
    public void setupVAOVBOWithVerticesColor(){
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
        //set vboColor
        vboColor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        //mengirim vbocolor ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(verticesColor),
                GL_STATIC_DRAW);
    }
    public void drawSetup(Camera camera, Projection projection){
        bind();

        uniformsMap.setUniform(
                "model", model);
        uniformsMap.setUniform(
                "view", camera.getViewMatrix());
        uniformsMap.setUniform(
                "projection", projection.getProjMatrix());

        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0,
                3, GL_FLOAT,
                false,
                0, 0);
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public void drawSetupWithVerticesColor(){
        bind();
        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0,
                3, GL_FLOAT,
                false,
                0, 0);
        // Bind VBOColor
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);
    }
    public void draw(Camera camera,Projection projection){
        System.out.println("draw parent");
        drawSetup(camera,projection);
        // Draw the vertices
        glLineWidth(10);
        glPointSize(10);
        //GL_TRIANGLES
        //GL_LINE_LOOP
        //GL_LINE_STRIP
        //GL_LINES
        //GL_POINTS
        //GL_TRIANGLE_FAN
        glDrawArrays(GL_TRIANGLES, 0,
                vertices.size());
        for(Object child:childObject){
            child.draw(camera,projection);
        }
    }
    public void drawWithVerticesColor(){
        drawSetupWithVerticesColor();
        // Draw the vertices
        glLineWidth(10);
        glPointSize(10);
        //GL_TRIANGLES
        //GL_LINE_LOOP
        //GL_LINE_STRIP
        //GL_LINES
        //GL_POINTS
        //GL_TRIANGLE_FAN
        glDrawArrays(GL_TRIANGLES, 0,
                vertices.size());
    }
    public void drawLine(){
//        drawSetup();
        // Draw the vertices
        glLineWidth(1);
        glPointSize(1);
        glDrawArrays(GL_LINE_STRIP, 0,
                vertices.size());
    }
    public void translateObject(Float offsetX,Float offsetY,Float offsetZ){
        model = new Matrix4f().translate(offsetX,offsetY,offsetZ).mul(new Matrix4f(model));
        for (Object child:childObject){
            child.translateObject(offsetX,offsetY,offsetZ);
        }
    }
    public void rotateObject(Float degree, Float offsetX,Float offsetY,Float offsetZ){
        model = new Matrix4f().rotate(degree,offsetX,offsetY,offsetZ).mul(new Matrix4f(model));
        for (Object child:childObject){
            child.rotateObject(degree,offsetX,offsetY,offsetZ);
        }
    }
    public void scaleObject(Float x,Float y,Float z){
        model = new Matrix4f().scale(x,y,z).mul(new Matrix4f(model));
        for (Object child:childObject){
            child.scaleObject(x,y,z);
        }
    }
}
