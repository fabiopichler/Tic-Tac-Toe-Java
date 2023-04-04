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

import net.fabiopichler.tictactoejava.base.TextureFilter;
import net.fabiopichler.tictactoejava.base.freetype.FontManager;
import net.fabiopichler.tictactoejava.base.utils.IntRef;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.*;

import static org.lwjgl.opengl.GL33C.*;

public class Textures {
    private final GLCapabilities glCaps;

    public Textures(final GLCapabilities glCaps) {
        this.glCaps = glCaps;
    }

    public void release() {
        FontManager.instance().deleteTextures();
        FontManager.instance().removeEvents();
    }

    public void init() {
        FontManager.instance().setEvents(this::onCreateFontTexture, this::onDeleteFontTexture);
    }

    public Texture2D createTexture(final ImageData image, final TextureFilter filter) {
        final IntRef mode = new IntRef();
        final IntRef format = new IntRef();
        final int rowLength = image.pitch / image.bytesPerPixel;

        getFormat(image, mode, format);

        if (rowLength != image.width)
            glPixelStorei(GL_UNPACK_ROW_LENGTH, rowLength);

        final int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, mode.value, image.width, image.height, 0, format.value, GL_UNSIGNED_BYTE, image.buffer);

        if (glGetError() != GL_NO_ERROR)
            throw new RuntimeException("OpenGL error");

        filter(filter);
        clampToEdge();

        glBindTexture(GL_TEXTURE_2D, 0);
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);

        return new Texture2D(texture, image.width, image.height);
    }

    public void destroyTexture(final Texture2D texture) {
        if (texture != null)
            glDeleteTextures(texture.id());
    }

    private Texture2D onCreateFontTexture(final ByteBuffer pixels, final int width, final int height, final int filter) {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        final int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, pixels);

        if (glGetError() != GL_NO_ERROR)
            throw new RuntimeException("OpenGL error");

        filter(TextureFilter.values()[filter]);
        clampToEdge();

        glBindTexture(GL_TEXTURE_2D, 0);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);

        return new Texture2D(texture, width, height);
    }

    private void onDeleteFontTexture(final Object texture) {
        destroyTexture((Texture2D)texture);
    }

    private void getFormat(final ImageData image, final IntRef mode, final IntRef format) {
        if (image.bytesPerPixel == 4) {
            mode.value = GL_RGBA;
            format.value = GL_RGBA;

            if (image.rmask != 0x000000ff)
                format.value = GL_BGRA;

        } else {
            mode.value = GL_RGB;
            format.value = GL_RGB;

            if (image.rmask != 0x000000ff)
                format.value = GL_BGR;
        }
    }

    private void filter(final TextureFilter filter) {
        if (filter == TextureFilter.Linear) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        } else if (filter == TextureFilter.Mipmap && glCaps.OpenGL30) {
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        } else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        }
    }

    private void clampToEdge() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }
}
