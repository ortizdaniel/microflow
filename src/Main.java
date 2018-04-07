import model.Manager;
import model.entities.TAD;
import model.relations.Call;
import view.MainView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainView view = new MainView();
            view.setVisible(true);

            TAD t1, t2;
            Call c;
            view.addDrawable(t1 = Manager.createTAD(20, 20));
            view.addDrawable(t2 = Manager.createTAD(300, 300));
            view.addDrawable(Manager.createInterface(100, 30));
            view.addDrawable(Manager.createState(300, 70));
            view.addDrawable(Manager.createPeripheral(40, 500));
            view.addDrawable(c = Manager.createCall(t1, t2));
        });
    }
}
