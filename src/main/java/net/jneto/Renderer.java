package net.jneto;

import net.jneto.models.RawModel;
import net.jneto.models.TexturedModel;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    public void prepare(){
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(1,0,0,1);

    }
    public void render(TexturedModel texturedModel){
        RawModel model = texturedModel.getRawModel();
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT,0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
