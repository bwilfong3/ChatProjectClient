import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class ChatWindow extends JFrame implements ActionListener, DocumentListener
{
    String chattingWith,
           user;
    
    ConnectionToServer cts;
    
    JLabel whoIsIt;
    
    JPanel chatStuff;
    
    JTextArea chat,
              yourText;
    
    JScrollPane jsp;
    
    JButton sendButton;

    
    public ChatWindow(String chattingWith, String user, ConnectionToServer cts)
    {
        this.chattingWith = chattingWith;
        this.user = user;
        this.cts = cts;
        
        whoIsIt = new JLabel("Chatting with " + chattingWith);
        
        chat = new JTextArea(30, 30);
        chat.setEditable(false);
        chat.setLineWrap(true);
        jsp = new JScrollPane(chat);
        
        yourText = new JTextArea(5, 30);
        yourText.setLineWrap(true);
        yourText.getDocument().addDocumentListener(this);
        
        sendButton = new JButton("SEND");
        sendButton.setEnabled(false);
        sendButton.setActionCommand("SEND");
        sendButton.addActionListener(this);
        
        chatStuff = new JPanel();
        chatStuff.add(yourText);
        chatStuff.add(sendButton);
        
        add(whoIsIt, BorderLayout.NORTH);
        add(jsp, BorderLayout.CENTER);
        add(chatStuff, BorderLayout.SOUTH);
        
        setupMainFrame();
    }

    public void setupMainFrame()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(d.width/3, d.height/2);
        
        setLocation(d.width/3, d.height/3);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     
        setTitle("Chat with " + chattingWith);
        
        setVisible(true);   
    }

    public void actionPerformed(ActionEvent e) 
    {
        if (e.getActionCommand().equals("SEND"))
        {
            chat.append(user + ": " + yourText.getText() + "\n");
            cts.send("CHAT TO|" + chattingWith + "|" + user + "|" + yourText.getText() + "\n");
            yourText.setText("");
        }   
        
    }

    public void insertUpdate(DocumentEvent e) 
    {
        if(yourText.getText().trim().length() < 1)
            sendButton.setEnabled(false);
        else
            sendButton.setEnabled(true);
        
    }

    public void removeUpdate(DocumentEvent e) 
    {
        if(yourText.getText().trim().length() < 1)
            sendButton.setEnabled(false);
        else
            sendButton.setEnabled(true);
        
    }

    public void changedUpdate(DocumentEvent e) {};
}

