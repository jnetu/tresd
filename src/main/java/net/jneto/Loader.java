package net.jneto;

import net.jneto.models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL30.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3, positions);
        storeDataInAttributeList(1,2, textureCoords);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }
    private int createVAO(){
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

//    public int loadTexture(String filename) throws IOException {
//        Texture texture = null;
//        texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + filename + ".png"));
//        int textureID = texture.getTextureID();
//        textures.add(textureID);
//        return textureID;
//    }

    public int loadTexture(String filename) throws IOException {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            buffer = STBImage.stbi_load("res/" + filename + ".png", w, h, comp, 4);
            if (buffer == null) {
                throw new RuntimeException("Failed to load texture file: " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        STBImage.stbi_image_free(buffer);

        textures.add(textureID);
        return textureID;
    }

    public void cleanUp(){
        for(int vao : vaos){
            glDeleteVertexArrays(vao);
        }
        for(int vbo : vbos){
            glDeleteBuffers(vbo);
        }
        for (int texture : textures){
            glDeleteTextures(texture);
        }

    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber,coordinateSize,GL_FLOAT,false,0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    private void unbindVAO(){
        glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
