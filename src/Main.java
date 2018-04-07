import view.MainView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView view = new MainView();
            view.setVisible(true);
        });
    }
}
