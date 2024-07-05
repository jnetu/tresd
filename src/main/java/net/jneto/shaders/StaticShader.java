package net.jneto.shaders;

import java.io.IOException;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/main/java/net/jneto/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/main/java/net/jneto/shaders/fragmentShader.glsl";

    public StaticShader() throws IOException {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0,"position");
        super.bindAttributes(1,"textureCoords");
    }
}
