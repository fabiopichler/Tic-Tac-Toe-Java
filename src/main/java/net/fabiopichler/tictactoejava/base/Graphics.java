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

import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.base.vectors.IntVec2;

public class Graphics {
    private final OpenGLRenderer renderer;
    private final IntVec2 size = new IntVec2();

    public Graphics(final Window window) {
        renderer = new OpenGLRenderer();

        IntVec2 size = window.size();
        setSize(size.x, size.y);
    }

    public void release() {
        renderer.release();
    }

    public OpenGLRenderer getRenderer() {
        return renderer;
    }

    public void setSize(final int w, final int h) {
        size.x = w;
        size.y = h;
        renderer.setLogicalSize(w, h);
        renderer.setViewportSize(w, h);
    }

    public IntVec2 gize() {
        return size;
    }
}
