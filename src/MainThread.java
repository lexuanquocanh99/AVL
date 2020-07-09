public class MainThread extends Thread {
    private String data;
    private int id;
    private MainFrame mf;

    public MainThread(MainFrame mf,int id, String data) {
        this.data = data;
        this.mf = mf;
        this.id = id;
    }

    public void run() {
        mf.performOperation(id,data);
    }
}



