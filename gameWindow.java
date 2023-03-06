package catch_the_drop;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class gameWindow extends JFrame {
    private static gameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image gameOver;
    private static Image drop;

    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 50;
    private static int score;

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(gameWindow.class.getResourceAsStream("background5.jpg"));
        gameOver = ImageIO.read(gameWindow.class.getResourceAsStream("gameOver.png"));
        drop = ImageIO.read(gameWindow.class.getResourceAsStream("drop7.png"));
        game_window = new gameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop) {
                    drop_top = -100;
                    drop_left = (int) (Math.random() *(game_field.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v + 10;
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top + drop_v * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int)drop_left, (int)drop_top, null);
        if (drop_top > game_window.getHeight()) g.drawImage(gameOver, 280, 120, null);
    }
    private static class GameField extends JPanel{

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
