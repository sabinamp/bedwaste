package ch.fhnw.bedwaste;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TextView;

import org.threeten.bp.LocalTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownLabel {

    // COUNTDOWN LOGIC

    private final static int HOUR_TO_ACTIVATE_COUNTDOWNLABEL = 12;
    private final static String ENDTIME_TO_DEACTIVATE_COUNTDOWNLABEL = "24:00:00";

    Handler countdownHandler = new Handler();

    private TextView countdownTextSeconds;
    private TextView countdownTextMinutes;
    private TextView countdownTextHours;
    private TableLayout countdownBox;
    private AppCompatActivity appCompatActivity;

    private CountDownTimer countDownTimer;

    public CountdownLabel(AppCompatActivity appCompatActivity) throws ParseException {

        this.appCompatActivity = appCompatActivity;
    }

    // This Runnable Object is used to trigger the countdown method & the handlermethod calls this runnable each 5sec after APP has started.

    Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            countdownHandler.postDelayed(this, 10000);

            try {

                LocalTime localTime = LocalTime.now();
                if (localTime.getHour() >= HOUR_TO_ACTIVATE_COUNTDOWNLABEL) {
                    startCountdown();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    public void startCountdown() throws ParseException {

        LocalTime localTime = LocalTime.now();

        // Format time and calcualtion of elapsed time..
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date endTime = format.parse(ENDTIME_TO_DEACTIVATE_COUNTDOWNLABEL);
        Date deviceLocaltime = format.parse(localTime.toString());

        long timeElapsed = endTime.getTime() - deviceLocaltime.getTime();

        countdownTextSeconds = (TextView) appCompatActivity.findViewById(R.id.countdownTextSeconds);
        countdownTextMinutes = (TextView) appCompatActivity.findViewById(R.id.countdownTextMinutes);
        countdownTextHours = (TextView) appCompatActivity.findViewById(R.id.countdownTextHours);
        countdownBox = (TableLayout) appCompatActivity.findViewById(R.id.countdownBox);

        countdownBox.setAlpha(1);

        countDownTimer = new CountDownTimer(timeElapsed, 1000) {

            public void onTick(long millisUntilFinished) {

                // in Seconds
                if ((millisUntilFinished / 1000 % 60) >= 10) {
                    countdownTextSeconds.setText("" + millisUntilFinished / 1000 % 60);
                } else {
                    countdownTextSeconds.setText("0" + millisUntilFinished / 1000 % 60);
                }

                // in Minutes
                if ((millisUntilFinished / (60 * 1000) % 60) >= 10) {
                    countdownTextMinutes.setText("" + millisUntilFinished / (60 * 1000) % 60);
                } else {
                    countdownTextMinutes.setText("0" + millisUntilFinished / (60 * 1000) % 60);
                }

                // in Hours
                if ((millisUntilFinished / (60 * 60 * 1000) % 24) >= 10) {
                    countdownTextHours.setText("" + millisUntilFinished / (60 * 60 * 1000) % 24);
                } else {
                    countdownTextHours.setText("0" + millisUntilFinished / (60 * 60 * 1000) % 24);
                }
                countdownHandler.removeCallbacks(countdownRunnable);
            }

            public void onFinish() {
                countdownBox.setAlpha(0);
                countdownHandler.postDelayed(countdownRunnable, 10000);

            }
        }.start();
    }


    public TableLayout getCountdownBox() {
        return countdownBox;
    }

    public void setCountdownBox(TableLayout countdownBox) {
        this.countdownBox = countdownBox;
    }


}




