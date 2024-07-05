package net.jneto;

import net.jneto.models.RawModel;
import net.jneto.models.TexturedModel;
import net.jneto.shaders.StaticShader;
import net.jneto.textures.ModelTexture;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;

public class MainGame {

    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();
        displayManager.init();

        try {
            new MainGame().run(displayManager.getWindow());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        displayManager.cleanUp();
    }

    public void run(long window) throws IOException {
        Loader loader = new Loader();
        Renderer renderer = new Renderer();

        StaticShader shader = new StaticShader();


        float[] vertices = {
                -0.5f,  0.5f, 0f,   // V0
                -0.5f, -0.5f, 0f,   // V1
                0.5f, -0.5f, 0f,   // V2
                0.5f,  0.5f, 0f    // V3
        };
        //order to render indices method:
        int[] indices = {
                0, 1, 3, //triangle: v0,v1,v3
                3, 1, 2  //triangle: v3,v1,v2
        };

        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        RawModel model = loader.loadToVAO(vertices, textureCoords,indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("tree"));
        TexturedModel texturedModel = new TexturedModel(model, texture);



        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            shader.start();
            renderer.prepare();
            renderer.render(texturedModel);
            shader.stop();
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        loader.cleanUp();
        shader.cleanUp();
    }
}
