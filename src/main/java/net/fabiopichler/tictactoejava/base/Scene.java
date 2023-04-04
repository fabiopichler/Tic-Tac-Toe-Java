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

import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;

public abstract class Scene implements IScene {
    protected SceneManager sceneManager;
    protected Window window;
    protected Graphics graphics;
    protected OpenGLRenderer renderer;

    @Override
    public abstract void onCreate();

    @Override
    public abstract void onDestroy();

    public void onProcessEvent(final Event event) {}

    public void onUpdate(final double deltaTime) {}

    public void onDraw() {}

    public void setSceneManager(final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setWindow(final Window window) {
        this.window = window;
    }

    public void setGraphics(final Graphics graphics) {
        this.graphics = graphics;
    }

    public void setRenderer(final OpenGLRenderer renderer) {
        this.renderer = renderer;
    }
}
