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

public class SceneManager {
    private final Window window;
    private final Graphics graphics;
    private final OpenGLRenderer renderer;
    private final EventManager eventManager;
    private Scene scene;
    private Scene newScene;
    private long lastTime;

    public SceneManager(final Window window, final Graphics graphics) {
        this.window = window;
        this.graphics = graphics;
        this.renderer = graphics.getRenderer();

        eventManager = new EventManager(window, this::onEvent);
        lastTime = System.nanoTime();
    }

    public void release() {
        if (scene != null)
            scene.onDestroy();
    }

    public void goTo(final Scene scene) {
        newScene = scene;
    }

    public void initScene() {
        if (newScene == null)
            return;

        if (scene != null)
            scene.onDestroy();

        scene = newScene;
        newScene = null;

        scene.setSceneManager(this);
        scene.setWindow(window);
        scene.setGraphics(graphics);
        scene.setRenderer(renderer);
        scene.onCreate();
    }

    private void onEvent(final Event event) {
        if (scene != null)
            scene.onProcessEvent(event);
    }

    public void run() {
        while (!window.windowShouldClose()) {
            initScene();

            eventManager.pollEvents();

            update();
            draw();
        }
    }

    public void update() {
        final long now = System.nanoTime();
        final double deltaTime = (double)(now - lastTime) / 1000000000.0d;
        lastTime = now;

        if (scene != null)
            scene.onUpdate(deltaTime);
    }

    public void draw() {
        renderer.clear();

        if (scene != null)
            scene.onDraw();

        window.swapBuffers();
    }

    public Window window() {
        return window;
    }

    public Graphics graphics() {
        return graphics;
    }
}
