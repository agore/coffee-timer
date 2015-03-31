package org.bitxbit.coffeetimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CoffeeTimerActivity extends ActionBarActivity {
    private static final String TAG = CoffeeTimerActivity.class.getSimpleName();
    @InjectView(R.id.img_indicator)
    ImageView imgIndicator;
    @InjectView(R.id.txt_phase)
    TextView txtPhase;
    @InjectView(R.id.txt_timer)
    TextView txtTimer;

    private int secondsElapsed;
    private boolean timerRunning;
    private TimeUpdateRunnable runnable;
    private ToneGenerator tg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_timer);
        ButterKnife.inject(this);
        runnable = new TimeUpdateRunnable(this);
        tg = new ToneGenerator(AudioManager.STREAM_DTMF, 100);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onStart() {
        super.onStart();
        imgIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    timerRunning = false;
                    pause();
                } else {
                    timerRunning = true;
                    imgIndicator.post(runnable);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        tg.release();
    }

    private void reset() {
        imgIndicator.removeCallbacks(runnable);
        imgIndicator.setImageResource(R.drawable.initial_circle);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        txtTimer.setText("0:00");
    }

    private void pause() {
        imgIndicator.removeCallbacks(runnable);
    }

    private String format(int secondsElapsed) {
        int m = secondsElapsed / 60;
        int s = secondsElapsed % 60;
        return (s < 10) ? (m + ":0" + s) : (m + ":" + s);
    }

    private void updateTime() {
        secondsElapsed++;
        txtTimer.setText(format(secondsElapsed));

        if (secondsElapsed == 1) {
            imgIndicator.setImageResource(R.drawable.bloom_circle);
            txtPhase.setText("Bloom");
        }
        if (secondsElapsed > 27 && secondsElapsed < 31) {
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
        if (secondsElapsed == 31) {
            imgIndicator.setImageResource(R.drawable.brew_circle);
            txtPhase.setText("Pour");
        }

        if (secondsElapsed > 117 && secondsElapsed < 121) {
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
        if (secondsElapsed == 121) {
            imgIndicator.setImageResource(R.drawable.wait_circle);
            txtPhase.setText("Stand");
        }

        if (secondsElapsed > 222 && secondsElapsed <= 225) {
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        }

        if (secondsElapsed == 225) {
            imgIndicator.removeCallbacks(runnable);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            txtPhase.setText("Done");
            imgIndicator.setImageResource(R.drawable.done_circle);
            return;
        }

        imgIndicator.postDelayed(runnable, 1000);
    }

    private static class TimeUpdateRunnable implements Runnable {
        private final WeakReference<CoffeeTimerActivity> ref;

        private TimeUpdateRunnable(CoffeeTimerActivity act) {
            ref = new WeakReference<CoffeeTimerActivity>(act);
        }

        @Override
        public void run() {
            CoffeeTimerActivity act = ref.get();
            if (act != null) act.updateTime();
        }
    }
}
