import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ChatGUI extends JFrame implements ActionListener, ListSelectionListener
{
    JLabel buddyLabel;
    
    JList<BuddyObject> buddyList;
    DefaultListModel<BuddyObject> dlm;
    
    JPanel buttonPanel;
    
    JButton addBuddyButton,
            deleteBuddyButton,
            startChatButton;
    
    JScrollPane jsp;
    
    String username;
    
    ConnectionToServer localcts;
    
    BuddyDialogUpdater bdu;
    
    Hashtable<String, ChatWindow> ht;
    
    Container cp;

    public ChatGUI(ConnectionToServer cts, String username)
    {
        localcts = cts;
        
        this.username = username;
        
        cp = getContentPane();
        
        ht = new Hashtable<String, ChatWindow>();
        
        buddyLabel = new JLabel("My Buddies");
        cp.add(buddyLabel, BorderLayout.NORTH);
        
        dlm = new DefaultListModel<BuddyObject>();
        buddyList = new JList<BuddyObject>(dlm);
        buddyList.addListSelectionListener(this);
        jsp = new JScrollPane(buddyList);
        cp.add(jsp, BorderLayout.CENTER);
        
        buttonPanel = new JPanel(new GridLayout(3,1));
        
        addBuddyButton = new JButton("Add New Buddy");
        addBuddyButton.setActionCommand("ADD BUDDY");
        addBuddyButton.addActionListener(this);
        buttonPanel.add(addBuddyButton);
        
        deleteBuddyButton = new JButton("Delete Buddy");
        deleteBuddyButton.setActionCommand("DELETE BUDDY");
        deleteBuddyButton.addActionListener(this);
        deleteBuddyButton.setEnabled(false);
        buttonPanel.add(deleteBuddyButton);
        
        startChatButton = new JButton("Start Chatting!");
        startChatButton.setActionCommand("START CHAT");
        startChatButton.addActionListener(this);
        startChatButton.setEnabled(false);
        buttonPanel.add(startChatButton);
        
        cp.add(buttonPanel, BorderLayout.SOUTH);
        
        setupMainFrame();
        
    }
    
    public void setupMainFrame()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(d.width/5, d.height/2);
        
        setLocation(d.width/3, d.height/3);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        setTitle("ShoutOut 1.0");
        
        setVisible(true);   
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("ADD BUDDY"))
            bdu = new BuddyDialogUpdater(new AddBuddyDialog(localcts, username));
        
        else if (e.getActionCommand().equals("START CHAT"))
        {
            ht.put(dlm.elementAt(buddyList.getSelectedIndex()).username,
                   new ChatWindow(dlm.elementAt(buddyList.getSelectedIndex()).username, username, localcts));
            localcts.send("CHAT REQUEST TO|" + dlm.elementAt(buddyList.getSelectedIndex()).username + "|" + username );
        }
    }
    
    public void buddyRequestReceived(String buddy)
    {
        new JOptionPane();
        int accepted = JOptionPane.showConfirmDialog(this, "Buddy Request from " + buddy + ".\n"
                                                             + "Do you want to accept this request?");
        
        if (accepted == JOptionPane.YES_OPTION)
        {
            localcts.send("BUDDY ACCEPTED|" + buddy + "|" + username);
            this.addBuddy(buddy);
        }
    }
    
    public void badUsername()
    {
        new JOptionPane();
        JOptionPane.showMessageDialog(this, "Error: Username does not exist.");
        
        bdu = new BuddyDialogUpdater(new AddBuddyDialog(localcts, username));
    }
    
    public void addBuddy(String buddy)
    {
        BuddyObject newBud = new BuddyObject(buddy, true);
        dlm.addElement(newBud);
    }

    public void valueChanged(ListSelectionEvent e) 
    {
        if(buddyList.getSelectedIndex() >= 0)
        {
            deleteBuddyButton.setEnabled(true);
            startChatButton.setEnabled(true);
        }
        
        else
        {
            deleteBuddyButton.setEnabled(false);
            startChatButton.setEnabled(false); 
        }
    }
    
    public void displayChatRequest(String buddy)
    {
        new JOptionPane();
        int decision = JOptionPane.showConfirmDialog(this, buddy + " has requested to chat with you. Do you accept?");
        
        if (decision == JOptionPane.YES_OPTION)
        {
            ht.put(buddy, new ChatWindow(buddy, username, localcts));
            localcts.send("CHAT REQUEST ACCEPTED|" + username + "|" + buddy);
        }
        
        else
            localcts.send("CHAT REQUEST DENIED|" + username + "|" + buddy);
        
    }
    
    public void chatAccepted(String buddy)
    {
        ht.get(buddy).chat.append(buddy + " has accepted your chat request.\n");
    }
    
    public void chatDenied(String buddy)
    {
        ht.get(buddy).chat.append(buddy + " has denied your chat request.\n");
    }
    
    public void displayMessage(String buddy, String message)
    {
        ht.get(buddy).chat.append(buddy + ": " + message + "\n");
    }
}
