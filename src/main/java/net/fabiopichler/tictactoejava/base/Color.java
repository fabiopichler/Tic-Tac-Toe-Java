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

public class Color {
    public static class Vec4 {
        private float r, g, b, a;

        public Vec4() {
            this.r = 0.f;
            this.g = 0.f;
            this.b = 0.f;
            this.a = 1.f;
        }

        public Vec4(float r, float g, float b) {
            this.r = Math.max(0.f, Math.min(255.f, r));
            this.g = Math.max(0.f, Math.min(255.f, g));
            this.b = Math.max(0.f, Math.min(255.f, b));
            this.a = 1.f;
        }

        public Vec4(float r, float g, float b, float a) {
            this.r = Math.max(0.f, Math.min(255.f, r));
            this.g = Math.max(0.f, Math.min(255.f, g));
            this.b = Math.max(0.f, Math.min(255.f, b));
            this.a = Math.max(0.f, Math.min(1.f, a));
        }

        public Vec4(Vec4 other) {
            this.r = other.r;
            this.g = other.g;
            this.b = other.b;
            this.a = other.a;
        }

        public void set(float r, float g, float b) {
            set(r, g, b, 1.f);
        }

        public void set(float r, float g, float b, float a) {
            this.r = Math.max(0.f, Math.min(255.f, r));
            this.g = Math.max(0.f, Math.min(255.f, g));
            this.b = Math.max(0.f, Math.min(255.f, b));
            this.a = Math.max(0.f, Math.min(1.f, a));
        }

        public void set(Vec4 other) {
            this.r = other.r;
            this.g = other.g;
            this.b = other.b;
            this.a = other.a;
        }

        public void setAlpha(float a) {
            this.a = Math.max(0.f, Math.min(1.f, a));
        }

        public float r() {
            return r;
        }

        public float g() {
            return g;
        }

        public float b() {
            return b;
        }

        public float a() {
            return a;
        }
    }

    public static class Data {
        public final Vec4 topLeft;
        public final Vec4 topRight;
        public final Vec4 bottomLeft;
        public final Vec4 bottomRight;

        public Data(Vec4 topLeft, Vec4 topRight, Vec4 bottomLeft, Vec4 bottomRight) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }

        public void set(Vec4 topLeft, Vec4 topRight, Vec4 bottomLeft, Vec4 bottomRight) {
            this.topLeft.set(topLeft);
            this.topRight.set(topRight);
            this.bottomLeft.set(bottomLeft);
            this.bottomRight.set(bottomRight);
        }
    }

    private final Data colors;

    public Color() {
        colors = new Data(new Vec4(), new Vec4(), new Vec4(), new Vec4());
    }

    public Color(float r, float g, float b) {
        colors = new Data(
                new Vec4(r, g, b),
                new Vec4(r, g, b),
                new Vec4(r, g, b),
                new Vec4(r, g, b)
        );
    }

    public Color(float r, float g, float b, float a) {
        colors = new Data(
                new Vec4(r, g, b, a),
                new Vec4(r, g, b, a),
                new Vec4(r, g, b, a),
                new Vec4(r, g, b, a)
        );
    }

    public Color.Data colors() {
        return colors;
    }

    public Color set(final Color color) {
        colors.topLeft.set(color.colors.topLeft);
        colors.topRight.set(color.colors.topRight);
        colors.bottomLeft.set(color.colors.bottomLeft);
        colors.bottomRight.set(color.colors.bottomRight);

        return this;
    }

    public Color solid(float r, float g, float b) {
        colors.topLeft.set(r, g, b);
        colors.topRight.set(r, g, b);
        colors.bottomLeft.set(r, g, b);
        colors.bottomRight.set(r, g, b);

        return this;
    }

    public Color solid(float r, float g, float b, float a) {
        colors.topLeft.set(r, g, b, a);
        colors.topRight.set(r, g, b, a);
        colors.bottomLeft.set(r, g, b, a);
        colors.bottomRight.set(r, g, b, a);

        return this;
    }

    public Color alpha(float a) {
        colors.topLeft.setAlpha(a);
        colors.topRight.setAlpha(a);
        colors.bottomLeft.setAlpha(a);
        colors.bottomRight.setAlpha(a);

        return this;
    }

    public Color top(float r, float g, float b) {
        colors.topLeft.set(r, g, b);
        colors.topRight.set(r, g, b);

        return this;
    }

    public Color top(float r, float g, float b, float a) {
        colors.topLeft.set(r, g, b, a);
        colors.topRight.set(r, g, b, a);

        return this;
    }

    public Color topAlpha(float a) {
        colors.topLeft.setAlpha(a);
        colors.topRight.setAlpha(a);

        return this;
    }

    public Color right(float r, float g, float b) {
        colors.topRight.set(r, g, b);
        colors.bottomRight.set(r, g, b);

        return this;
    }

    public Color right(float r, float g, float b, float a) {
        colors.topRight.set(r, g, b, a);
        colors.bottomRight.set(r, g, b, a);

        return this;
    }

    public Color rightAlpha(float a) {
        colors.topRight.setAlpha(a);
        colors.bottomRight.setAlpha(a);

        return this;
    }

    public Color bottom(float r, float g, float b) {
        colors.bottomLeft.set(r, g, b);
        colors.bottomRight.set(r, g, b);

        return this;
    }

    public Color bottom(float r, float g, float b, float a) {
        colors.bottomLeft.set(r, g, b, a);
        colors.bottomRight.set(r, g, b, a);

        return this;
    }

    public Color bottomAlpha(float a) {
        colors.bottomLeft.setAlpha(a);
        colors.bottomRight.setAlpha(a);

        return this;
    }

    public Color left(float r, float g, float b) {
        colors.topLeft.set(r, g, b);
        colors.bottomLeft.set(r, g, b);

        return this;
    }

    public Color left(float r, float g, float b, float a) {
        colors.topLeft.set(r, g, b, a);
        colors.bottomLeft.set(r, g, b, a);

        return this;
    }

    public Color leftAlpha(float a) {
        colors.topLeft.setAlpha(a);
        colors.bottomLeft.setAlpha(a);

        return this;
    }

    public Color topLeft(float r, float g, float b) {
        colors.topLeft.set(r, g, b);
        return this;
    }

    public Color topLeft(float r, float g, float b, float a) {
        colors.topLeft.set(r, g, b, a);
        return this;
    }

    public Color topLeftAlpha(float a) {
        colors.topLeft.setAlpha(a);
        return this;
    }

    public Color topRight(float r, float g, float b) {
        colors.topRight.set(r, g, b);
        return this;
    }

    public Color topRight(float r, float g, float b, float a) {
        colors.topRight.set(r, g, b, a);
        return this;
    }

    public Color topRightAlpha(float a) {
        colors.topRight.setAlpha(a);
        return this;
    }

    public Color bottomLeft(float r, float g, float b) {
        colors.bottomLeft.set(r, g, b);
        return this;
    }

    public Color bottomLeft(float r, float g, float b, float a) {
        colors.bottomLeft.set(r, g, b, a);
        return this;
    }

    public Color bottomLeftAlpha(float a) {
        colors.bottomLeft.setAlpha(a);
        return this;
    }

    public Color bottomRight(float r, float g, float b) {
        colors.bottomRight.set(r, g, b);
        return this;
    }

    public Color bottomRight(float r, float g, float b, float a) {
        colors.bottomRight.set(r, g, b, a);
        return this;
    }

    public Color bottomRightAlpha(float a) {
        colors.bottomRight.setAlpha(a);
        return this;
    }

    public static boolean equals(Color a, Color b) {
        return a.equals(b);
    }

    public boolean equals(final Color other) {
        boolean topLeft = (
                colors.topLeft.r == other.colors.topLeft.r
                        && colors.topLeft.g == other.colors.topLeft.g
                        && colors.topLeft.b == other.colors.topLeft.b
                        && colors.topLeft.a == other.colors.topLeft.a
        );

        boolean topRight = (
                colors.topRight.r == other.colors.topRight.r
                        && colors.topRight.g == other.colors.topRight.g
                        && colors.topRight.b == other.colors.topRight.b
                        && colors.topRight.a == other.colors.topRight.a
        );

        boolean bottomLeft = (
                colors.bottomLeft.r == other.colors.bottomLeft.r
                        && colors.bottomLeft.g == other.colors.bottomLeft.g
                        && colors.bottomLeft.b == other.colors.bottomLeft.b
                        && colors.bottomLeft.a == other.colors.bottomLeft.a
        );

        boolean bottomRight = (
                colors.bottomRight.r == other.colors.bottomRight.r
                        && colors.bottomRight.g == other.colors.bottomRight.g
                        && colors.bottomRight.b == other.colors.bottomRight.b
                        && colors.bottomRight.a == other.colors.bottomRight.a
        );

        return (topLeft && topRight && bottomLeft && bottomRight);
    }
}
