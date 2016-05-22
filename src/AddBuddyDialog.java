import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AddBuddyDialog extends JDialog implements ActionListener, DocumentListener
{
    JLabel     buddyMessage;
    JTextField buddyNameTF;
    JButton    sendRequestButton,
               cancelButton;
    
    JPanel     textFieldPanel,
               buttonPanel;
    
    ConnectionToServer localcts;
    
    Toolkit tk;
    Dimension d;
    
    String myUsername;
    
    public AddBuddyDialog(ConnectionToServer cts, String myUsername)
    {
        localcts = cts;
        this.myUsername = myUsername;
        
        buddyMessage = new JLabel("Please enter the username of the buddy you wish to add.");
        
        buddyNameTF = new JTextField(25);
        buddyNameTF.getDocument().addDocumentListener(this);
        textFieldPanel = new JPanel();
        textFieldPanel.add(buddyNameTF);        
        
        sendRequestButton = new JButton("SEND");
        sendRequestButton.setActionCommand("SEND");
        sendRequestButton.addActionListener(this);
        sendRequestButton.setEnabled(false);
        
        cancelButton = new JButton("CANCEL");
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(this);
        
        buttonPanel = new JPanel();
        buttonPanel.add(sendRequestButton);
        buttonPanel.add(cancelButton);
        
        this.add(buddyMessage, BorderLayout.NORTH);
        this.add(textFieldPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        
        setSize(d.width/3, d.height/5);
        setLocation(d.width/3, d.height/3);
        
        this.setModal(true);
        this.setVisible(true);     
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("SEND"))
            {
                if(buddyNameTF.getText().equals(myUsername))
                {
                    new JOptionPane();
                    JOptionPane.showMessageDialog(this, "Error: You cannot add yourself as a buddy.");
                }
                
                else
                {
                    localcts.send("BUDDY REQUEST TO|" + buddyNameTF.getText()
                            + "|" + myUsername);
                    dispose();
                }
            }
        
        else if (e.getActionCommand().equals("CANCEL"))
        {
            this.dispose();
        }
    }

    public void insertUpdate(DocumentEvent e) 
    {        
        if (buddyNameTF.getText().trim().length() < 3)
            sendRequestButton.setEnabled(false);
        else
            sendRequestButton.setEnabled(true);
    }

    public void removeUpdate(DocumentEvent e) 
    {
        if (buddyNameTF.getText().trim().length() < 3)
            sendRequestButton.setEnabled(false);
        else
            sendRequestButton.setEnabled(true);        
    }

    public void changedUpdate(DocumentEvent e) {};
}

