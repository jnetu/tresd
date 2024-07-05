package net.jneto.shaders;

import static org.lwjgl.opengl.GL30.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(String vertexShaderFile, String fragmentShaderFile) throws IOException {
        vertexShaderID = loadShader(vertexShaderFile,GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShaderFile, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
        bindAttributes();
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
