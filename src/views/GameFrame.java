package views;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(String title, int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}

