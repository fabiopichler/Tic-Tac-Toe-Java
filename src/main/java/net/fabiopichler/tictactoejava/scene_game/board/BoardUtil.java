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

public class BoardUtil {
    private static boolean checkBoardRow(final BoardItem[] row, final Player player) {
        int len = 0;

        for (int i = 0; i < 3; ++i)
            if (row[i].player == player)
                ++len;

        return len == 3;
    }

    public static Player checkBoardRows(final int rows, final BoardItem[][] board) {
        for (int i = 0; i < rows; ++i) {
            final BoardItem[] row = board[i];

            for (int player = Player.Player_1.ordinal(); player <= Player.Player_2.ordinal(); ++player) {
                final Player[] values = Player.values();

                if (checkBoardRow(row, values[player]))
                    return values[player];
            }
        }

        return Player.None;
    }

    public static Player checkBoardDiagonals(final BoardItem[][] board) {
        final var diagonals = new BoardItem[2][3];

        for (int i = 0; i < 3; ++i) {
            diagonals[0][i] = board[i][i];
            diagonals[1][i] = board[i][(3 - 1) - i];
        }

        return checkBoardRows(2, diagonals);
    }

    public static void transposeBoard(final BoardItem[][] itemA, final BoardItem[][] itemB) {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                itemB[i][j] = itemA[j][i];
    }
}
