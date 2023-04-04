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

package net.fabiopichler.tictactoejava;

import net.fabiopichler.tictactoejava.base.Graphics;
import net.fabiopichler.tictactoejava.base.SceneManager;
import net.fabiopichler.tictactoejava.base.Window;

import net.fabiopichler.tictactoejava.base.freetype.FontManager;
import net.fabiopichler.tictactoejava.base.freetype.FontType;
import net.fabiopichler.tictactoejava.scene_game.SceneGame;
import org.lwjgl.*;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class App {
    private final Window window;
    private final Graphics graphics;
    private final SceneManager sceneManager;
    private final GLFWErrorCallback errCallback;

    public App() {
        System.out.println("LWJGL " + Version.getVersion());

        glfwSetErrorCallback(errCallback = new GLFWErrorCallback() {
            private final GLFWErrorCallback delegate = GLFWErrorCallback.createPrint(System.err);

            @Override
            public void invoke(int error, long description) {
                if (error == GLFW_VERSION_UNAVAILABLE)
                    System.err.println("This app requires OpenGL 3.3 or higher.");

                delegate.invoke(error, description);
            }

            @Override
            public void free() {
                delegate.free();
            }
        });

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        FontManager.init();
        FontManager fontManager = FontManager.instance();
        fontManager.setCharCodeList("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890ÁÂÃÉÊÍÓÔÕÚÇáâãéêíóôõúç,.;[]{}()<>-+=\\|/'\"!@#$%&*?©");
        fontManager.addFont("Noto Sans", FontType.Normal, "/fonts/NotoSans-Regular.ttf", 0);
        fontManager.addFont("Noto Sans", FontType.Bold, "/fonts/NotoSans-Bold.ttf", 0);

        window = new Window(640, 480, "Tic Tac Toe (Java)");
        window.setWindowIcon(getClass().getResource("scene_game/board/player_1.png"));

        graphics = new Graphics(window);

        sceneManager = new SceneManager(window, graphics);
        sceneManager.goTo(new SceneGame());
    }

    public void release() {
        sceneManager.release();
        graphics.release();
        window.release();

        FontManager.close();

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void run() {
        window.show();
        sceneManager.run();
    }
}
