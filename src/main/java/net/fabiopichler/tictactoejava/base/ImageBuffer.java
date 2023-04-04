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

package net.fabiopichler.tictactoejava.base;

import net.fabiopichler.tictactoejava.base.utils.FileUtils;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

public class ImageBuffer {
    private ByteBuffer buffer;
    private int width;
    private int height;
    private int bytesPerPixel;

    public void release() {
        if (buffer != null)
            stbi_image_free(buffer);

        this.width = 0;
        this.height = 0;
        this.bytesPerPixel = 0;
    }

    public boolean loadFromFile(final URL location) {
        try {
            return loadFromMemory(FileUtils.readToByteBuffer(location));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean loadFromMemory(final ByteBuffer buffer) {
        final IntBuffer width = BufferUtils.createIntBuffer(1);
        final IntBuffer height = BufferUtils.createIntBuffer(1);
        final IntBuffer components = BufferUtils.createIntBuffer(1);

        this.buffer = stbi_load_from_memory(buffer, width, height, components, STBI_rgb_alpha);

        if (this.buffer != null) {
            this.width = width.get();
            this.height = height.get();
            this.bytesPerPixel = 4;
            return true;
        }

        this.width = 0;
        this.height = 0;
        this.bytesPerPixel = 0;
        return false;
    }

    public ByteBuffer buffer() {
        return buffer;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int bytesPerPixel() {
        return bytesPerPixel;
    }
}
