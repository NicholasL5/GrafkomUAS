package Engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;


import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;
    private static int fps;

    private static long lastFrameTime;
    private static float delta;

    private long window;
    private boolean open=  true;
    private int width, height;
    private String title;
    private boolean isRunning = false;

    private  MouseInput mouseInput;

    public Window(int width, int height, String title){
        this.width = width;
        this.height = height;
        this.title = title;
        isRunning = true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOpen(){
        return open;
    }

    public void init(){

        //Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width+2, height+2, title, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        mouseInput = new MouseInput(window);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        lastFrameTime = getCurrentTime();
    }

    public void update(){

        // Set the clear color

        glfwSwapBuffers(window); // swap the color buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glViewport(0,0,width,height);

        if(glfwWindowShouldClose(window))
            open = false;

        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;

        mouseInput.input();
    }

    public void cleanup(){
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    public static float getFrameTimeSeconds(){
        return delta;
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public static long getCurrentTime() {
        return System.nanoTime() / 1_000_000;
    }

//    public void run(){
//        this.isRunning = true;
//        int frames = 0;
//        long frameCounter = 0;
//        long lastTime = System.nanoTime();
//        double unprocessedTime = 0;
//
//        while (isRunning){
//            boolean render = false;
//            long startTime = System.nanoTime();
//            long passedTime = startTime - lastTime;
//            lastTime = startTime;
//
//            unprocessedTime += passedTime/(double) NANOSECOND;
//            frameCounter += passedTime;
//
//            input();
//
//            //input
//            while (unprocessedTime > frameTime){
//                render = true;
//                unprocessedTime -= frameTime;
//
//                if (window.windowShouldClose()){
//                    stop();
//                }
//
//                if (frameCounter >= NANOSECOND){
//                    setFps(frames);
//                    this.setTitle("test FPS:" + getFps());
//                    frames = 0;
//                    frameCounter = 0;
//                }
//
//            }
//            if (render){
//                update(frameTime);
//                render();
//                frames++;
//            }
//
//        }
//
//
//    }
//
//    public void stop(){
//        isRunning = false;
//    }
//
//    public boolean isRunning() {
//        return isRunning;
//    }
}
