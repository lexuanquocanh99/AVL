public class Animation extends Thread {

    public MainFrame mf;
    boolean hold = false;

    public Animation(MainFrame mf) {
        this.mf = mf;
    }

    public void waitOnPause() {
        if (this.mf.isPause()) {
            this.hold = true;
            synchronized (this) {
                try {
                    while (this.hold) {
                        wait();
                    }
                } catch (InterruptedException localInterruptedException) {

                }
            }
        }
    }
}
