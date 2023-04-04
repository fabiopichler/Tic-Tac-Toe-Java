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

import net.fabiopichler.tictactoejava.base.*;
import net.fabiopichler.tictactoejava.base.drawables.Image;
import net.fabiopichler.tictactoejava.base.drawables.RectangleShape;
import net.fabiopichler.tictactoejava.base.drawables.Text;
import net.fabiopichler.tictactoejava.base.freetype.FontType;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.scene_game.board.Player;

public class Header extends GameComponent {
    private final int space;
    private final int margin;
    private final float line_p1_x;
    private final float line_p2_x;
    private final RectangleShape line;
    private RectangleShape background1;
    private RectangleShape background2;
    private Text result;
    private Text player1;
    private Image player1Icon;
    private Text player2;
    private Image player2Icon;
    private Player currentPlayer = Player.Player_1;
    private Player gameResult = Player.None;

    public Header(final OpenGLRenderer renderer, final SceneGameRect sceneGameRect) {
        super(renderer, sceneGameRect);

        final float w = 134;
        final float x = sceneGameRect.sidebar_w + ((sceneGameRect.content_w - w) / 2);
        line_p1_x = x - 85.f;
        line_p2_x = x + 85.f;
        space = 6;
        margin = 20;

        line = new RectangleShape(renderer, w, 4.f);
        line.setPosition(line_p1_x, 62.f);
        line.setColor(255, 255, 255, 0.43f);

        createBackgrounds();
        createResultText();
        createPlayer1Text();
        createPlayer2Text();
    }

    public void release() {
        result.release();
        player1.release();
        player1Icon.release();
        player2.release();
        player2Icon.release();
    }

    public void processEvent(final Event event) {
    }

    public void update(final double deltaTime) {
        final float x = line.x();

        if (currentPlayer == Player.Player_1 && x >= line_p1_x)
            line.setX((float)Math.max(x - (800.0 * deltaTime), line_p1_x));

        else if (currentPlayer == Player.Player_2 && x <= line_p2_x)
            line.setX((float)Math.min(x + (800.0 * deltaTime), line_p2_x));
    }

    public void draw() {
        if (gameResult == Player.None) {
            line.draw();
            player1.draw();
            player1Icon.draw();
            player2.draw();
            player2Icon.draw();
        } else {
            background1.draw();
            background2.draw();
            result.draw();
        }
    }

    public void setCurrentPlayer(final Player currentPlayer, final Player gameResult) {
        this.currentPlayer = currentPlayer;
        this.gameResult = gameResult;

        if (gameResult == Player.Player_1)
            result.setText("Vitória do jogador 1");

        else if (gameResult == Player.Player_2)
            result.setText("Vitória do jogador 2");

        else if (gameResult == Player.Tied)
            result.setText("Deu empate!");

        if (gameResult != Player.None)
            setupResultText();
    }

    private void createResultText() {
        result = new Text(renderer);
        result.setText("...");
        result.setFontSize(24);
        result.setFontType(FontType.Bold);
        result.setColor(180, 40, 200);

        setupResultText();
    }

    private void createBackgrounds() {
        final int w = 302;
        final int h = 52;
        final int x = sceneGameRect.sidebar_w + ((sceneGameRect.content_w - w) / 2);
        final int y = 18;

        background1 = new RectangleShape(renderer, w, h);
        background1.setPosition(x, y);
        background1.setColor(210, 60, 230);

        background2 = new RectangleShape(renderer, w - 6, h - 6);
        background2.setPosition(x + 3, y + 3);
        background2.setColor(245, 220, 255);
    }

    private void createPlayer1Text() {
        player1 = new Text(renderer);
        player1.setText("Jogador 1");
        player1.setFontSize(20);
        player1.setFontType(FontType.Bold);
        player1.setColor(255, 255, 255, 0.78f);

        player1Icon = new Image(renderer);
        player1Icon.setAlpha(0.78f);
        player1Icon.loadFromFile(getClass().getResource("board/player_1.png"), TextureFilter.Linear);

        setupPlayer1Text();
    }

    private void createPlayer2Text() {
        player2 = new Text(renderer);
        player2.setText("Jogador 2");
        player2.setFontSize(20);
        player2.setFontType(FontType.Bold);
        player2.setColor(255, 255, 255, 0.78f);

        player2Icon = new Image(renderer);
        player2Icon.setAlpha(0.78f);
        player2Icon.loadFromFile(getClass().getResource("board/player_2.png"), TextureFilter.Linear);

        setupPlayer2Text();
    }

    private void setupResultText() {
        final int w = (int)result.width();

        result.setPosition(sceneGameRect.sidebar_w + (int)((sceneGameRect.content_w - w) / 2), 26);
    }

    private void setupPlayer1Text() {
        final int text_w = (int)player1.width();
        final int icon_w = 24;
        final int icon_h = 24;
        final int text_x = (sceneGameRect.sidebar_w + ((sceneGameRect.content_w - text_w) / 2)) - (text_w / 2) - margin - icon_w - space;
        final int icon_x = (sceneGameRect.sidebar_w + ((sceneGameRect.content_w - icon_w) / 2)) - (icon_w / 2) - margin;

        player1.setPosition(text_x, 28);

        player1Icon.setSize(icon_w, icon_h);
        player1Icon.setPosition(icon_x, 32);
    }

    private void setupPlayer2Text() {
        final int text_w = (int)player2.width();
        final int icon_w = 22;
        final int icon_h = 22;
        final int text_x = (sceneGameRect.sidebar_w + ((sceneGameRect.content_w - text_w) / 2)) + (text_w / 2) + margin + icon_w + space;
        final int icon_x = (sceneGameRect.sidebar_w + ((sceneGameRect.content_w - icon_w) / 2)) + (icon_w / 2) + margin;

        player2.setPosition(text_x, 28);

        player2Icon.setSize(icon_w, icon_h);
        player2Icon.setPosition(icon_x, 33);
    }
}
