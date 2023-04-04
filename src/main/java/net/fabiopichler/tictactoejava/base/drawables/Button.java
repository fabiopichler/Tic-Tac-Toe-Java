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

import net.fabiopichler.tictactoejava.base.*;
import net.fabiopichler.tictactoejava.base.freetype.FontType;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.base.vectors.Rect;

import static org.lwjgl.glfw.GLFW.*;

public class Button extends Layout implements IDrawable {
    private enum State {
        Normal,
        Pressed,
        Hover
    }

    public interface OnPressEventHandler {
        void call(final Button button);
    }

    private final Color color = new Color(50, 140, 140);
    private final Color colorHover = new Color(30, 120, 120);
    private final Color colorPressed = new Color(60, 60, 60);
    private final RectangleShape background;
    private Text text;
    private Image icon;
    private OnPressEventHandler onPressEventHandler;
    private State state = State.Normal;

    public Button(final OpenGLRenderer renderer) {
        super(renderer, 60.f, 40.f);

        background = new RectangleShape(renderer, width(), height());
    }

    public Button(final OpenGLRenderer renderer, final Text text) {
        super(renderer, 60.f, 40.f);

        background = new RectangleShape(renderer, width(), height());

        setText(text);
    }

    public Button(final OpenGLRenderer renderer, final Image image) {
        super(renderer, 60.f, 40.f);

        background = new RectangleShape(renderer, width(), height());
        icon = image;
    }

    public void release() {
        if (text != null)
            text.release();

        if (icon != null)
            icon.release();
    }

    public void setBackgroundColor(float r, float g, float b) {
        color.solid(r, g, b);
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        color.solid(r, g, b, a);
    }

    public void setBackgroundColor(Color color) {
        this.color.set(color);
    }

    public void setBackgroundHoverColor(float r, float g, float b) {
        colorHover.solid(r, g, b);
    }

    public void setBackgroundHoverColor(float r, float g, float b, float a) {
        colorHover.solid(r, g, b, a);
    }

    public void setBackgroundHoverColor(Color color) {
        colorHover.set(color);
    }

    public void setBackgroundPressedColor(float r, float g, float b) {
        colorPressed.solid(r, g, b);
    }

    public void setBackgroundPressedColor(float r, float g, float b, float a) {
        colorPressed.solid(r, g, b, a);
    }

    public void setBackgroundPressedColor(Color color) {
        colorPressed.set(color);
    }

    public RectangleShape background() {
        return background;
    }

    public void setText(final Text text) {
        if (this.text == null) {
            text.setColor(255, 255, 255);
            text.setFontType(FontType.Bold);
            text.setFontSize(16);
        }

        this.text = text;
        update();
    }

    public Text text() {
        return text;
    }

    public void setIcon(final Image icon) {
        this.icon = icon;
        update();
    }

    public Image icon() {
        return icon;
    }

    public void setOnPressEvent(final OnPressEventHandler handler) {
        onPressEventHandler = handler;
    }

    private void callPressedEvent() {
        if (onPressEventHandler != null)
            onPressEventHandler.call(this);
    }

    @Override
    public void processEvent(final Event event) {
        if (event.type == Event.Type.CursorPos || event.type == Event.Type.MouseButton) {
            if (event.mouseButton.button == GLFW_MOUSE_BUTTON_LEFT && pointerIsHovering(event)) {
                if (event.mouseButton.action == GLFW_PRESS) {
                    state = State.Pressed;
                    callPressedEvent();
                    return;

                } else if (state == State.Pressed && event.mouseButton.action != GLFW_RELEASE) {
                    return;
                }
            }

            if (pointerIsHovering(event))
                state = State.Hover;
            else
                state = State.Normal;
        }
    }

    @Override
    public void draw() {
        update();

        if (state == State.Hover)
            background.setColor(colorHover);
        else if (state == State.Pressed)
            background.setColor(colorPressed);
        else
            background.setColor(color);

        background.draw();

        if (icon != null)
            icon.draw();

        if (text != null)
            text.draw();
    }

    private boolean pointerIsHovering(final Event event) {
        final Rect rect = rect();

        return event.cursorPos.x >= rect.x
                && event.cursorPos.x <= (rect.x + rect.w)
                && event.cursorPos.y >= rect.y
                && event.cursorPos.y <= (rect.y + rect.h);
    }

    @Override
    public void update() {
        if (text == null && icon == null)
            return;

        final Rect rect = rect();
        final Layout layout = text != null ? text : icon;
        final int w = (int) layout.width();
        final int h = (int) layout.height();

        layout.setSize(w, h);
        layout.setPosition(rect.x + ((rect.w - w) / 2), rect.y + ((rect.h - h) / 2));
    }

    @Override
    protected void onLayoutUpdate() {
        background.setSize(width(), height());
        background.setPosition(x(), y());

        update();
    }
}
