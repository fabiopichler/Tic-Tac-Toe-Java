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

import net.fabiopichler.tictactoejava.base.vectors.IntVec2;
import org.lwjgl.glfw.GLFWImage;

import java.net.URL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private final long nativeWindow;
    private final IntVec2 size;

    public Window(final int width, final int height, final String title) {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        size = new IntVec2(width, height);
        nativeWindow = glfwCreateWindow(width, height, title, NULL, NULL);

        if (nativeWindow == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(nativeWindow);
        glfwSwapInterval(1);
    }

    public void release() {
        glfwFreeCallbacks(nativeWindow);
        glfwDestroyWindow(nativeWindow);
    }

    public void setWindowIcon(final URL location) {
        final var imageBuffer = new ImageBuffer();

        if (imageBuffer.loadFromFile(location)) {
            final GLFWImage glfwImage = GLFWImage.malloc();
            glfwImage.set(imageBuffer.width(), imageBuffer.height(), imageBuffer.buffer());

            final GLFWImage.Buffer glfwImageBuffer = GLFWImage.malloc(1);
            glfwImageBuffer.put(0, glfwImage);

            glfwSetWindowIcon(nativeWindow, glfwImageBuffer);

            glfwImage.free();
            imageBuffer.release();
        }
    }

    public void show() {
        glfwShowWindow(nativeWindow);
    }

    public void hide() {
        glfwHideWindow(nativeWindow);
    }

    public void setWindowTitle(final String title) {
        glfwSetWindowTitle(nativeWindow, title);
    }

    public long nativeWindow() {
        return nativeWindow;
    }

    public IntVec2 size() {
        return size;
    }

    public void swapBuffers() {
        glfwSwapBuffers(nativeWindow);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(nativeWindow);
    }
}
