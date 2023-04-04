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
import net.fabiopichler.tictactoejava.base.drawables.Button;
import net.fabiopichler.tictactoejava.base.drawables.RectangleShape;
import net.fabiopichler.tictactoejava.base.vectors.IntVec2;
import net.fabiopichler.tictactoejava.scene_game.board.GameBoard;
import net.fabiopichler.tictactoejava.scene_game.board.Player;

public class SceneGame extends Scene {
    private final SceneGameRect sceneGameRect = new SceneGameRect();
    private RectangleShape background;
    private GameBoard gameBoard;
    private Sidebar sidebar;
    private Header header;
    private Footer footer;
    private int player1WinCount;
    private int player2WinCount;
    private int tiedCount;

    @Override
    public void onCreate() {
        final IntVec2 windowSize = window.size();

        sceneGameRect.window_w = windowSize.x;
        sceneGameRect.window_h = windowSize.y;
        sceneGameRect.sidebar_w = 200;
        sceneGameRect.sidebar_h = windowSize.y;
        sceneGameRect.content_w = windowSize.x - sceneGameRect.sidebar_w;
        sceneGameRect.content_h = windowSize.y;

        background = new RectangleShape(renderer, sceneGameRect.window_w, sceneGameRect.window_h);
        background.setColor(new Color().top(150, 40, 170).bottom(80, 0, 100));

        sidebar = new Sidebar(renderer, sceneGameRect);
        header = new Header(renderer, sceneGameRect);

        footer = new Footer(renderer, sceneGameRect);
        footer.getRestartButton().setOnPressEvent(this::onPressed);

        newGame();
    }

    @Override
    public void onDestroy() {
        gameBoard.release();
        footer.release();
        header.release();
        sidebar.release();
    }

    @Override
    public void onProcessEvent(Event event) {
        header.processEvent(event);
        footer.processEvent(event);
        gameBoard.processEvent(event);
    }

    @Override
    public void onUpdate(double deltaTime) {
        header.update(deltaTime);
        gameBoard.update(deltaTime);
    }

    @Override
    public void onDraw() {
        background.draw();
        gameBoard.draw();
        header.draw();
        footer.draw();
        sidebar.draw();
    }

    private void newGame() {
        if (gameBoard != null)
            gameBoard.release();

        gameBoard = new GameBoard(renderer, sceneGameRect);
        gameBoard.setGameEvent(this::onGameEvent);

        header.setCurrentPlayer(Player.Player_1, Player.None);
    }

    private void onPressed(final Button button) {
        newGame();
    }

    private void onGameEvent(final GameBoard gameBoard){
        final Player player = gameBoard.getCurrentPlayer();
        final Player gameResult = gameBoard.getGameResult();

        header.setCurrentPlayer(player, gameResult);

        if (gameResult == Player.Player_1)
            sidebar.setPlayer1WinText(++player1WinCount);

        else if (gameResult == Player.Player_2)
            sidebar.setPlayer2WinText(++player2WinCount);

        else if (gameResult == Player.Tied)
            sidebar.setTiedCountText(++tiedCount);
    }
}
