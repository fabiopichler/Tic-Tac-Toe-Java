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

package net.fabiopichler.tictactoejava.base.freetype;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImageWrite.stbi_write_png;

public class FreeTypeUtils {
    public static void savePng(final ByteBuffer pixelBuffer, final int width, final int height, final String name) {
        pixelBuffer.limit(pixelBuffer.capacity());

        final ByteBuffer pngBuffer = BufferUtils.createByteBuffer(pixelBuffer.capacity() * 4);

        for (int i = 0; i < pixelBuffer.capacity(); ++i) {
            pngBuffer.put(i * 4, pixelBuffer.get(i));
            pngBuffer.put(i * 4 + 1, pixelBuffer.get(i));
            pngBuffer.put(i * 4 + 2, pixelBuffer.get(i));
            pngBuffer.put(i * 4 + 3, (byte) 0xff);
        }

        stbi_write_png("build/" + name + ".png",
                width, height, 4, pngBuffer, width * 4);
    }
}
