package llabs.vcreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView;
    TextToSpeech tts;
    boolean done = false;
    String alarmTitle;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

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
            Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        } else {

            AlarmManager alarmManager = (AlarmManager) mContext
                    .getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(PROX_ALERT_INTENT);
            intent.putExtra("ALERT_TIME", alert.date);
            intent.putExtra("ID_ALERT", alert.idAlert);
            intent.putExtra("TITLE", alert.title);
            intent.putExtra("GEO_LOC", alert.isGeoLoc);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                    alert.idAlert, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.cancel(pendingIntent);

            TextView alarmTitleView = (TextView) findViewById(R.id.alarmTitle);
            alarmTitle = alarmTitleView.getText().toString();
            Speak_Text(alarmTitle);
            setAlarmText("");
            Log.d("MyActivity", "Alarm Off");
        }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    public void Speak_Text(String text_to_speak) {
        if (done)
            tts.speak(text_to_speak, TextToSpeech.QUEUE_FLUSH, null, null);
        else {
            Log.d("voice","not done");
        }
    }
}

