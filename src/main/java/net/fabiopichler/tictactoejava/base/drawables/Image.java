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
import net.fabiopichler.tictactoejava.base.ImageBuffer;
import net.fabiopichler.tictactoejava.base.TextureFilter;
import net.fabiopichler.tictactoejava.base.opengl_renderer.ImageData;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.base.opengl_renderer.Texture2D;
import net.fabiopichler.tictactoejava.base.vectors.IntRect;
import net.fabiopichler.tictactoejava.base.vectors.IntVec2;

import java.net.URL;
import java.nio.ByteBuffer;

public class Image extends Layout implements IDrawable {
    private final IntVec2 realSize = new IntVec2();
    private final IntRect srcrect = new IntRect();
    private final Color color = new Color(255, 255, 255);
    private Texture2D texture;
    private double angle;

    public Image(final OpenGLRenderer renderer) {
        super(renderer);
    }

    public void release() {
        renderer.destroyTexture(texture);
    }

    public boolean loadFromFile(final URL location) {
        return loadFromFile(location, TextureFilter.Nearest);
    }

    public boolean loadFromFile(final URL location, final TextureFilter filter) {
        final var imageBuffer = new ImageBuffer();

        if (imageBuffer.loadFromFile(location))
            return createTexture(imageBuffer, filter);

        return false;
    }

    public boolean loadFromMemory(final ByteBuffer buffer) {
        return loadFromMemory(buffer, TextureFilter.Nearest);
    }

    public boolean loadFromMemory(final ByteBuffer buffer, final TextureFilter filter) {
        final var imageBuffer = new ImageBuffer();

        if (imageBuffer.loadFromMemory(buffer))
            return createTexture(imageBuffer, filter);

        return false;
    }

    public void setSourceRect(final int x, final int y, final int w, final int h) {
        srcrect.x = x;
        srcrect.y = y;
        srcrect.w = w;
        srcrect.h = h;
    }

    public void setSourceRect(final IntRect srcrect) {
        this.srcrect.x = srcrect.x;
        this.srcrect.y = srcrect.y;
        this.srcrect.w = srcrect.w;
        this.srcrect.h = srcrect.h;
    }

    public void setAngle(final double angle) {
        this.angle = angle;
    }

    @Override
    public void draw() {
        if (texture != null)
            renderer.draw(texture, srcrect, rect(), angle, color);
    }

    public IntVec2 realSize() {
        return realSize;
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

    public void setAlpha(float a) {
        color.alpha(a);
    }

    public Color color() {
        return color;
    }

    private boolean createTexture(ImageBuffer imageBuffer, TextureFilter filter) {
        final ImageData image = new ImageData();
        image.width = imageBuffer.width();
        image.height = imageBuffer.height();
        image.bytesPerPixel = imageBuffer.bytesPerPixel();
        image.pitch = image.width * imageBuffer.bytesPerPixel();
        image.rmask = 0x000000ff;
        image.buffer = imageBuffer.buffer();

        renderer.destroyTexture(texture);
        texture = renderer.createTexture(image, filter);

        if (texture != null) {
            realSize.x = image.width;
            realSize.y = image.height;
            srcrect.w = image.width;
            srcrect.h = image.height;
            setSize(image.width, image.height);
        } else {
            throw new RuntimeException("Unable to create texture");
        }

        imageBuffer.release();

        return texture != null;
    }
}
