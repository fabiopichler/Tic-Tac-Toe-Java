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
import net.fabiopichler.tictactoejava.base.freetype.*;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.base.vectors.Vec2;

public class Text extends Layout implements IDrawable {
    private final Color color = new Color(60, 60, 60);
    private RectangleShape background;
    private Font font;
    private String fontName;
    private int fontSize = 16;
    private FontType fontType = FontType.Normal;
    private TextAlign textAlign = TextAlign.Left;
    private String text;
    private int wrapLength;
    private DrawableText drawableText;
    private boolean changed = true;
    private int visibleChars = -1;

    public Text(final OpenGLRenderer renderer) {
        super(renderer);

        text = "";
    }

    public Text(final OpenGLRenderer renderer, final String text) {
        super(renderer);

        this.text = text;
    }

    public Text(final OpenGLRenderer renderer, final String text, final int fontSize) {
        super(renderer);

        this.text = text;
        this.fontSize = fontSize;
    }

    public Text(final OpenGLRenderer renderer, final String text, final int fontSize, final FontType fontType) {
        super(renderer);

        this.text = text;
        this.fontSize = fontSize;
        this.fontType = fontType;
    }

    public void release() {
    }

    public void setColor(final Color color) {
        if (color != null)
            this.color.set(color);
        else
            this.color.solid(60, 60, 60);
    }

    public void setColor(float r, float g, float b) {
        color.solid(r, g, b);
    }

    public void setColor(float r, float g, float b, float a) {
        color.solid(r, g, b, a);
    }

    public Color color() {
        return color;
    }

    public void setText(final String text) {
        if (this.text.equals(text))
            return;

        this.text = text;
        setup();
    }

    public String text() {
        return text;
    }

    public void setTextAlign(final TextAlign textAlign) {
        if (this.textAlign == textAlign)
            return;

        this.textAlign = textAlign;
        changed = true;
    }

    public TextAlign textAlign() {
        return textAlign;
    }

    public void setWrapLength(int wrapLength) {
        if (this.wrapLength == wrapLength)
            return;

        this.wrapLength = wrapLength;
        setup();
    }

    public int wrapLength() {
        return wrapLength;
    }

    public void setFont(final Font font) {
        if (font == null || this.font.equals(font))
            return;

        this.font = font;
        fontName = font.name();
        fontType = font.type();
        fontSize = font.size();

        setup();
    }

    public Font font() {
        return font;
    }

    public void setFontName(final String name) {
        if (fontName.equals(name))
            return;

        fontName = name;
        setup();
    }

    public void setFontType(final FontType type) {
        if (fontType == type)
            return;

        fontType = type;
        setup();
    }

    public void setFontSize(final int size) {
        if (fontSize == size || size <= 0)
            return;

        fontSize = size;
        setup();
    }

    public void hideChars() {
        visibleChars = 0;
    }

    public void showChars() {
        visibleChars = -1;
    }

    public void showChars(int number) {
        if (number < -1 || number > drawableText.characters().size())
            return;

        visibleChars = number;
    }

    public void incrementVisibleChar(int number) {
        visibleChars += number;
        visibleChars = Math.max(0, Math.min(drawableText.characters().size(), visibleChars));
    }

    public int visibleChars() {
        return visibleChars;
    }

    public RectangleShape background() {
        if (background == null) {
            background = new RectangleShape(renderer, width(), height());
            background.setPosition(x(), y());
        }

        return background;
    }

    @Override
    public void update() {
        if (changed)
            setup();
    }

    @Override
    public void draw() {
        update();

        if (background != null)
            background.draw();

        renderer.draw(drawableText, x(), y(), color, visibleChars);
    }

    private void setup() {
        changed = false;

        font = FontManager.instance().getFont(fontName, fontType, fontSize);
        drawableText = font.createDrawableText(text, wrapLength, textAlign);
        final Vec2 size = drawableText.size();

        setSize(size.x, size.y);

        if (background != null)
            background.setSize(size.x, size.y);
    }
}
