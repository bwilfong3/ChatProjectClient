import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PasswordDialog extends JDialog implements ActionListener, DocumentListener
{
    ConnectionToServer localcts;
    
    JTextField userIDTF,
               passTF;
    
    JButton    registerButton,
               loginButton,
               exitButton;
    
    JPanel     textFieldPanel,
               buttonPanel,
               userPanel,
               pwPanel;
    
    JLabel     userLabel,
               pwLabel,
               statusLabel;
    
    Toolkit    tk;
    
    Dimension  d;
    
    PasswordDialog(Talker t)
    {
        localcts = new ConnectionToServer(t, new PWDialogUpdater(this));
        
        registerButton = new JButton("Register New User");
        registerButton.setActionCommand("REGISTER");
        registerButton.addActionListener(this);
        registerButton.setEnabled(false);
        
        loginButton = new JButton("Login");
        loginButton.setActionCommand("LOGIN");
        loginButton.addActionListener(this);
        loginButton.setEnabled(false);
        
        exitButton = new JButton("Exit");
        exitButton.setActionCommand("EXIT");
        exitButton.addActionListener(this);
        
        userIDTF = new JTextField(15);
        passTF   = new JTextField(15);
        userIDTF.getDocument().addDocumentListener(this);
        passTF.getDocument().addDocumentListener(this);
        
        userPanel = new JPanel();
        userPanel.add(userIDTF);
        pwPanel   = new JPanel();
        pwPanel.add(passTF);
        
        userLabel = new JLabel("Username: ");
        pwLabel   = new JLabel("Password: ");
        statusLabel = new JLabel("Welcome to ShoutOut 0.0");
        
        textFieldPanel = new JPanel(new GridLayout(2,2));
        textFieldPanel.add(userLabel);
        textFieldPanel.add(userPanel);
        textFieldPanel.add(pwLabel);
        textFieldPanel.add(pwPanel);
        
        buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        this.add(statusLabel, BorderLayout.NORTH);
        this.add(textFieldPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        
        setSize(d.width/3, d.height/5);
        setLocation(d.width/3, d.height/3);
        
        this.setModal(true);
        this.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals("LOGIN"))
            localcts.send("LOGIN|" + userIDTF.getText() + "|" + passTF.getText());
                
        else if (ae.getActionCommand().equals("REGISTER"))
            localcts.send("REGISTER|" + userIDTF.getText() + "|" + passTF.getText());
            
        else if (ae.getActionCommand().equals("EXIT"))
        {
            this.dispose();
            return;
        }
    }
    
    public void insertUpdate(DocumentEvent de)
    {
        if (userIDTF.getText().trim().length() < 3 || 
            passTF.getText().trim().length() < 3)
        {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
        
        else
        {   
            if (userIDTF.getText().contains("#")
             || userIDTF.getText().contains("|")
             || userIDTF.getText().contains("%"))
                   {
                        statusLabel.setText("Invalid symbol. Please refrain from using"        
                                         +  " special symbols in your username.");
                        
                        registerButton.setEnabled(false);
                        loginButton.setEnabled(false);    
                   }
                    
             else
             {
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
             }
        }
        

    }
    public void removeUpdate(DocumentEvent de)
    {
        if (userIDTF.getText().trim().length() < 3 || 
            passTF.getText().trim().length() < 3)
        {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
        
        else
        {   
            if (userIDTF.getText().contains("#")
             || userIDTF.getText().contains("|")
             || userIDTF.getText().contains("%"))
                   {
                        statusLabel.setText("Invalid symbol. Please refrain from using"        
                                         +  " special symbols in your username.");
                        
                        registerButton.setEnabled(false);
                        loginButton.setEnabled(false);    
                   }
                    
             else
             {
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
             }
        }
        

    }
    
    public void changedUpdate(DocumentEvent de)
    {}
   
}
