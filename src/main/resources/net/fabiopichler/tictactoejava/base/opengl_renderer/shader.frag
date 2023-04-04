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

#ifdef GL_ES
    precision highp float;
#endif

uniform sampler2D uSampler;

varying vec2 vUV;
varying vec4 vColor;

void main()
{
    vec4 color = vec4(vColor.rgb / 255.0, vColor.a);

#if hasFont
    gl_FragColor = color * vec4(1.0, 1.0, 1.0, texture2D(uSampler, vUV).r);
#elif hasTexture
    gl_FragColor = color * texture2D(uSampler, vUV);
#else
    gl_FragColor = color;
#endif
}
