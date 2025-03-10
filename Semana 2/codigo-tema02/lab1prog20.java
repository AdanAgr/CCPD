class FinTrans{
    public static String transName;
    public static double amount;
}
class TransThread extends Thread {
    private FinTrans ft;
    
    TransThread (FinTrans ft, String name)  {
        super (name); // Save thread's name
        this.ft = ft; // Save reference to financial transaction object
    }
    
    public void run () {
        for (int i = 0; i < 100; i++) {
           
            /*   ADD CODE:   
            *       Write the control statements and 
            *       synchronized blocks to protect the 
            *       critical sections of the Deposit and 
            *       Withdrawal threads.
            * 
            */
        }
    }
}

class lab1prob20 {
    public static void main (String [] args)  {
        FinTrans ft = new FinTrans ();
        TransThread tt1 = new TransThread (ft, "Deposit");
        TransThread tt2 = new TransThread (ft, "Withdrawal");
        tt1.start ();
        tt2.start ();
    }
 }
