package se.umu.cs.gcom.Debugger;

public class Main {
    public static void main(String[] args) {
        LoginView testView = new LoginView();
        LoginController testController = new LoginController(testView);
    }
}
