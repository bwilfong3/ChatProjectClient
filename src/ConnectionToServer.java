import java.io.IOException;
import java.net.SocketException;

import javax.swing.SwingUtilities;

public class ConnectionToServer implements Runnable
{
    Talker communicator;
    String labelMessage;
    Thread ctsThread;
    PWDialogUpdater pwdu;
    ChatGUIUpdater cgu;
    
    ConnectionToServer(Talker t, PWDialogUpdater pwdu)
    {
        communicator = t;
        this.pwdu = pwdu;
        ctsThread = new Thread(this);
        ctsThread.start();            // Begin communication with server
    }
    
    public void run()
    {
        String message;
        
        try{
        while(true)
        {
            
            message = this.receive();
            
            if (message.startsWith("SUCCESSFUL REGISTRATION") ||
                message.startsWith("SUCCESSFUL LOGIN"))
            {
                String messageContents[] = message.split("[\\x7C]");
                
                pwdu.setAction(PWDialogUpdater.CLOSE_DIALOG);
                SwingUtilities.invokeLater(pwdu);
                cgu = new ChatGUIUpdater(new ChatGUI(this, messageContents[1]));
            }
            
            else if (message.startsWith("USERNAME TAKEN"))
            {
                pwdu.setAction(PWDialogUpdater.USERNAME_TAKEN);
                SwingUtilities.invokeLater(pwdu);
            }
            
            else if (message.startsWith("INVALID ID"))
            {
                pwdu.setAction(PWDialogUpdater.INVALID_ID);
                SwingUtilities.invokeLater(pwdu);
            }
            
            else if (message.startsWith("BRQ FAILED USER DOES NOT EXIST"))
            {
                cgu.setAction(ChatGUIUpdater.NONEXISTENT_USERNAME);
                SwingUtilities.invokeLater(cgu);
            }
            
            else if (message.startsWith("BUDDY REQUEST FROM|"))
            {
                processBuddyRequest(message, cgu);
            }
            
            else if (message.startsWith("BUDDY ACCEPTED"))
            {
                addBuddy(message, cgu);
            }
            
            else if (message.startsWith("CHAT REQUEST FROM"))
            {
                requestForChat(message, cgu);
            }
            
            else if (message.startsWith("CHAT ACCEPTED"))
            {
                chatAccepted(message, cgu);
            }
            
            else if (message.startsWith("CHAT DENIED"))
            {
                chatDenied(message, cgu);
            }
            
            else if (message.startsWith("CHAT FROM"))
            {
                displayMessage(message, cgu);
            }
                                        
        }
        }
        catch(Exception SocketException)
        {
            System.out.println("Connection with server terminated.");
        }
    }
    
    public void send(String message)
    {
        try 
        {
            communicator.send(message);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public String receive() throws SocketException
    {
        String message = null;
        try 
        {
            message = communicator.receive();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return message;
    }
    
    public void processBuddyRequest(String msg, ChatGUIUpdater cgu)
    {
        String[] messageContents = msg.split("[\\x7c]");
        cgu.setAction(ChatGUIUpdater.BRQ_RECEIVED);
        cgu.setBuddy(messageContents[1]);
        SwingUtilities.invokeLater(cgu);
    }
    
    public void addBuddy(String msg, ChatGUIUpdater cgu)
    {
        String[] messageContents = msg.split("[\\x7c]");
        cgu.setBuddy(messageContents[1]);
        cgu.setAction(ChatGUIUpdater.BUDDY_ACCEPTED);
        SwingUtilities.invokeLater(cgu);
    }
    
    public void requestForChat(String msg, ChatGUIUpdater cgu)
    {
        String[] messageContents = msg.split("[\\x7c]");
        cgu.setBuddy(messageContents[1]);
        cgu.setAction(ChatGUIUpdater.CHAT_REQUEST);
        SwingUtilities.invokeLater(cgu);
    }
    
    public void chatAccepted(String msg, ChatGUIUpdater cgu)
    {
        String[] messageContents = msg.split("[\\x7c]");
        cgu.setBuddy(messageContents[1]);
        cgu.setAction(ChatGUIUpdater.CHAT_ACCEPTED);
        SwingUtilities.invokeLater(cgu);
    }
    
    public void chatDenied(String msg, ChatGUIUpdater cgu)
    {
        String[] messageContents = msg.split("[\\x7c]");
        cgu.setBuddy(messageContents[1]);
        cgu.setAction(ChatGUIUpdater.CHAT_DENIED);
        SwingUtilities.invokeLater(cgu);
    }
    
    public void displayMessage(String msg, ChatGUIUpdater cgu)
    {
        String[] messageContents = msg.split("[\\x7c]");
        cgu.setBuddy(messageContents[1]); // source comes first
        cgu.setMessage(messageContents[2]); // destination comes next
        cgu.setAction(ChatGUIUpdater.MESSAGE_RECEIVED);
        SwingUtilities.invokeLater(cgu);
    }
}
