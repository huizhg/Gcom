package se.umu.cs.gcom.Debugger;

import javax.swing.*;


public class LoginView extends JFrame {
    private JFrame frame = new JFrame("GCom Login");
    private JPanel panel = new JPanel();
    private JLabel userLabel = new JLabel("User:");           // 创建UserJLabel
    private JTextField userText = new JTextField();           // 获取登录名
    private JButton loginButton = new JButton("login");       // 创建登录按钮

    public JTextField getUserText() {
        return userText;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public LoginView() {
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);                     //在屏幕中居中显示
        frame.add(panel);                                      // 添加面板
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置X号后关闭
        placeComponents(panel);                                //往窗体里放其他控件
        frame.setVisible(true);                                //设置窗体可见
    }
    private void placeComponents(JPanel panel) {

        panel.setLayout(null);  //设置布局为 null

        userLabel.setBounds(100, 60, 160, 25);
        panel.add(userLabel);

        userText.setBounds(210, 60, 330, 25);
        panel.add(userText);

        loginButton.setBounds(210, 200, 160, 25);
        panel.add(loginButton);

    }
}
