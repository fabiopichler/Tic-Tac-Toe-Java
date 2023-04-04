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

import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private final String family;
    private final FontType type;
    private final String fileName;
    private final int filter;
    private final Map<Integer, Font> fonts = new HashMap<>();

    public Fonts(String family, FontType type, String fileName, int filter) {
        this.family = family;
        this.type = type;
        this.fileName = fileName;
        this.filter = filter;
    }

    public String name() {
        return makeName(family, type);
    }

    public String family() {
        return family;
    }

    public FontType type() {
        return type;
    }

    public String fileName() {
        return fileName;
    }

    public int filter() {
        return filter;
    }

    public Map<Integer, Font> fonts() {
        return fonts;
    }

    public static String makeName(final String family, final FontType type) {
        return family + " " + type.name();
    }
}
