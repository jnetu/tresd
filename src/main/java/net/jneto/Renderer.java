package net.jneto;

import org.lwjgl.opengl.GL30;

public class Renderer {

    public void prepare(){
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);
        GL30.glClearColor(1,0,0,1);

    }

    public void render(RawModel model){
        GL30.glBindVertexArray(model.getVaoID());
        GL30.glEnableVertexAttribArray(0);
        //GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, model.getVertexCount());
        GL30.glDrawElements(GL30.GL_TRIANGLES, model.getVertexCount(), GL30.GL_UNSIGNED_INT,0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
