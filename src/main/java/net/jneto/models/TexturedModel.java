package net.jneto.models;

import net.jneto.textures.ModelTexture;

public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;

    public TexturedModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public ModelTexture getTexture() {
        return texture;
    }
    public RawModel getRawModel() {
        return rawModel;
    }
}
