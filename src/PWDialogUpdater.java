
public class PWDialogUpdater implements Runnable
{
    PasswordDialog localPWDialog;
    String whatDoIDo;
    static final String CLOSE_DIALOG = "close";
    static final String USERNAME_TAKEN = "username taken";
    static final String INVALID_ID = "invalid id";
    
    public PWDialogUpdater(PasswordDialog pwd)
    {
        localPWDialog = pwd;
        whatDoIDo = "";
    }
    
    public void setAction(String action)
    {
        whatDoIDo = action;
    }
    
    public void run()
    {   
        if (whatDoIDo.equals(CLOSE_DIALOG))
            localPWDialog.dispose();

        else if (whatDoIDo.equals(USERNAME_TAKEN))
            localPWDialog.statusLabel.setText("Username already exists. Please choose a different one.");
            
        else if (whatDoIDo.equals(INVALID_ID))
            localPWDialog.statusLabel.setText("Invalid username or password. Please try again.");
    }
}
