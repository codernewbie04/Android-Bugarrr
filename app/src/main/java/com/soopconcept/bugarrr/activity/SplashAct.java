package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.NotificationReceiver;
import com.soopconcept.bugarrr.utils.PrefManager;

import java.util.Calendar;

public class SplashAct extends AppCompatActivity {
    private Animation logo_splash;
    private ImageView app_logo;
    private Context mContext;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        mContext    =  this;
        logo_splash  = AnimationUtils.loadAnimation(mContext, R.anim.logo_animation);
        app_logo    = findViewById(R.id.applogo);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            setupNotification();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser() != null){
                    checkFirestore(mAuth.getCurrentUser());
                } else {
                    Intent goto_getstarted = new Intent(mContext, GetStartedAct.class);
                    ActivityOptionsCompat option = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(SplashAct.this, app_logo, "applogo_transition");
                    startActivity(goto_getstarted, option.toBundle());

                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finishAffinity();
                        }
                    }, 1000);
                }

            }
        }, 2000);
    }

    private void setupNotification() {
        setupNotificationSleep();
        setupNotificationWakeUp();
        setupNotificationDrink();
        setupNotificationStand();
    }

    private void setupNotificationDrink() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.setAction("DrinkNotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60, pendingIntent);
    }

    private void setupNotificationStand() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.setAction("StandNotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60, pendingIntent);
    }

    private void setupNotificationWakeUp() {
        Calendar calendar = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        PrefManager pref = new PrefManager(mContext);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long intendedTime = calendar.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.setAction("WakeUpNotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(intendedTime >= currentTime){
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else{
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = calendar.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void setupNotificationSleep() {
        Calendar calendar = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        PrefManager pref = new PrefManager(mContext);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long intendedTime = calendar.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.setAction("SleepNotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(intendedTime >= currentTime){
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else{
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = calendar.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void checkFirestore(FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("users").document(currentUser.getUid());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(!document.contains("body_measurement")){
                            startActivity(new Intent(mContext, PersonalInfoAct.class));
                            finishAffinity();
                        }  else if(!document.contains("body_measurement.plan_target")){
                            startActivity(new Intent(mContext, TargetAct.class));
                            finishAffinity();
                        } else {
                            startActivity(new Intent(mContext, HomeAct.class));
                            finishAffinity();
                        }
                    } else {
                        mAuth.signOut();
                        FancyToast.makeText(mContext,"Harap login ulang!",FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();
                        initValue();
                    }
                } else {
                    FancyToast.makeText(mContext,"No Connection!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                }
            }
        });
    }

    private void initEvent() {
        //mulai animasi
        app_logo.startAnimation(logo_splash);
    }
}