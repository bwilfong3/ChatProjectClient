import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class ChatGUIUpdater implements Runnable
{
    ChatGUI localGUI;
    static final String NONEXISTENT_USERNAME = "username does not exist";
    static final String BRQ_RECEIVED = "buddy request received";
    static final String BRQ_SENT = "Buddy request sent";
    static final String BUDDY_ACCEPTED = "Buddy accepted";
    static final String CHAT_REQUEST = "Recieved chat request";
    static final String CHAT_ACCEPTED = "chat accepted";
    static final String CHAT_DENIED = "chat denied";
    static final String MESSAGE_RECEIVED = "message received";
    String whatDoIDo;
    String buddy;
    String message;
    
    public ChatGUIUpdater(ChatGUI cgu)
    {
        localGUI = cgu;
        whatDoIDo = "";
        buddy = "";
        message = "";
                
    }
    
    public void setAction(String cmd)
    {
        whatDoIDo = cmd;
    }
    
    public void setBuddy(String b)
    {
        buddy = "";
        buddy = b;
    }
    
    public void setMessage(String m)
    {
        message = "";
        message = m;
    }
    
    public void run()
    {   
        if (whatDoIDo.equals(NONEXISTENT_USERNAME))
            localGUI.badUsername();
        
        else if (whatDoIDo.equals(BRQ_RECEIVED))
            localGUI.buddyRequestReceived(buddy);
        
        else if (whatDoIDo.equals(BUDDY_ACCEPTED))
            localGUI.addBuddy(buddy);
        
        else if (whatDoIDo.equals(CHAT_REQUEST))
            localGUI.displayChatRequest(buddy);
        
        else if (whatDoIDo.equals(CHAT_ACCEPTED))
            localGUI.chatAccepted(buddy);
        
        else if (whatDoIDo.equals(MESSAGE_RECEIVED))
            localGUI.displayMessage(buddy, message);
    }
}
