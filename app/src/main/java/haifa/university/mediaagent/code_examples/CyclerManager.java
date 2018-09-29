package haifa.university.mediaagent.code_examples;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yura on 30/12/2015.
 */
public class CyclerManager {
    private final String TAG="CyclerManager";
    private ResThread rThread = null;
    private static CyclerManager instance = null;
    private boolean initialized = false;
    private List<IResultReceiver> listeneres = new ArrayList<IResultReceiver>();
    private List<String> resultsList = new ArrayList<String>();
    private int IntervalInMinutes = 1;

    public static CyclerManager getInstance() {
        if (instance == null) {
            instance = new CyclerManager();
        }
        return instance;

    }

    public CyclerManager() {

    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.stop();
            instance = null;
        }
    }

    public void start() {
        if (!initialized) {
            rThread = new ResThread();
            rThread.start();
            initialized = true;

        }
    }

    public void stop() {
        rThread.stopThread();
        resultsList.clear();
        listeneres.clear();
    }

    public void deliverResults() {
        Log.i(TAG,"deliverResults");
        System.out.println(TAG + " onCreate");
        List<String> result = new ArrayList<String>();
        result.addAll(resultsList);
        for (IResultReceiver o : listeneres) {
            o.onRecieve(result);
        }
        resultsList.clear();
    }

    public void subscribeForResults(IResultReceiver receiver) {
        if (!listeneres.contains(receiver)) {
            listeneres.add(receiver);
        }
    }

    public boolean unsubscibeForResults(IResultReceiver receiver) {
        if (listeneres.contains(receiver)) {
            return listeneres.remove(receiver);
        }
        return false;
    }

    // thread - the core of the process timing
    public class ResThread extends Thread {

        public static final int Stop = 0;
        public static final int Play = 1;
        public static final int Pause = 2;

        int state;
        long delay = (IntervalInMinutes * 60) * 1000; // milliseconds
        private boolean mStarted;
        public boolean mRunning = true;
        Random r = new Random();

        public ResThread() {
            super("BleThread");
        }

        @Override
        public void run() {

            while (mRunning) {

                try {

                    Thread.sleep(delay);
                    String res = "result " + r.nextInt();
                    resultsList.add(res);
                    CyclerManager.getInstance().deliverResults();

                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
            mStarted = false;
        }

        public boolean isRunning() {
            return mRunning;
        }

        public void setRunning(boolean mRunning) {
            this.mRunning = mRunning;
        }

        @Override
        public synchronized void start() {
            mStarted = true;
            mRunning = true;
            super.start();
        }

        public boolean getIsStarted() {
            return mStarted;
        }

        public void stopThread() {
            state = Stop;
            mRunning = false;
        }
    }
}
