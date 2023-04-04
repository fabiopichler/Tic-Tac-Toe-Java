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

import org.lwjgl.BufferUtils;
import org.lwjgl.util.freetype.*;

import java.nio.ByteBuffer;
import java.util.*;

import static org.lwjgl.util.freetype.FreeType.*;

public class FontManager {
    private static FontManager self;
    private final long library;
    private final Map<String, Fonts> fonts = new HashMap<>();
    private Fonts defaultFont;
    private OnCreateTextureEventHandler createTexture;
    private OnDeleteTextureEventHandler deleteTexture;
    private String charCodeList;

    public static void init() {
        if (self == null)
            self = new FontManager();
    }

    public static void close() {
        if (self != null)
            self.release();

        self = null;
    }

    public static FontManager instance() {
        return self;
    }

    private FontManager() {
        library = FreeType.initFreeType();
        setCharCodeList("");
    }

    private void release() {
        FT_Done_FreeType(library);
    }

    public void deleteTextures() {
        if (deleteTexture != null) {
            for (final var entry : fonts.entrySet())
                for (final var e : entry.getValue().fonts().entrySet())
                    deleteTexture.call(e.getValue().texture());

            fonts.clear();

        } else {
            System.err.println("deleteTexture not defined");
        }
    }

    public void setEvents(OnCreateTextureEventHandler createTexture, OnDeleteTextureEventHandler deleteTexture) {
        this.createTexture = createTexture;
        this.deleteTexture = deleteTexture;
    }

    public void removeEvents() {
        createTexture = null;
        deleteTexture = null;
    }

    public Set<String> getFontList() {
        return fonts.keySet();
    }

    public void addFont(final String family, final FontType type, final String fileName, final int filter) {
        final String name = Fonts.makeName(family, type);

        if (fonts.containsKey(name)) {
            System.err.println("The font already exists: " + name);
            return;
        }

        final var font = new Fonts(family, type, fileName, filter);
        fonts.put(name, font);

        if (defaultFont == null)
            defaultFont = font;
    }

    public void setDefaultFont(final String family, final FontType type) {
        final String name = Fonts.makeName(family, type);

        if (!fonts.containsKey(name))
            throw new RuntimeException("Font not found: " + name);

        defaultFont = fonts.get(name);
    }

    public Font getFont(final String family, final FontType type, final int fontSize) {
        if (family == null && defaultFont == null)
            throw new RuntimeException("Default font not defined");

        final String name = Fonts.makeName(family != null ? family : defaultFont.family(), type);

        if (!fonts.containsKey(name))
            throw new RuntimeException("Font not found: " + name);

        final Fonts font = fonts.get(name);

        if (font.fonts().containsKey(fontSize))
            return font.fonts().get(fontSize);

        final Font newFont = newFontFace(font, fontSize);
        font.fonts().put(fontSize, newFont);

        return newFont;
    }

    public Font newFontFace(final Fonts fonts, final int fontSize) {
        if (createTexture == null)
            throw new RuntimeException("FontManager.addFont: createTexture event not defined");

        final FT_Face face = FreeType.createFace(library, fonts.fileName());

        FT_Select_Charmap(face, FT_ENCODING_UNICODE);
        FT_Set_Pixel_Sizes(face, 0, fontSize);

        final Map<Character, FontChar> fontCharacters = new HashMap<>();
        final int ascender = (int)(face.size().metrics().ascender() >> 6);
        final int descender = (int)(face.size().metrics().descender() >> 6);
        final int lineHeight = ascender - descender;
        final int bitmapLineHeight = Math.abs(ascender);
        final int bitmapWidth = (int)Math.round((double)fontSize * Math.sqrt(charCodeList.length()));
        int bitmapHeight = bitmapLineHeight;
        ByteBuffer bitmapBuffer = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
        int charX = 0;
        int charY = 0;

        for (final char charCode : charCodeList.toCharArray()) {
            if (FT_Load_Char(face, charCode, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT /*| FT_LOAD_TARGET_LIGHT*/) != 0)
                continue;

            final FT_GlyphSlot glyph = face.glyph();

            if (glyph == null)
                continue;

            final FT_Bitmap bitmap = glyph.bitmap();
            final ByteBuffer bitmapGlyphBuffer = bitmap.buffer(bitmap.width() * bitmap.rows());

            if (charX + bitmap.width() >= bitmapWidth) {
                charX = 0;
                charY += bitmapLineHeight;
                bitmapHeight += bitmapLineHeight;
                bitmapBuffer = resizeBuffer(bitmapBuffer, bitmapWidth * bitmapHeight);
            }

            for (int row = 0; row < bitmap.rows(); ++row) {
                for (int col = 0; col < bitmap.width(); ++col) {
                    final int x = charX + col;
                    final int y = charY + row;

                    if (bitmapGlyphBuffer != null) {
                        final byte pixel = bitmapGlyphBuffer.get(row * bitmap.pitch() + col);
                        bitmapBuffer.put(y * bitmapWidth + x, pixel);
                    }
                }
            }

            fontCharacters.put(charCode, new FontChar(
                    charCode,
                    charX,
                    charY,
                    bitmap.width(),
                    bitmap.rows(),
                    glyph.bitmap_left(),
                    glyph.bitmap_top(),
                    (int)(glyph.advance().x() >> 6),
                    new FontChar.Metrics(
                            (int)(glyph.metrics().horiAdvance() >> 6)
                    )
            ));

            charX += bitmap.width() + 3;
        }

        FT_Done_Face(face);
        bitmapBuffer.flip();

        //FreeTypeUtils.savePng(bitmapBuffer, bitmapWidth, bitmapHeight, fonts.name() + " " + fontSize);

        return new Font(
                createTexture.call(bitmapBuffer, bitmapWidth, bitmapHeight, fonts.filter()),
                fonts.name(),
                fonts.type(),
                fontSize,
                lineHeight,
                ascender,
                fontCharacters
        );
    }

    public void setCharCodeList(final String charCodeList) {
        this.charCodeList = charCodeList;

        if (this.charCodeList.indexOf(Font.SPACE_CHAR) == -1)
            this.charCodeList += Font.SPACE_CHAR;

        if (this.charCodeList.indexOf(Font.UNKNOWN_CHAR) == -1)
            this.charCodeList += Font.UNKNOWN_CHAR;
    }

    private static ByteBuffer resizeBuffer(final ByteBuffer inputBuffer, final int capacity) {
        final ByteBuffer tempBuffer = BufferUtils.createByteBuffer(capacity);

        for (int i = 0; i < inputBuffer.capacity(); ++i)
            tempBuffer.put(inputBuffer.get(i));

        return tempBuffer;
    }
}
