package net.jneto.shaders;

import java.io.IOException;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/main/java/net/jneto/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/main/java/net/jneto/shaders/fragmentShader.glsl";
    
    private int location_transformationMatrix;
    private int location_textureSampler;
    
    public StaticShader() throws IOException {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0,"position");
        super.bindAttributes(1,"textureCoords");
    }

	@Override
	protected void getAllUniformLocation() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_textureSampler = super.getUniformLocation("textureSampler");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_textureSampler, 0);
	}
}
