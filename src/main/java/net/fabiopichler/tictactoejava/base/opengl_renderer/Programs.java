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

package net.fabiopichler.tictactoejava.base.opengl_renderer;

import net.fabiopichler.tictactoejava.base.utils.FileUtils;

import org.lwjgl.*;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.opengl.GL33C.*;

public class Programs {
    private final Locations[] programs = new Locations[ProgramType.size];
    private int lastProgram;

    public void release() {
        for (int i = 0; i < ProgramType.size; ++i)
            glDeleteProgram(programs[i].program);
    }

    public Locations initProgram(final ProgramType type) throws IOException, AssertionError {
        final int program = createProgram(type);

        final var locations = new Locations();
        locations.program = program;
        locations.aPosition = glGetAttribLocation(program, "aPosition");
        locations.aUV = -1;
        locations.aColor = glGetAttribLocation(program, "aColor");
        locations.uProjection = glGetUniformLocation(program, "uProjection");
        locations.uSampler = -1;
        locations.uSourcePosition = -1;
        locations.uTransform = glGetUniformLocation(program, "uTransform");

        if (type == ProgramType.Texture || type == ProgramType.Font) {
            locations.aUV = glGetAttribLocation(program, "aUV");
            locations.uSourcePosition = glGetUniformLocation(program, "uSourcePosition");
            locations.uSampler = glGetUniformLocation(program, "uSampler");
        }

        return programs[type.ordinal()] = locations;
    }

    public Locations getProgram(final ProgramType type) {
        final Locations program = programs[type.ordinal()];

        if (program.program != lastProgram) {
            glUseProgram(program.program);
            lastProgram = program.program;
        }

        return program;
    }

    private int createProgram(final ProgramType type) throws IOException, AssertionError {
        final int program = glCreateProgram();

        final String vert = getVertexShaderSource(type);
        final String frag = getFragmentShaderSource(type);

        compileShader(program, GL_VERTEX_SHADER, vert);
        compileShader(program, GL_FRAGMENT_SHADER, frag);

        glLinkProgram(program);
        checkProgram(program);
        glUseProgram(program);

        return program;
    }

    private void compileShader(final int program, final int type, final String src) throws AssertionError {
        final int shader = glCreateShader(type);

        final ByteBuffer source = BufferUtils.createByteBuffer(src.length() + 1);
        source.put(src.getBytes());
        source.flip();

        final PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        final IntBuffer lengths = BufferUtils.createIntBuffer(1);

        strings.put(0, source);
        lengths.put(0, source.remaining());

        glShaderSource(shader, strings, lengths);
        glCompileShader(shader);

        checkShader(shader);

        glAttachShader(program, shader);
        glDeleteShader(shader);
    }

    private void checkProgram(final int program) throws AssertionError {
        final int linked = glGetProgrami(program, GL_LINK_STATUS);
        final String programLog = glGetProgramInfoLog(program);

        if (programLog.trim().length() > 0)
            System.err.println(programLog);

        if (linked == 0)
            throw new AssertionError("Could not link program");
    }

    private void checkShader(final int shader) throws AssertionError {
        final int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);
        final String shaderLog = glGetShaderInfoLog(shader);

        if (shaderLog.trim().length() > 0)
            System.err.println(shaderLog);

        if (compiled == 0)
            throw new AssertionError("Could not compile shader");
    }

    private String getShaderSource(final ProgramType type, final String source) {
        String src = "#version 100\n"; // OpenGL ES 2.0 / WebGL 1.0

        if (type == ProgramType.Texture || type == ProgramType.Font)
            src += "#define hasTexture 1\n";
        else
            src += "#define hasTexture 0\n";

        if (type == ProgramType.Font)
            src += "#define hasFont 1\n";
        else
            src += "#define hasFont 0\n";

        return src + source;
    }

    private String getVertexShaderSource(final ProgramType type) throws IOException {
        final String vert = FileUtils.readToString(getClass().getResource("shader.vert"));

        return getShaderSource(type, vert);
    }

    private String getFragmentShaderSource(final ProgramType type) throws IOException {
        final String frag = FileUtils.readToString(getClass().getResource("shader.frag"));

        return getShaderSource(type, frag);
    }
}
