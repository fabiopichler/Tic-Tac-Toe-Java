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

public class Event {
    public enum Type {
        Unknown,
        Key,
        CursorPos,
        CursorEnter,
        MouseButton,
    }

    public static class Key {
        public int key = 0;
        public int scancode = 0;
        public int action = 0;
        public int mods = 0;
    }

    public static class CursorPos {
        public double x = 0;
        public double y = 0;
    }

    public static class CursorEnter {
        public boolean entered = false;
    }

    public static class MouseButton {
        public int button = 0;
        public int action = 0;
        public int mods = 0;
    }

    public final Event.Key key = new Event.Key();
    public final Event.CursorPos cursorPos = new Event.CursorPos();
    public final Event.CursorEnter cursorEnter = new Event.CursorEnter();
    public final Event.MouseButton mouseButton = new Event.MouseButton();

    public Event.Type type = Event.Type.Unknown;
    public Window window;
}
