package llabs.vcreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.speech.tts.TextToSpeech;
import java.util.Locale;


import java.util.Calendar;

public class AlarmActivity extends Activity {

    AlarmManager alarmManager;
    private static PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView;
    TextToSpeech tts;
    boolean done = false;
    String alarmTitle;

    public static Ringtone ringtone;


    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 12314314, myIntent, 0);

        tts = new TextToSpeech(AlarmActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                    done = true;
                }
            }
        });

    }

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {

            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            //Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            //pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        } else {

            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            PendingIntent newPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 12314314, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(newPendingIntent);

            ringtone.stop();

            try {
                Thread.sleep(1500);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            TextView alarmTitleView = (TextView) findViewById(R.id.alarmTitle);
            alarmTitle = alarmTitleView.getText().toString();
            parlaMiguel(alarmTitle);
            setAlarmText("");
        }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    public void parlaMiguel(String text_to_speak) {
        if (done)
            tts.speak(text_to_speak, TextToSpeech.QUEUE_FLUSH, null, null);
        else {
            Log.d("voice","not done");
        }
    }


}

