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

package net.fabiopichler.tictactoejava.base.freetype;

public class FontChar {
    public static class Metrics {
        private final int horiAdvance;

        public Metrics(final int horiAdvance) {
            this.horiAdvance = horiAdvance;
        }

        public int horiAdvance() {
            return horiAdvance;
        }
    }

    private final char charCode;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int bitmap_left;
    private final int bitmap_top;
    private final int advance_x;
    private final Metrics metrics;

    public FontChar(
            final char charCode,
            final int x,
            final int y,
            final int width,
            final int height,
            final int bitmap_left,
            final int bitmap_top,
            final int advance_x,
            final Metrics metrics
    ) {
        this.charCode = charCode;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bitmap_left = bitmap_left;
        this.bitmap_top = bitmap_top;
        this.advance_x = advance_x;
        this.metrics = metrics;
    }

    public char charCode() {
        return charCode;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int bitmap_left() {
        return bitmap_left;
    }

    public int bitmap_top() {
        return bitmap_top;
    }

    public int advance_x() {
        return advance_x;
    }

    public Metrics metrics() {
        return metrics;
    }
}
