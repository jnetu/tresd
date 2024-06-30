package net.jneto;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import java.io.IOException;

public class MainGame {

    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();
        displayManager.init();

        try {
            new MainGame().run(displayManager.getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }

        displayManager.cleanUp();
    }

    public void run(long window) throws IOException {
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        ShaderProgram shader = new ShaderProgram("src/main/java/net/jneto/vertexShader.glsl", "src/main/java/net/jneto/fragmentShader.glsl");

        float[] vertices = {
                -0.5f, 0.5f, 0f,   //v0
                -0.5f, -0.5f, 0f,  //v1
                0.5f, -0.5f, 0f,   //v2
                0.5f, 0.5f, 0f     //v3
        };
        //order to render indices method:
        int[] indices = {
                0, 1, 3, //triangle: v0,v1,v3
                3, 1, 2  //triangle: v3,v1,v2
        };

        RawModel model = loader.loadToVAO(vertices, indices);

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
            shader.start();
            renderer.prepare();
            renderer.render(model);
            shader.stop();
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        loader.cleanUp();
        shader.cleanUp();
    }
}
