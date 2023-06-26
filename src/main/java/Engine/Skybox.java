package Engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Skybox extends Object{
    /**
     * vertices sama dengan positions
     */


    private static final float SIZE = 500f;
    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };
    public int tex_tbo;
    int night_tex_tbo;

    private static final float ROTATE_SPEED = 1f;
    private float rotation = 0;
    private float time = 0;
    private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
    private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};

    public Skybox(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color) {
        super(shaderModuleDataList, vertices, color);
        uniformsMap = new UniformsMap(getProgramId());

        this.vertices = Utils.floatToList(VERTICES);

        setupVAOVBO();
        this.tex_tbo = loadCubeMap(TEXTURE_FILES);
        this.night_tex_tbo = loadCubeMap(NIGHT_TEXTURE_FILES);
    }

    private TextureData decodeTextureFile(String filename){
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;

        try {
            FileInputStream in = new FileInputStream(filename);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("file didnt work");
        }
        return new TextureData(width, height, buffer);
    }

    public int loadCubeMap(String[] textureFile){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < textureFile.length;i++){
            TextureData data = decodeTextureFile("D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources" +
                    "\\Skybox_texture\\" + textureFile[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(),0, GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        return texID;
    }

    @Override
    public void setupVAOVBO(){
        //set vao
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        //set vbo
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                VERTICES,
                GL_STATIC_DRAW);
    }

    public void drawSetup(Camera camera, Projection projection){
        bind();
        Matrix4f viewMat = new Matrix4f(camera.getViewMatrix());
        viewMat.m30(0);
        viewMat.m31(0);
        viewMat.m32(0);
        rotation += ROTATE_SPEED * 0.005f;
        viewMat.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), viewMat);

        uniformsMap.setUniform(
                "view",viewMat);
        uniformsMap.setUniform(
                "projection", projection.getProjMatrix());
        uniformsMap.setUniform("cubeMap", 0);
        uniformsMap.setUniform("cubeMap2", 1);

        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0,
                3, GL_FLOAT,
                false,
                0, 0);
    }

    public void draw(Camera camera,Projection projection){
        drawSetup(camera,projection);
        // Draw the vertices

        bindTextures();

        glDrawArrays(GL_TRIANGLES, 0,
                vertices.size());

    }

    private void bindTextures(){
        time += 0.005f * 1000;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if(time >= 0 && time < 5000){

            texture1 = tex_tbo;
            texture2 = tex_tbo;
            blendFactor = (time - 0)/(5000 - 0);
//            uniformsMap.setUniform("blendFactor", blendFactor);
        }else if(time >= 5000 && time < 8000){
            texture1 = tex_tbo;
            texture2 = night_tex_tbo;
            blendFactor = (time - 5000)/(8000 - 5000);
//            uniformsMap.setUniform("blendFactor", blendFactor);
        }else if(time >= 8000 && time < 21000){
            texture1 = night_tex_tbo;
            texture2 = night_tex_tbo;
            blendFactor = (time - 8000)/(21000 - 8000);
//            uniformsMap.setUniform("blendFactor", blendFactor);
        }else{
            texture1 = night_tex_tbo;
            texture2 = tex_tbo;
            blendFactor = (time - 21000)/(24000 - 21000);
//            uniformsMap.setUniform("blendFactor", blendFactor);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        uniformsMap.setUniform("blendFactor", blendFactor);
//        shader.loadBlendFactor(blendFactor);
    }

}
