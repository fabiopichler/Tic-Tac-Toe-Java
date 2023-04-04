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

import net.fabiopichler.tictactoejava.base.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.freetype.FT_Face;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

import static org.lwjgl.util.freetype.FreeType.FT_Init_FreeType;
import static org.lwjgl.util.freetype.FreeType.FT_New_Memory_Face;

public class FreeType {
    public static long initFreeType() {
        final PointerBuffer libraryBuffer = BufferUtils.createPointerBuffer(1);
        int error;

        if ((error = FT_Init_FreeType(libraryBuffer)) != 0)
            throw new RuntimeException("Could not init FreeType Library. Code " + error);

        return libraryBuffer.get();
    }

    public static FT_Face createFace(final long library, final String fileName) {
        final PointerBuffer faceBuffer = BufferUtils.createPointerBuffer(1);
        int error;

        if ((error = FT_New_Memory_Face(library, getFontBuffer(fileName), 0, faceBuffer)) != 0)
            throw new RuntimeException("Failed to load font. Code " + error);

        final FT_Face face = FT_Face.createSafe(faceBuffer.get());

        return Objects.requireNonNull(face);
    }

    public static ByteBuffer getFontBuffer(final String fileName) {
        try {
            return FileUtils.readToByteBuffer(FreeType.class.getResource(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
