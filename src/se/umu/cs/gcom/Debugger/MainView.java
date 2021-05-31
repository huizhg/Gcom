package se.umu.cs.gcom.Debugger;

import javax.swing.*;
import java.awt.*;


public class MainView extends JFrame {
    private JFrame frame = new JFrame("G-Login");

    private JPanel loginPanel = new JPanel();
    private JLabel userLabel = new JLabel("User:");           // 创建UserJLabel
    private JTextField userText = new JTextField();           // 获取登录名
    private JButton loginButton = new JButton("login");       // 创建登录按钮

    private JPanel userPanel = new JPanel();
    private JTextField createGroupField;
    private JComboBox comTypeBox;
    private JComboBox orderTypeBox;
    private JButton createButton;
    private JButton joinButton1;
    private JList GrouplistField;
    private JLabel createGroupLabel;
    private JLabel communicationLabel;
    private JLabel joinGroupLabel;
    private JTextField joinGroupField;
    private JLabel orderingLabel;
    private JButton removeButton;

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getUserPanel() {
        return userPanel;
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JTextField getUserText() {
        return userText;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public MainView() {
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);                     //在屏幕中居中显示
        frame.add(loginPanel);                                      // 添加面板
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置X号后关闭
        buildLoginView(loginPanel);                                //往窗体里放其他控件
        frame.setVisible(true);                                //设置窗体可见
    }
    public void buildUserView (String userName){
        frame.add(userPanel);
        frame.setTitle("GCom: User - "+userName);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);

        userPanel.setLayout(new GridBagLayout());
        createGroupLabel = new JLabel();
        createGroupLabel.setText("Create Group");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(createGroupLabel, gbc);
        communicationLabel = new JLabel();
        communicationLabel.setText("Communication");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(communicationLabel, gbc);
        createGroupField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(createGroupField, gbc);
        joinGroupLabel = new JLabel();
        joinGroupLabel.setText("Join Group");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(joinGroupLabel, gbc);
        joinGroupField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(joinGroupField, gbc);
        orderingLabel = new JLabel();
        orderingLabel.setText("Ordering");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        userPanel.add(orderingLabel, gbc);
        comTypeBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(comTypeBox, gbc);
        orderTypeBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(orderTypeBox, gbc);
        createButton = new JButton();
        createButton.setText("Create");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(createButton, gbc);
        removeButton = new JButton();
        removeButton.setText("Remove");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(removeButton, gbc);
        joinButton1 = new JButton();
        joinButton1.setText("Join");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userPanel.add(joinButton1, gbc);
        GrouplistField = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        GrouplistField.setModel(defaultListModel1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        userPanel.add(GrouplistField, gbc);

//        userPanel.setEnabled(true);
//        userPanel.setVisible(true);
    }
    private void buildLoginView(JPanel loginPanel) {

        loginPanel.setLayout(null);  //设置布局为 null

        userLabel.setBounds(50, 30, 80, 25);
        loginPanel.add(userLabel);

        userText.setBounds(105, 30, 165, 25);
        loginPanel.add(userText);

        loginButton.setBounds(105, 100, 80, 25);
        loginPanel.add(loginButton);

    }
}
