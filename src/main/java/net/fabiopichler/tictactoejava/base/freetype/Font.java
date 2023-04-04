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

import java.util.*;

public class Font {
    private static class TextLine {
        public float width = 0;
        public final List<FontChar> fontChars = new ArrayList<>();
    }

    public static final char SPACE_CHAR = ' ';
    public static final char UNKNOWN_CHAR = '�';
    public static final char NEWLINE_CHAR = '\n';

    private final Object texture;
    private final String name;
    private final FontType type;
    private final int size;
    private final int lineHeight;
    private final int ascender;
    private final Map<Character, FontChar> characters;

    public Font(
            final Object texture,
            final String name,
            final FontType type,
            final int size,
            final int lineHeight,
            final int ascender,
            final Map<Character, FontChar> characters
    ) {
        this.texture = texture;
        this.name = name;
        this.type = type;
        this.size = size;
        this.lineHeight = lineHeight;
        this.ascender = ascender;
        this.characters = characters;
    }

    public Object texture() {
        return texture;
    }

    public String name() {
        return name;
    }

    public FontType type() {
        return type;
    }

    public int size() {
        return size;
    }

    public int lineHeight() {
        return lineHeight;
    }

    public Map<Character, FontChar> characters() {
        return characters;
    }

    public FontChar getCharacter(final char charCode) {
        return characters.get(characters.containsKey(charCode) ? charCode : UNKNOWN_CHAR);
    }

    public boolean equals(final Font other) {
        return other != null && name.equals(other.name) && type == other.type && size == other.size;
    }

    public DrawableText createDrawableText(final String input, final int wrapLength, final TextAlign textAlign) {
        int textWidth = 0;
        int currentWidth = 0;
        final List<TextLine> textLines = new ArrayList<>();

        textLines.add(new TextLine());

        for (int charIndex = 0; charIndex < input.length(); ++charIndex) {
            final char charCode = input.charAt(charIndex);
            final boolean isNewLine = charCode == NEWLINE_CHAR;

            if (isNewLine || (wrapLength > 0 && next(wrapLength, currentWidth, input, charIndex))) {
                final TextLine lastLine = getLastLine(textLines);
                lastLine.width = currentWidth;
                currentWidth = 0;

                textLines.add(new TextLine());

                if (wrapLength > 0 && !isNewLine && lastLine.fontChars.size() > 0) {
                    int count = 0;

                    for (int j = lastLine.fontChars.size() - 1; j >= 0; --j) {
                        var ch = lastLine.fontChars.get(j);

                        ++count;

                        if (ch.charCode() == SPACE_CHAR)
                            break;
                    }

                    if (count < lastLine.fontChars.size()) {
                        final int index = lastLine.fontChars.size() - count;

                        for (int j = index; j < lastLine.fontChars.size(); ++j) {
                            final var ch = lastLine.fontChars.get(j);

                            if (ch.charCode() != SPACE_CHAR) {
                                getLastLine(textLines).fontChars.add(ch);
                                currentWidth += ch.metrics().horiAdvance();
                            }

                            lastLine.width -= ch.metrics().horiAdvance();
                        }

                        if (lastLine.fontChars.size() > index + 1)
                            lastLine.fontChars.subList(index + 1, lastLine.fontChars.size()).clear();
                    }
                }
            }

            if (!isNewLine) {
                final FontChar fontChar = getCharacter(charCode);
                getLastLine(textLines).fontChars.add(fontChar);
                currentWidth += fontChar.metrics().horiAdvance();
            }

            if (textWidth < currentWidth)
                textWidth = currentWidth;
        }

        getLastLine(textLines).width = currentWidth;
        final float textHeight = textLines.size() * lineHeight;
        final var drawableText = new DrawableText(this, textWidth, textHeight);

        addCharactersToText(textLines, textAlign, textWidth, drawableText);

        return drawableText;
    }

    private boolean next(final int wrapLength, final float currentWidth, final String inputText, final int charIndex) {
        final FontChar ch = getCharacter(inputText.charAt(charIndex));
        final FontChar nextCh = getCharacter(inputText.charAt(charIndex + 1));

        return (currentWidth > wrapLength && ch.charCode() != SPACE_CHAR)
                || (((currentWidth + nextCh.metrics().horiAdvance()) > wrapLength) && nextCh.charCode() != SPACE_CHAR);
    }

    private void addCharactersToText(final List<TextLine> textLines, final TextAlign textAlign,
                                     final int textWidth, final DrawableText drawableText) {
        float pen_x = 0;
        float pen_y = ascender;

        for (final TextLine line : textLines) {
            if (textAlign == TextAlign.Center)
                pen_x += (textWidth - line.width) / 2;
            else if (textAlign == TextAlign.Right)
                pen_x += textWidth - line.width;

            for (final FontChar fontChar : line.fontChars) {
                drawableText.characters().add(new DrawableChar(
                        fontChar,
                        pen_x + fontChar.bitmap_left(),
                        pen_y - fontChar.bitmap_top(),
                        fontChar.width(),
                        fontChar.height()
                ));

                pen_x += fontChar.advance_x();
            }

            pen_x = 0;
            pen_y += lineHeight;
        }
    }

    private static TextLine getLastLine(List<TextLine> textLines) {
        return textLines.get(textLines.size() - 1);
    }
}
