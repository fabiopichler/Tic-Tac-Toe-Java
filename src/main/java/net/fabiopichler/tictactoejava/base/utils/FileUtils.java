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

package net.fabiopichler.tictactoejava.base.utils;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class FileUtils {
    public static String readToString(final URL location) throws IOException {
        if (location == null)
            throw new IOException("Location resource not found");

        try (final InputStream stream = location.openStream()) {
            if (stream != null)
                return new String(stream.readAllBytes());

        } catch (Exception e) {
            throw new IOException(e);
        }

        return null;
    }

    public static ByteBuffer readToByteBuffer(final URL location) throws IOException {
        if (location == null)
            throw new IOException("Location resource not found");

        try (final InputStream stream = location.openStream()) {
            if (stream != null) {
                final byte[] bytes = stream.readAllBytes();

                return BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
            }

        } catch (Exception e) {
            throw new IOException(e);
        }

        throw new IOException("readFileBuffer error");
    }
}
