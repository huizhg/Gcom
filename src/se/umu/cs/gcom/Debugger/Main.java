package se.umu.cs.gcom.Debugger;

public class Main {
    public static void main(String[] args) {
        MainView testView = new MainView();
        MainController testController = new MainController(testView);
    }
}
