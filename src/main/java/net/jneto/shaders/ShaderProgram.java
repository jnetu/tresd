package net.jneto.shaders;

import static org.lwjgl.opengl.GL30.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;


public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4*4);

    public ShaderProgram(String vertexShaderFile, String fragmentShaderFile) throws IOException {
        vertexShaderID = loadShader(vertexShaderFile,GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShaderFile, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        glLinkProgram(programID);
        glValidateProgram(programID);
        getAllUniformLocation();

    }
    
    protected abstract void getAllUniformLocation();
    
    protected int getUniformLocation(String uniformName) {
    	return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void start(){
        glUseProgram(programID);
    }
    public void stop(){
        glUseProgram(0);
    }
    public void cleanUp(){
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttributes(int attribute, String variableName) {
        glBindAttribLocation(programID, attribute, variableName);
    }
    
    protected void loadFloat(int location, float value) {
    	GL20.glUniform1f(location,value);
    }
    protected void loadInt(int location, int value) {
    	GL20.glUniform1i(location, value);
    }
    protected void loadVector(int location, Vector3f vector) {
    	GL20.glUniform3f(location, vector.x, vector.y,vector.z); 	
    }
    protected void loadVector(int location, boolean value) {
    	float toLoad = 0;
    	if(value) {
    		toLoad = 1;
    	}
    	GL20.glUniform1f(location, toLoad);
    }
    
    protected void loadMatrix(int location, Matrix4f matrix) {
    	//matrix.store(matrixBuffer); --LWJGL 2
    	matrix.get(matrixBuffer);
    	matrixBuffer.flip();
    	GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private static int loadShader(String file, int type) throws IOException {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");

            }
            reader.close();
        }catch (IOException e){
            System.err.println("Could not load shader file: " + file + " - " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if(glGetShaderi(shaderID,GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println(glGetShaderInfoLog(shaderID));
            System.err.println("Could not compile shader file: " + file);
            System.exit(1);
        }
        return shaderID;
    }
}
