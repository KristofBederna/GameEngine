package views;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(String title) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(title);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}

