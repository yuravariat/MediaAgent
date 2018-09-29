package haifa.university.mediaagent.code_examples;

import java.util.Timer;
import java.util.TimerTask;

import haifa.university.mediaagent.common.AppLogger;

/**
 * Created by yura on 14/06/2016.
 */
public class TimerStopWatch {
    private final String TAG="InfoMediaAgentService";
    private Timer timer;
    private TimerTask timerTask;
    private int counter=0;
    private long oldTime=0;

    public TimerStopWatch(){}

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 5 second
        timer.schedule(timerTask, 5000, 5000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                AppLogger.getInstance().writeLog(TAG, "in timer ++++  "+ (counter++), AppLogger.LogLevel.TRACE);
            }
        };
    }
}
