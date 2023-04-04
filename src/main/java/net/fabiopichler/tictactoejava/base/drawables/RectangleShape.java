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

import net.fabiopichler.tictactoejava.base.Color;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;

public class RectangleShape extends Layout implements IDrawable {
    private final Color color = new Color(0, 0, 0, 0.f);

    public RectangleShape(final OpenGLRenderer renderer, final float width, final float height) {
        super(renderer, width, height);
    }

    @Override
    public void draw() {
        renderer.drawRect(rect(), color);
    }

    public void setColor(float r, float g, float b) {
        color.solid(r, g, b);
    }

    public void setColor(float r, float g, float b, float a) {
        color.solid(r, g, b, a);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public Color color() {
        return color;
    }
}
