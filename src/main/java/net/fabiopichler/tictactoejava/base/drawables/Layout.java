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

package net.fabiopichler.tictactoejava.base.drawables;

import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.base.vectors.Rect;

public abstract class Layout extends Drawable {
    private final Rect rect;

    protected Layout(final OpenGLRenderer renderer) {
        super(renderer);

        rect = new Rect(0, 0, 0, 0);
    }

    protected Layout(final OpenGLRenderer renderer, final float width, final float height) {
        super(renderer);

        rect = new Rect(0, 0, width, height);
    }

    protected void onLayoutUpdate() {}

    public void setPosition(final float x, final float y) {
        rect.x = x;
        rect.y = y;

        onLayoutUpdate();
    }

    public void setSize(final float w, final float h) {
        rect.w = w;
        rect.h = h;

        onLayoutUpdate();
    }

    public void setX(final float x) {
        rect.x = x;

        onLayoutUpdate();
    }

    public void setY(final float y) {
        rect.y = y;

        onLayoutUpdate();
    }

    public void setWidth(final float w) {
        rect.w = w;

        onLayoutUpdate();
    }

    public void setHeight(final float h) {
        rect.h = h;

        onLayoutUpdate();
    }

    public void move(final float velX, final float velY) {
        rect.x += velX;
        rect.y += velY;

        onLayoutUpdate();
    }

    public float x() {
        return rect.x;
    }

    public float y() {
        return rect.y;
    }

    public float width() {
        return rect.w;
    }

    public float height() {
        return rect.h;
    }

    public Rect rect() {
        return rect;
    }
}
