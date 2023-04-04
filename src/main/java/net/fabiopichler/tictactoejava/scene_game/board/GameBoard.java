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

package net.fabiopichler.tictactoejava.scene_game.board;

import net.fabiopichler.tictactoejava.base.*;
import net.fabiopichler.tictactoejava.base.drawables.Button;
import net.fabiopichler.tictactoejava.base.drawables.Image;
import net.fabiopichler.tictactoejava.base.drawables.RectangleShape;
import net.fabiopichler.tictactoejava.base.opengl_renderer.OpenGLRenderer;
import net.fabiopichler.tictactoejava.base.vectors.IntRect;
import net.fabiopichler.tictactoejava.scene_game.GameComponent;
import net.fabiopichler.tictactoejava.scene_game.SceneGameRect;

public class GameBoard extends GameComponent {
    public interface GameEventHandler {
        void call(final GameBoard game);
    }

    private static class Board {
        public final BoardItem[][] items = new BoardItem[3][3];
        public IntRect rect;
        public int item_size;
        public int space;
    }

    private final Board board = new Board();
    private final RectangleShape background;
    private final Image player1Image;
    private final Image player2Image;
    private Player player = Player.Player_1;
    private Player gameResult = Player.None;
    private int round;
    private GameEventHandler gameEvent;
    private double p1Angle;

    public GameBoard(final OpenGLRenderer renderer, final SceneGameRect sceneGameRect) {
        super(renderer, sceneGameRect);

        final int board_size = 304;
        final int board_x = sceneGameRect.sidebar_w + ((sceneGameRect.content_w - board_size) / 2);
        final int board_y = (sceneGameRect.window_h - board_size) / 2;

        board.item_size = 98;
        board.space = 5;
        board.rect = new IntRect(board_x, board_y, board_size, board_size);

        background = new RectangleShape(renderer, board_size, board_size);
        background.setPosition(board.rect.x, board.rect.y);
        background.setColor(90, 0, 110);

        player1Image = new Image(renderer);
        player2Image = new Image(renderer);

        player1Image.loadFromFile(getClass().getResource("player_1.png"), TextureFilter.Linear);
        player2Image.loadFromFile(getClass().getResource("player_2.png"), TextureFilter.Linear);

        player1Image.setAlpha(0.78f);
        player2Image.setAlpha(0.78f);

        setupBoard();
    }

    public void release() {
        for (final BoardItem[] row : board.items)
            for (final BoardItem col : row)
                col.button.release();

        player1Image.release();
        player2Image.release();
    }

    public void processEvent(final Event event) {
        for (final BoardItem[] row : board.items)
            for (final BoardItem col : row)
                col.button.processEvent(event);
    }

    public void update(final double deltaTime) {
        p1Angle = p1Angle + 30.0 * deltaTime;

        if (p1Angle > 360.0)
            p1Angle = 0.0;

        for (final BoardItem[] row : board.items) {
            for (final BoardItem col : row) {
                final Image icon = col.button.icon();

                if (icon != null)
                    icon.setAngle(col.player == Player.Player_1 ? p1Angle : 0);
            }
        }
    }

    public void draw() {
        background.draw();

        for (final BoardItem[] row : board.items)
            for (final BoardItem col : row)
                col.button.draw();
    }

    public void setGameEvent(final GameEventHandler handler) {
        gameEvent = handler;
    }

    public Player getCurrentPlayer() {
        return player;
    }

    public Player getGameResult() {
        return gameResult;
    }

    private void setupBoard() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                final BoardItem item = board.items[row][col] = new BoardItem();
                item.player = Player.None;
                item.button = new Button(renderer);

                item.button.setSize(board.item_size, board.item_size);
                item.button.setPosition(
                        board.rect.x + (col * board.item_size) + (col * board.space),
                        board.rect.y + (row * board.item_size) + (row * board.space)
                );
                item.button.setOnPressEvent(this::onItemPress);
                item.button.setBackgroundColor(160, 40, 180);
                item.button.setBackgroundHoverColor(200, 50, 220);
                item.button.setBackgroundPressedColor(140, 40, 160);
            }
        }
    }

    private void onItemPress(Button button) {
        for (final BoardItem[] row : board.items) {
            for (final BoardItem col : row) {
                if (col.button == button) {
                    check(col);

                    return;
                }
            }
        }
    }

    private void check(final BoardItem item) {
        if (item.player != Player.None || gameResult != Player.None)
            return;

        item.player = player;
        gameResult = checkWinner();
        player = player == Player.Player_1 ? Player.Player_2 : Player.Player_1;

        if (gameEvent != null)
            gameEvent.call(this);

        item.button.setIcon(item.player == Player.Player_1 ? player1Image : player2Image);

        round++;
    }

    private Player checkWinner() {
        Player player = BoardUtil.checkBoardRows(3, board.items);

        if (player == Player.None) {
            final var items = new BoardItem[3][3];
            BoardUtil.transposeBoard(board.items, items);
            player = BoardUtil.checkBoardRows(3, items);
        }

        if (player == Player.None)
            player = BoardUtil.checkBoardDiagonals(board.items);

        if (player.ordinal() > Player.None.ordinal())
            return player;

        if (player == Player.None && round == 8)
            return Player.Tied;

        return Player.None;
    }
}
