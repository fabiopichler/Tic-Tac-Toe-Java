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

import static org.lwjgl.glfw.GLFW.*;

public class EventManager {
    public interface EventHandler {
        void call(final Event event);
    }

    private final Event event = new Event();
    private final Window window;
    private final EventManager.EventHandler eventHandler;

    public EventManager(final Window window, final EventManager.EventHandler handler) {
        this.window = window;
        this.eventHandler = handler;

        final long nativeWindow = window.nativeWindow();
        
        glfwSetKeyCallback(nativeWindow, this::onKeyCallback);
        glfwSetCursorPosCallback(nativeWindow, this::onCursorPositionCallback);
        glfwSetCursorEnterCallback(nativeWindow, this::onCursorEnterCallback);
        glfwSetMouseButtonCallback(nativeWindow, this::onMouseButtonCallback);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    private void onKeyCallback(final long window, final int key, final int scancode, final int action, final int mods) {
        if (this.window.nativeWindow() != window)
            return;

        setKeyValues(key, scancode, action, mods);

        try {
            callEvent(Event.Type.Key);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            setKeyValues(0, 0, 0, 0);
        }
    }

    private void onCursorPositionCallback(final long window, final double xpos, final double ypos) {
        if (this.window.nativeWindow() != window)
            return;

        setCursorPositionValues(xpos, ypos);

        try {
            callEvent(Event.Type.CursorPos);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            //setCursorPositionValues(0, 0);
        }
    }

    private void onCursorEnterCallback(final long window, final boolean entered) {
        if (this.window.nativeWindow() != window)
            return;

        setCursorEnterValues(entered);

        try {
            callEvent(Event.Type.CursorEnter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            setCursorEnterValues(false);
        }
    }

    private void onMouseButtonCallback(final long window, final int button, final int action, final int mods) {
        if (this.window.nativeWindow() != window)
            return;

        setMouseButtonValues(button, action, mods);

        try {
            callEvent(Event.Type.MouseButton);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            setMouseButtonValues(0, 0, 0);
        }
    }

    private void setKeyValues(final int key, final int scancode, final int action, final int mods) {
        event.key.key = key;
        event.key.scancode = scancode;
        event.key.action = action;
        event.key.mods = mods;
    }

    private void setCursorPositionValues(final double xpos, final double ypos) {
        event.cursorPos.x = xpos;
        event.cursorPos.y = ypos;
    }

    private void setCursorEnterValues(final boolean entered) {
        event.cursorEnter.entered = entered;
    }

    private void setMouseButtonValues(final int button, final int action, final int mods) {
        event.mouseButton.button = button;
        event.mouseButton.action = action;
        event.mouseButton.mods = mods;
    }

    private void callEvent(final Event.Type type) {
        event.type = type;
        event.window = window;

        if (eventHandler != null)
            eventHandler.call(event);
    }
}
