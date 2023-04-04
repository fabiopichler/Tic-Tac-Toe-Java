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

import net.fabiopichler.tictactoejava.base.Color;
import net.fabiopichler.tictactoejava.base.TextureFilter;
import net.fabiopichler.tictactoejava.base.freetype.FontChar;
import net.fabiopichler.tictactoejava.base.freetype.DrawableChar;
import net.fabiopichler.tictactoejava.base.freetype.DrawableText;
import net.fabiopichler.tictactoejava.base.vectors.*;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.opengl.GL33C.*;

public class OpenGLRenderer {
    private final Programs programs;
    private final Buffers buffers;
    private final Textures textures;
    private final Vec2 viewportSize = new Vec2(640, 480);
    private final Vec2 logicalSize = new Vec2(640, 480);
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private final Matrix4f matrix = new Matrix4f();

    public OpenGLRenderer() {
        for (int i = 0; i < colorsBuffer.capacity(); ++i)
            colorsBuffer.put(0.0f);

        colorsBuffer.flip();

        final GLCapabilities glCaps = GL.createCapabilities();
        programs = new Programs();
        buffers = new Buffers(glCaps);
        textures = new Textures(glCaps);

        initGL();
    }

    public void release() {
        buffers.release();
        programs.release();
        textures.release();
    }

    private void initGL() {
        System.out.println("Game renderer: OpenGL");
        System.out.println("GL renderer: " + glGetString(GL_RENDERER));
        System.out.println("GL version: " + glGetString(GL_VERSION));

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        buffers.init();
        textures.init();

        for (final ProgramType value : ProgramType.values()) {
            try {
                updateProjection(programs.initProgram(value).uProjection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Texture2D createTexture(final ImageData image, final TextureFilter filter) {
        return textures.createTexture(image, filter);
    }

    public void destroyTexture(final Texture2D texture) {
        textures.destroyTexture(texture);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void draw(final Texture2D texture,
                     final IntRect srcrect, final Rect dstrect, final double angle, final Color color) {
        if (texture == null)
            return;

        final Locations program = programs.getProgram(ProgramType.Texture);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.id());

        if (srcrect != null) {
            glUniform4f(program.uSourcePosition,
                    srcrect.x / texture.width(), srcrect.y / texture.height(),
                    srcrect.w / texture.width(), srcrect.h / texture.height());
        }

        matrix.identity();

        if (angle == 0.0) {
            matrix.translate(dstrect.x, dstrect.y, 0);
        } else {
            final float center_x = dstrect.w / 2.0f;
            final float center_y = dstrect.h / 2.0f;

            matrix.translate(dstrect.x + center_x, dstrect.y + center_y, 0.0f);
            matrix.rotate((float)Math.toRadians(angle),0, 0, 1);
            matrix.translate(-center_x, -center_y, 0.0f);
        }

        matrix.scale(dstrect.w, dstrect.h, 0);

        glUniformMatrix4fv(program.uTransform, false, matrix.get(matrixBuffer));
        glUniform1i(program.uSampler, 0);

        buffers.enablePositionVBO(program);
        buffers.enableColorVBO(program, colorToBuffer(color));

        buffers.drawElements();

        buffers.disableColorVBO(program);
        buffers.disablePositionVBO(program);
    }

    public void draw(final DrawableText text, final float x, final float y, final Color color, final int visibleChars) {
        final Locations program = programs.getProgram(ProgramType.Font);
        final Texture2D texture = (Texture2D) text.font().texture();

        buffers.enablePositionVBO(program);
        buffers.enableColorVBO(program, colorToBuffer(color));

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.id());
        glUniform1i(program.uSampler, 0);

        int charIndex = 0;

        for (final DrawableChar drawableChar : text.characters()) {
            if (visibleChars > -1 && charIndex >= visibleChars)
                break;

            final FontChar fontChar = drawableChar.fontChar();

            glUniform4f(program.uSourcePosition,
                    fontChar.x() / texture.width(), fontChar.y() / texture.height(),
                    fontChar.width() / texture.width(), fontChar.height() / texture.height());

            matrix.identity();
            matrix.translate(x + drawableChar.x(), y + drawableChar.y(), 0);
            matrix.scale(drawableChar.width(), drawableChar.height(), 0);

            glUniformMatrix4fv(program.uTransform, false, matrix.get(matrixBuffer));

            buffers.drawElements();

            ++charIndex;
        }

        buffers.disableColorVBO(program);
        buffers.disablePositionVBO(program);
    }

    public void drawRect(final Rect rect, final Color color) {
        final Locations program = programs.getProgram(ProgramType.Color);

        matrix.identity();
        matrix.translate(rect.x, rect.y, 0);
        matrix.scale(rect.w, rect.h, 0);

        glUniformMatrix4fv(program.uTransform, false, matrix.get(matrixBuffer));

        buffers.enablePositionVBO(program);
        buffers.enableColorVBO(program, colorToBuffer(color));

        buffers.drawElements();

        buffers.disableColorVBO(program);
        buffers.disablePositionVBO(program);
    }

    public void setViewportSize(final int w, final int h) {
        viewportSize.x = w;
        viewportSize.y = h;

        int new_x, new_y, new_w, new_h;
        final float want_aspect = logicalSize.x / logicalSize.y;
        final float real_aspect = viewportSize.x / viewportSize.y;

        if (want_aspect > real_aspect) {
            final float scale = viewportSize.x / logicalSize.x;
            new_x = 0;
            new_w = (int) viewportSize.x;
            new_h = (int)Math.floor(logicalSize.y * scale);
            new_y = ((int) viewportSize.y - new_h) / 2;
        } else {
            final float scale = viewportSize.y / logicalSize.y;
            new_y = 0;
            new_h = (int) viewportSize.y;
            new_w = (int)Math.floor(logicalSize.x * scale);
            new_x = ((int) viewportSize.x - new_w) / 2;
        }

        glViewport(new_x, new_y, new_w, new_h);
    }

    public void setLogicalSize(final int w, final int h) {
        logicalSize.x = w;
        logicalSize.y = h;

        for (final ProgramType value : ProgramType.values())
            updateProjection(programs.getProgram(value).uProjection);
    }

    public void updateProjection(final int uProjection) {
        final var proj = new Matrix4f();
        final var view = new Matrix4f();
        final var model = new Matrix4f();

        proj.setOrtho(0.0f, logicalSize.x, logicalSize.y, 0.0f, -1.0f, 1.0f);
        view.setLookAt(
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);

        Matrix4f mvp = model.mul(view.mul(proj));

        glUniformMatrix4fv(uProjection, false, mvp.get(matrixBuffer));
    }

    private FloatBuffer colorToBuffer(final Color color) {
        final Color.Data colors = color.colors();

        colorsBuffer.put(0, colors.bottomLeft.r()).put(1, colors.bottomLeft.g())
                .put(2, colors.bottomLeft.b()).put(3, colors.bottomLeft.a());

        colorsBuffer.put(4, colors.bottomRight.r()).put(5, colors.bottomRight.g())
                .put(6, colors.bottomRight.b()).put(7, colors.bottomRight.a());

        colorsBuffer.put(8, colors.topRight.r()).put(9, colors.topRight.g())
                .put(10, colors.topRight.b()).put(11, colors.topRight.a());

        colorsBuffer.put(12, colors.topLeft.r()).put(13, colors.topLeft.g())
                .put(14, colors.topLeft.b()).put(15, colors.topLeft.a());

        return colorsBuffer;
    }
}
