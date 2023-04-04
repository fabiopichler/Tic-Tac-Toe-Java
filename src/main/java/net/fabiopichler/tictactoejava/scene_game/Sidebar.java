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

import net.fabiopichler.tictactoejava.base.drawables.RectangleShape;
import net.fabiopichler.tictactoejava.base.drawables.Text;
import net.fabiopichler.tictactoejava.base.freetype.FontType;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;

public class Sidebar extends GameComponent {
    private RectangleShape background;
    private RectangleShape verticalLine;
    private RectangleShape horizontalLine1;
    private RectangleShape horizontalLine2;
    private Text player1Text;
    private Text player1WinText;
    private Text player2Text;
    private Text player2WinText;
    private Text tiedText;
    private Text tiedCountText;
    private int width;
    private int player1_y;
    private int player1Win_y;
    private int player2_y;
    private int player2Win_y;
    private int tied_y;
    private int tiedCount_y;

    public Sidebar(final OpenGLRenderer renderer, final SceneGameRect sceneGameRect) {
        super(renderer, sceneGameRect);

        setupSizes();
        createTexts();
    }

    public void release() {
        player1Text.release();
        player1WinText.release();
        player2Text.release();
        player2WinText.release();
        tiedText.release();
        tiedCountText.release();
    }

    public void draw() {
        background.draw();
        verticalLine.draw();
        horizontalLine1.draw();
        horizontalLine2.draw();

        player1Text.draw();
        player1WinText.draw();
        player2Text.draw();
        player2WinText.draw();
        tiedText.draw();
        tiedCountText.draw();
    }

    public void setPlayer1WinText(int count) {
        updateText(player1WinText, player1Win_y, count);
    }

    public void setPlayer2WinText(int count) {
        updateText(player2WinText, player2Win_y, count);
    }

    public void setTiedCountText(int count) {
        updateText(tiedCountText, tiedCount_y, count);
    }

    private void setupSizes() {
        final int border_w = 2;
        final int title_margin = 30;
        final int number_margin = 75;
        final int second_block = sceneGameRect.sidebar_h / 3;
        final int third_block = second_block * 2;

        width = sceneGameRect.sidebar_w - border_w;

        player1_y = title_margin;
        player1Win_y = number_margin;
        player2_y = second_block + title_margin;
        player2Win_y = second_block + number_margin;
        tied_y = third_block + title_margin;
        tiedCount_y = third_block + number_margin;

        background = new RectangleShape(renderer, width, sceneGameRect.sidebar_h);
        verticalLine = new RectangleShape(renderer, border_w, sceneGameRect.sidebar_h);
        horizontalLine1 = new RectangleShape(renderer, width, border_w);
        horizontalLine2 = new RectangleShape(renderer, width, border_w);

        background.setColor(150, 40, 170);
        verticalLine.setColor(90, 0, 110);
        horizontalLine1.setColor(90, 0, 110);
        horizontalLine2.setColor(90, 0, 110);

        verticalLine.setPosition(width, 0);
        horizontalLine1.setPosition(0, second_block - border_w);
        horizontalLine2.setPosition(0, third_block - border_w);
    }

    private void createTexts() {
        player1Text = new Text(renderer);
        player1WinText = new Text(renderer);
        player2Text = new Text(renderer);
        player2WinText = new Text(renderer);
        tiedText = new Text(renderer);
        tiedCountText = new Text(renderer);

        player1Text.setText("Vitórias do Jogador 1");
        player1WinText.setText("0");
        player2Text.setText("Vitórias do Jogador 2");
        player2WinText.setText("0");
        tiedText.setText("Total de Empates");
        tiedCountText.setText("0");

        player1Text.setFontSize(16);
        player1WinText.setFontSize(40);
        player2Text.setFontSize(16);
        player2WinText.setFontSize(40);
        tiedText.setFontSize(16);
        tiedCountText.setFontSize(40);

        player1WinText.setFontType(FontType.Bold);
        player2WinText.setFontType(FontType.Bold);
        tiedCountText.setFontType(FontType.Bold);

        player1Text.setColor(255, 255, 255, 0.70f);
        player1WinText.setColor(255, 255, 255, 0.78f);
        player2Text.setColor(255, 255, 255, 0.70f);
        player2WinText.setColor(255, 255, 255, 0.78f);
        tiedText.setColor(255, 255, 255, 0.70f);
        tiedCountText.setColor(255, 255, 255, 0.78f);

        updateTextRect(player1Text, player1_y);
        updateTextRect(player1WinText, player1Win_y);
        updateTextRect(player2Text, player2_y);
        updateTextRect(player2WinText, player2Win_y);
        updateTextRect(tiedText, tied_y);
        updateTextRect(tiedCountText, tiedCount_y);
    }

    private void updateText(final Text text, final int pos_y, final int count) {
        text.setText(String.format("%d", count));
        updateTextRect(text, pos_y);
    }

    private void updateTextRect(final Text text, final int y) {
        text.setPosition((width - (int)text.width()) / 2.0f, y);
    }
}
