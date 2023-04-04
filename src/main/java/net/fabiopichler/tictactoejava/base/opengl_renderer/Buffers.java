/*-------------------------------------------------------------------------------

Copyright (c) 2020-2023 Fábio Pichler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

-------------------------------------------------------------------------------*/

package net.fabiopichler.tictactoejava.base.opengl_renderer;

import org.lwjgl.*;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.*;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Buffers {
    private final GLCapabilities glCaps;
    private int vao;
    private int positionVbo;
    private int colorVbo;
    private int elementBuffer;
    private int indicesCount;
    private int verticesCount;

    public Buffers(final GLCapabilities glCaps) {
        this.glCaps = glCaps;
    }

    public void release() {
        glDeleteBuffers(positionVbo);
        glDeleteBuffers(elementBuffer);
        glDeleteBuffers(colorVbo);

        if (glCaps.OpenGL30)
            glDeleteVertexArrays(vao);
    }

    public void init() {
        if (glCaps.OpenGL30) {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);
        }

        indicesCount = 3 * 2;

        IntBuffer indices = BufferUtils.createIntBuffer(indicesCount);
        indices.put(0).put(1).put(2);
        indices.put(0).put(2).put(3);
        indices.flip();

        verticesCount = 4 * 4;

        FloatBuffer vertices = BufferUtils.createFloatBuffer(verticesCount);
        vertices.put(0.0f).put(1.0f).put(0.0f).put(1.0f);
        vertices.put(1.0f).put(1.0f).put(1.0f).put(1.0f);
        vertices.put(1.0f).put(0.0f).put(1.0f).put(0.0f);
        vertices.put(0.0f).put(0.0f).put(0.0f).put(0.0f);
        vertices.flip();

        elementBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        positionVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, positionVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        colorVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferData(GL_ARRAY_BUFFER, 4 * 4 * 4, GL_DYNAMIC_DRAW);
    }

    public void enablePositionVBO(final Locations program) {
        glBindBuffer(GL_ARRAY_BUFFER, positionVbo);

        glEnableVertexAttribArray(program.aPosition);
        glVertexAttribPointer(program.aPosition, 2, GL_FLOAT, false, verticesCount, 0L);

        if (program.aUV != -1) {
            glEnableVertexAttribArray(program.aUV);
            glVertexAttribPointer(program.aUV, 2, GL_FLOAT, false, verticesCount, 8L);
        }
    }

    public void disablePositionVBO(final Locations program) {
        glDisableVertexAttribArray(program.aPosition);

        if (program.aUV != -1)
            glDisableVertexAttribArray(program.aUV);
    }

    public void enableColorVBO(final Locations program, final FloatBuffer colorsBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, colorsBuffer);
        glEnableVertexAttribArray(program.aColor);
        glVertexAttribPointer(program.aColor, 4, GL_FLOAT, false, 0, 0L);
    }

    public void disableColorVBO(final Locations program) {
        glDisableVertexAttribArray(program.aColor);
    }

    public void drawElements() {
        glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, NULL);
    }
}
