package Engine;

import jdk.jshell.execution.Util;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Terrain extends Object{
    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;
    public static final float MAX_HEIGHT = 40;
    public static final float MIN_HEIGHT = -40;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    private Vector3f position;
    Float radiusZ;
    List<Integer> index;
    List<Vector3f> normal;
    List<Vector2f> textureCoordinates;
    int ibo;
    int nbo;
    int tbo;
    int tex_tbo;

    private float x;
    private float z;

    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private float[][] heights;
    private final String PATHNAME = "D:\\projects\\java\\GrafkomKelasLampu\\GrafkomKelas\\resources\\textures\\";


    public Terrain(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, int gridX, int gridZ) {
        super(shaderModuleDataList, vertices, color);
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;


        generateTerrain("D:\\projects\\java\\GrafkomKelas\\resources\\textures\\heightmap.png");
        setIbo();
        setupVAOVBO();

        try {
            blendMap = new TerrainTexture(loadTexture(PATHNAME + "blendMap.png"));
        } catch (Exception e) {
            System.out.println("problem in blendmap");
            throw new RuntimeException(e);
        }
        try {
            texturePack = new TerrainTexturePack(
                    //bg
                    new TerrainTexture(loadTexture(PATHNAME +"terrain.png")),
                    //r
                    new TerrainTexture(loadTexture(PATHNAME + "mud.png")),
                    //g
                    new TerrainTexture(loadTexture(PATHNAME + "grassFlowers.png")),
                    //b
                    new TerrainTexture(loadTexture(PATHNAME + "dirt_road.png"))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setIbo(){
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.listoInt(index), GL_STATIC_DRAW);
    }


    private void generateTerrain(String heightMap){

//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(new File(heightMap));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        int VERTEX_COUNT = image.getHeight();

        heights = new float[VERTEX_COUNT][VERTEX_COUNT];


        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] _vertices = new float[count * 3];
        float[] _normals = new float[count * 3];
        float[] _textureCoords = new float[count*2];
        int[] _indices = new int[6 * (VERTEX_COUNT-1) * (VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0; i< VERTEX_COUNT; i++){
            for(int j=0;j < VERTEX_COUNT;j++){
                _vertices[vertexPointer * 3] = -(float) j / ((float)VERTEX_COUNT - 1) * SIZE;

//                float height = getHeight(j, i ,image);
//                heights[j][i] = height;

                 _vertices[vertexPointer * 3 + 1] = 0;
//                _vertices[vertexPointer * 3 + 1] = height;

                _vertices[vertexPointer * 3 + 2] = -(float) i / ((float)VERTEX_COUNT - 1) * SIZE;

//                Vector3f normal = calculateNormal(j, i, image);
//                _normals[vertexPointer * 3] = normal.x;
//                _normals[vertexPointer * 3 + 1] = normal.y;
//                _normals[vertexPointer * 3 + 2] = normal.z;

                _normals[vertexPointer * 3] =0;
                _normals[vertexPointer * 3 + 1] =1;
                _normals[vertexPointer * 3 + 2] = 0;

                _textureCoords[vertexPointer * 2] = (float) j / ((float)VERTEX_COUNT - 1);
                _textureCoords[vertexPointer * 2 + 1] = (float) i / ((float)VERTEX_COUNT - 1);

                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int z=0;z<VERTEX_COUNT-1;z++){
            for(int x=0;x<VERTEX_COUNT-1;x++){
                int topLeft = (z * VERTEX_COUNT) + x;
                int topRight = topLeft + 1;
                int bottomLeft = ((z + 1) * VERTEX_COUNT) + x;
                int bottomRight = bottomLeft + 1;
                _indices[pointer++] = topLeft;
                _indices[pointer++] = bottomLeft;
                _indices[pointer++] = topRight;
                _indices[pointer++] = topRight;
                _indices[pointer++] = bottomLeft;
                _indices[pointer++] = bottomRight;
            }
        }

        this.vertices = Utils.floatToList(_vertices);
        this.normal = Utils.floatToList(_normals);
        this.textureCoordinates = Utils.floatToList2(_textureCoords);
        this.index = Utils.intToList(_indices);
    }

    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;

        float gridSquareSize = SIZE / ((float) heights.length - 1);

        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0){
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;
        if (xCoord <= (1-zCoord)){
            answer = barryCentric(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        }else {
            answer = barryCentric(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        }
        return answer;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image){
        // ambil semua height di tetangganya
        float heightL = getHeight(x-1, z, image);
        float heightR = getHeight(x+1, z, image);
        float heightD = getHeight(x, z-1, image);
        float heightU = getHeight(x, z+1, image);

        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD-heightU);
        normal.normalize();
        return normal;
    }

    private float getHeight(int x, int z, BufferedImage image){
        if(x<0 || x>=image.getHeight() || z < 0 || z>=image.getHeight()){
            return 0;
        }
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR/2f;
        height /= MAX_PIXEL_COLOR/2f;
        height *= MAX_HEIGHT;
        return height;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }


    public int loadTexture(String filename) throws Exception{
        int width, height;
        ByteBuffer buffer;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null)
                throw new Exception("Image File " + filename + " not load " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int texID = GL11.glGenTextures();

        //bind texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0 ,GL11.GL_RGBA, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        tex_tbo = texID;
        return texID;
    }

    public void setupVAOVBO(){
        super.setupVAOVBO();

        //nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat2(textureCoordinates),
                GL_STATIC_DRAW);

    }

    public void draw(Camera camera, Projection projection) {
        this.drawSetup(camera, projection);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex_tbo);

        bindTextures();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        glDrawElements(GL_TRIANGLES, index.size(), GL_UNSIGNED_INT, 0);
    }

    // bind texture banyak
    public void bindTextures(){
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTexID());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTexID());

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTexID());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTexID());

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, blendMap.getTexID());
    }


    public void drawSetup(Camera camera, Projection projection){
        super.drawSetup(camera,projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);

        //directional Light
//        uniformsMap.setUniform("dirLight.direction",new Vector3f(-0.2f,-1.0f,2f));
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-6.266E-3f, -4.993E-1f,  2.630E-2f));
//        uniformsMap.setUniform("dirLight.ambient",new Vector3f(0.05f,0.05f,0.05f));
//        uniformsMap.setUniform("dirLight.diffuse",new Vector3f(0.4f,0.4f,0.4f));
//        uniformsMap.setUniform("dirLight.specular",new Vector3f(0.5f,0.5f,0.5f));
//        uniformsMap.setUniform("dirLight.direction",new Vector3f(-0.2f,-1.0f,-0.3f));
        uniformsMap.setUniform("dirLight.ambient",new Vector3f(0.05f,0.05f,0.05f));
        uniformsMap.setUniform("dirLight.diffuse",new Vector3f(0.1f,0.2f,0.1f));
        uniformsMap.setUniform("dirLight.specular",new Vector3f(0f,0f,0f));

        //posisi pointLight
        Vector3f[] _pointLightPositions = {
                //diluar kota
//                new Vector3f(-5E+2f,  5f, -2.527E+2f),
                //atap kiri rumah utama
//                new Vector3f(-4.132E+2f,  18f, -2.274E+2f),
                //sebelah rumah utama
//                new Vector3f(-4.002E+2f,  5f, -2.526E+2f),
                //gtw apa
                new Vector3f(-4.387E+2f,  10f, -3.374E+2f),
                //lampu rumah tengah atas
                new Vector3f(-4.178E+2f,  18f, -2.654E+2f),
                //lampu rumah tengah teras
                new Vector3f(-4.180E+2f,  10f, -2.370E+2f),
                //gtw apa
                new Vector3f(-3.901E+2f,  0.000E+0f, -3.895E+2f),
                // api unggun
                new Vector3f(-4.176E+2f,  4, -2.689E+2f),
                //lampu
                new Vector3f(-4.355E+2f,  8.744E+0f, -3.147E+2f),
                //lampu rumah 2 lantai
                new Vector3f(-4.471E+2f,  8.744E+0f, -2.472E+2f),
                // lampu jalan 2
                new Vector3f(-3.341E+2f,  8.744E+0f, -2.557E+2f),
                // lampu jalan 3
                new Vector3f(-4.445E+2f,  8.744E+0f, -1.915E+2f),
                //lampu rumah tukang
                new Vector3f(-456f,  6.0f, -2.836E+2f),
                //lampu rumah 2 lantai
                new Vector3f(-3.877E+2f,  8.744E+0f, -2.773E+2f)
        };

        for(int i = 0;i<_pointLightPositions.length;i++) {
            if (i == 4) {
                uniformsMap.setUniform("pointLights[" + i + "].position", _pointLightPositions[i]);
                uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
                uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(3.8f, 3.8f, 3.8f));
                uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(4.0f, 4.0f, 4.0f));
                uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
                uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
                uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
                uniformsMap.setUniform("pointLights[" + i + "].lampColor", new Vector3f(1.0f, 1.0f, 1f));
                continue;
            } else {
                uniformsMap.setUniform("pointLights[" + i + "].position", _pointLightPositions[i]);
                uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
                uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(3.8f, 3.8f, 3.8f));
                uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(4.0f, 4.0f, 4.0f));
                uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
                uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
                uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
                uniformsMap.setUniform("pointLights[" + i + "].lampColor", new Vector3f(1.0f, 1.0f, 1.0f));
            }
        }

        //spotlight
        uniformsMap.setUniform("spotLight.position",camera.getPosition());
        uniformsMap.setUniform("spotLight.direction",camera.getDirection());
        uniformsMap.setUniform("spotLight.ambient",new Vector3f(1.0f,1.0f,1.0f));
        uniformsMap.setUniform("spotLight.diffuse",new Vector3f(5.0f,5.0f,5.0f));
        uniformsMap.setUniform("spotLight.specular",new Vector3f(5.0f,5.0f,5.0f));
        uniformsMap.setUniform("spotLight.constant",10.0f);
        uniformsMap.setUniform("spotLight.linear",10f);
        uniformsMap.setUniform("spotLight.quadratic",10f);
        uniformsMap.setUniform("spotLight.cutOff",(float)Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("spotLight.outerCutOff",(float)Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("viewPos",camera.getPosition());

        uniformsMap.setUniform("backgroundTexture", 0);
        uniformsMap.setUniform("rTexture", 1);
        uniformsMap.setUniform("gTexture", 2);
        uniformsMap.setUniform("bTexture", 3);
        uniformsMap.setUniform("blendMap", 4);


        // bind texture
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glVertexAttribPointer(2,
                2, GL_FLOAT,
                false,
                0, 0);


    }



    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }
}
