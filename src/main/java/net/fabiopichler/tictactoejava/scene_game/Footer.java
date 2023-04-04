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

package net.fabiopichler.tictactoejava.scene_game;

import net.fabiopichler.tictactoejava.base.drawables.Button;
import net.fabiopichler.tictactoejava.base.Event;
import net.fabiopichler.tictactoejava.base.drawables.Text;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;

public class Footer extends GameComponent {
    private Button restartButton;
    private Text copyrightText;

    public Footer(final OpenGLRenderer renderer, final SceneGameRect sceneGameRect) {
        super(renderer, sceneGameRect);

        createRestartButton();
        createCopyrightText();
    }

    public void release() {
        restartButton.release();
        copyrightText.release();
    }

    public void processEvent(final Event event) {
        restartButton.processEvent(event);
    }

    public void draw() {
        restartButton.draw();
        copyrightText.draw();
    }

    public Button getRestartButton() {
        return restartButton;
    }

    private void createRestartButton() {
        restartButton = new Button(renderer, new Text(renderer, "Reiniciar"));

        restartButton.setBackgroundColor(160, 40, 180);
        restartButton.setBackgroundHoverColor(200, 50, 220);
        restartButton.setBackgroundPressedColor(140, 40, 160);
        restartButton.text().setColor(255, 255, 255, 0.78f);

        final int width = 110;
        final int height = 32;
        final int padding = 40;

        restartButton.setSize(width, height);
        restartButton.setPosition(
                sceneGameRect.sidebar_w + (int)((sceneGameRect.content_w - width) / 2),
                sceneGameRect.window_h - height - padding);
    }

    private void createCopyrightText() {
        copyrightText = new Text(renderer);

        copyrightText.setText("© 2020-2023 Fábio Pichler                       www.fabiopichler.net");
        copyrightText.setFontSize(14);
        copyrightText.setColor(255, 255, 255, 0.62f);

        final int width = (int)copyrightText.width();
        final int height = (int)copyrightText.height();
        final int padding = 10;

        copyrightText.setPosition(
                sceneGameRect.sidebar_w + (int)((sceneGameRect.content_w - width) / 2),
                sceneGameRect.window_h - height - padding);
    }
}
