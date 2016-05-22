


public class BuddyDialogUpdater implements Runnable
{
    AddBuddyDialog abd;
    String whatDoIDo;
    String statusLabel;
    
    static final String NONEXISTENT_USERNAME = "username does not exist";
    static final String BRQ_SENT = "Buddy request sent";
    
    public BuddyDialogUpdater(AddBuddyDialog abd)
    {
        this.abd = abd;
        statusLabel = "";
        whatDoIDo = "";
    }
    
    public void setAction(String cmd)
    {
        whatDoIDo = cmd;
    }
    
    public void run()
    {
        if (whatDoIDo.equals(NONEXISTENT_USERNAME))
        {
            abd.buddyMessage.setText(statusLabel);

        }
    
        else if (whatDoIDo.equals(BRQ_SENT))
        {
            abd.dispose();
        }
    }
}
