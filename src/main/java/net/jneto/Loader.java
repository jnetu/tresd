package net.jneto;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, int[] indices){
        int vaoID = createVAO();
        bindIndixesBuffer(indices);
        storeDataInAttributeList(0,positions);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    public void cleanUp(){
        for(int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : vbos){
            GL30.glDeleteBuffers(vbo);
        }

    }

    private void storeDataInAttributeList(int attributeNumber, float[] data){
        int vboID = GL30.glGenBuffers();
        vbos.add(vboID);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attributeNumber,3,GL30.GL_FLOAT,false,0,0);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }
    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndixesBuffer(int[] indices){
        int vboID = GL30.glGenBuffers();
        vbos.add(vboID);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
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
