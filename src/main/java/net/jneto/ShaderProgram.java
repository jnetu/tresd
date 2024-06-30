package net.jneto;
import org.lwjgl.opengl.GL30;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programID;

    public ShaderProgram(String vertexFile, String fragmentFile) throws IOException {
        int vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
    }

    private int loadShader(String file, int type) throws IOException {
        StringBuilder shaderSource = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get(file))) {
            shaderSource.append(line).append("\n");
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(shaderID, 500));
            throw new IllegalStateException("Could not compile shader");
        }
        return shaderID;
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        glDeleteProgram(programID);
    }
}
