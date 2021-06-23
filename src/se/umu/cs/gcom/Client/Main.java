package se.umu.cs.gcom.Client;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                MainView testView = new MainView();
                MainController testController = new MainController(testView);
            }
        });
    }
}
