package se.umu.cs.gcom.Client;

import se.umu.cs.gcom.GCom.Message;

import javax.swing.*;
import java.awt.*;


public class MainView extends JFrame {
    private JFrame frame = new JFrame("G-Login");
    private JPanel loginPanel = new JPanel();
    private JLabel userLabel = new JLabel("User:");
    private JTextField userText = new JTextField();
    private JButton loginButton = new JButton("login");
    private JPanel groupPanel = new JPanel();
    private JTextField createGroupField;
    private JComboBox comTypeBox;
    private JComboBox orderTypeBox;
    private JButton createButton;
    private JButton joinButton;

    private JList grouplistField;
    private JLabel createGroupLabel;
    private JLabel communicationLabel;
    private JLabel joinGroupLabel;
    private JTextField joinGroupField;
    private JLabel orderingLabel;
    private JButton removeButton;

    //UserView
    private JList messagelist;
    private JTextField inputMessage;
    private JButton send;
    private JList memberlist;
    private JButton leaveButton;
    private JButton debugButton;
    private JButton updateMemberButton;
    private JButton removeMemberButton;
    private JButton addMemberButton;
    private JPanel userView;
    //DebuggerView
    private JFrame debugframe;
    private JPanel debugPanel;
    private JButton debugsendButton;
    private JButton debugdeliverButton;
    private JButton debugshowButton;
    private JList debugmessagesList;
    private JList debugqueueList;
    public DefaultListModel<Message> msglistModel = new DefaultListModel<>();
    public DefaultListModel<String> queuelistModel = new DefaultListModel<>();

    //BackendView
    private JFrame backendframe = new JFrame();
    private JLabel DeliverdMsg;
    public JList BackendArea = new JList();
    private JLabel Performance;
    public JList PerformanceArea = new JList();

    public JButton getDebugdeliverButton() {
        return debugdeliverButton;
    }

    public JList getDebugqueueList() {
        return debugqueueList;
    }

    public JList getDebugmessagesList() {
        return debugmessagesList;
    }

    public JButton getDebugsendButton() {
        return debugsendButton;
    }

    public JFrame getDebugframe() {
        return debugframe;
    }

    //UserView

    public JButton getRemoveMemberButton() {
        return removeMemberButton;
    }

    public JButton getAddMemberButton() {
        return addMemberButton;
    }

    public JPanel getUserView() {
        return userView;
    }

    public JList getMemberlist() {
        return memberlist;
    }

    public JButton getDebugButton() {
        return debugButton;
    }

    public JButton getUpdateMemberButton() {
        return updateMemberButton;
    }

    public JButton getSend() {
        return send;
    }

    public JTextField getInputMessage() {
        return inputMessage;
    }

    public JList getMessagelist() {
        return messagelist;
    }

    public JButton getLeaveButton() {
        return leaveButton;
    }

    //GroupView
    public JList getGrouplistField() {
        return grouplistField;
    }
    public JTextField getCreateGroupField() {
        return createGroupField;
    }
    public JButton getCreateButton() {
        return createButton;
    }
    public JButton getRemoveButton() {
        return removeButton;
    }
    public JButton getJoinButton() {
        return joinButton;
    }

    public JTextField getJoinGroupField() {
        return joinGroupField;
    }

    public JComboBox getComTypeBox() {
        return comTypeBox;
    }

    public JComboBox getOrderTypeBox() {
        return orderTypeBox;
    }

    public JPanel getGroupPanel() {
        return groupPanel;
    }

    // LoginView
    public JPanel getLoginPanel() {
        return loginPanel;
    }
    public JTextField getUserText() {
        return userText;
    }
    public JButton getLoginButton() {
        return loginButton;
    }

    public JFrame getFrame() {
        return frame;
    }

    public MainView() {
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.add(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildLoginView(loginPanel);
        frame.setVisible(true);
        debugframe = new JFrame();
        DefaultListModel mModel = new DefaultListModel();
        BackendArea.setModel(mModel);
        DefaultListModel pModel = new DefaultListModel();
        PerformanceArea.setModel(pModel);
    }
    public void builddebugView (String userName){
        debugframe.setTitle("Debugger - "+userName);
        debugframe.setSize(1400,600);
        debugframe.setLocationRelativeTo(null);
        debugframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        debugPanel = new JPanel();
        debugframe.add(debugPanel);

        debugPanel.setLayout(new GridBagLayout());

        debugmessagesList = new JList();
        debugmessagesList.setModel(msglistModel);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.weightx = 9.0;
        gbc.weighty = 9.0;
        gbc.fill = GridBagConstraints.BOTH;
        debugPanel.add(debugmessagesList, gbc);

        debugsendButton = new JButton();
        debugsendButton.setText("Send");
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        debugPanel.add(debugsendButton);

        debugqueueList = new JList();
        debugqueueList.setModel(queuelistModel);
//        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.weightx = 9.0;
        gbc.weighty = 9.0;
        gbc.fill = GridBagConstraints.BOTH;
        debugPanel.add(debugqueueList, gbc);


        debugdeliverButton = new JButton();
        debugdeliverButton.setText("Deliver");
        gbc.gridx = 3;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        debugPanel.add(debugdeliverButton);

        debugframe.setVisible(true);
    }
    public void buildbackendView(){
        backendframe.setTitle("Backend");
        backendframe.setSize(700,600);
        backendframe.setLocationRelativeTo(null);
        backendframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        final JPanel panel1 = new JPanel();
        backendframe.add(panel1);

        panel1.setLayout(new GridBagLayout());
        DeliverdMsg = new JLabel();
        DeliverdMsg.setText("DeliverdMsg");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(DeliverdMsg, gbc);
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(BackendArea, gbc);
        Performance = new JLabel();
        Performance.setText("Performance");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(Performance, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(PerformanceArea, gbc);
        backendframe.setVisible(true);
    }
    public void buildUserView (String userName){
        frame.setTitle("GCom Chat View: User - "+userName);
        userView = new JPanel();
        frame.add(userView);

        userView.setLayout(new GridBagLayout());

        messagelist = new JList();
        DefaultListModel msgModel = new DefaultListModel();
        messagelist.setModel(msgModel);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 10;
        gbc.weightx = 9.0;
        gbc.weighty = 9.0;
        gbc.fill = GridBagConstraints.BOTH;
        userView.add(messagelist, gbc);

        inputMessage = new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(inputMessage, gbc);

        send = new JButton();
        send.setText("Send");

        gbc.gridx = 1;
        gbc.gridy = 10;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(send, gbc);

        updateMemberButton = new JButton();
        updateMemberButton.setText("Update");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(updateMemberButton, gbc);

        leaveButton = new JButton();
        leaveButton.setText("Leave");
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(leaveButton, gbc);

        debugButton = new JButton();
        debugButton.setText("Debug");
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(debugButton, gbc);


        removeMemberButton = new JButton();
        removeMemberButton.setText("Remove");
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(removeMemberButton, gbc);
        addMemberButton = new JButton();
        addMemberButton.setText("Add");

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userView.add(addMemberButton, gbc);

        memberlist = new JList();

        final DefaultListModel defaultListModel1 = new DefaultListModel();
        memberlist.setModel(defaultListModel1);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 11;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        userView.add(memberlist, gbc);

    }
    public void buildGroupView (String userName){
        frame.add(groupPanel);
        frame.setTitle("GCom: User - "+userName);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);

        groupPanel.setLayout(new GridBagLayout());
        createGroupLabel = new JLabel();
        createGroupLabel.setText("Create Group");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        groupPanel.add(createGroupLabel, gbc);
        communicationLabel = new JLabel();
        communicationLabel.setText("Communication");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        groupPanel.add(communicationLabel, gbc);
        createGroupField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        groupPanel.add(createGroupField, gbc);

        joinGroupLabel = new JLabel();
        joinGroupLabel.setText("Join Group");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        groupPanel.add(joinGroupLabel, gbc);
        joinGroupField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        groupPanel.add(joinGroupField, gbc);

        orderingLabel = new JLabel();
        orderingLabel.setText("Ordering");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        groupPanel.add(orderingLabel, gbc);
        comTypeBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        comTypeBox.addItem("Non reliable");
        groupPanel.add(comTypeBox, gbc);
        orderTypeBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        orderTypeBox.addItem("Unordered");
        orderTypeBox.addItem("Causal");
        groupPanel.add(orderTypeBox, gbc);
        createButton = new JButton();
        createButton.setText("Create");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        groupPanel.add(createButton, gbc);
        removeButton = new JButton();
        removeButton.setText("Remove");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        groupPanel.add(removeButton, gbc);
        joinButton = new JButton();
        joinButton.setText("Join");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        groupPanel.add(joinButton, gbc);
        grouplistField = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        grouplistField.setModel(defaultListModel1);
        grouplistField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        groupPanel.add(grouplistField, gbc);

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
