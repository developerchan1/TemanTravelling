package com.fantasticiii.temantravelling.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Fragments.FillDataDialog;
import com.fantasticiii.temantravelling.Manager.PreferencesManager;
import com.fantasticiii.temantravelling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    LinearLayout itemTourGuide, itemPlace, itemHistory, itemAccount, noOrder, runningOrder;
    TextView tvCity, tvTimeLeft, tvName;
    CardView cvRunningOrder;
    MaterialButton btnOrderNow;
    PreferencesManager preferencesManager;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTourGuide = findViewById(R.id.item_tour_guide);
        //itemPlace = findViewById(R.id.item_place);
        itemHistory = findViewById(R.id.item_history);
        itemAccount = findViewById(R.id.item_account);
        noOrder = findViewById(R.id.no_order);
        runningOrder = findViewById(R.id.running_order);
        cvRunningOrder = findViewById(R.id.cv_running_order);
        btnOrderNow = findViewById(R.id.btn_order_now);
        tvCity = findViewById(R.id.tv_city);
        tvTimeLeft = findViewById(R.id.tv_time_left);
        tvName = findViewById(R.id.tv_name);
        db = FirebaseFirestore.getInstance();
        preferencesManager = new PreferencesManager(this);

        //noinspection deprecation
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //set tvName
        tvName.setText(preferencesManager.getName());

        if(preferencesManager.getProgressId().isEmpty()){
            noOrder.setVisibility(View.VISIBLE);
            runningOrder.setVisibility(View.GONE);
        }else{
            noOrder.setVisibility(View.GONE);
            runningOrder.setVisibility(View.VISIBLE);

            getRunningOrderDataFromFirestore();
        }

        itemTourGuide.setOnClickListener((view) -> {
            if (preferencesManager.getNoWhatsapp().isEmpty()){
                showFillDataDialog();
            }else if(preferencesManager.getProgressId().isEmpty()){
                startActivity(new Intent(MainActivity.this, OrderTourGuideActivity.class));
            }else{
                startActivity(new Intent(MainActivity.this, TourGuideActivity.class));
            }
        });

        itemHistory.setOnClickListener((view) -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

        itemAccount.setOnClickListener((view -> {
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        }));

        cvRunningOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TourGuideActivity.class));
            }
        });

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferencesManager.getNoWhatsapp().isEmpty()){
                    showFillDataDialog();
                }else if(preferencesManager.getProgressId().isEmpty()){
                    startActivity(new Intent(MainActivity.this, OrderTourGuideActivity.class));
                }
            }
        });
    }

    private void getRunningOrderDataFromFirestore() {
        db.collection("onProgress").document(preferencesManager.getProgressId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }

                if (document != null && document.exists()) {
                    Log.d("Success", "DocumentSnapshot data: " + document.getData());

                    //show running order
                    noOrder.setVisibility(View.GONE);
                    runningOrder.setVisibility(View.VISIBLE);

                    tvCity.setText(document.getString("city"));
                    setTimeLeft(document.getString("dateAndTime"),
                            document.getLong("duration").intValue(),
                            document.getString("timeType"));
                } else {
                    Log.d("Error", "No such document");
                    Log.d("Error", "No such document");
                    preferencesManager.setProgressId("");
                    //show no order
                    noOrder.setVisibility(View.VISIBLE);
                    runningOrder.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setTimeLeft(String dateAndTime, int duration, String timeType) {
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAndTime);

            if(startDate.after(new Date())){
                //timer not started
                tvTimeLeft.setText("Waktu belum berjalan");
            }
            else{
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                if (timeType.equals("Jam")) {
                    c.add(Calendar.HOUR, duration);
                } else {
                    c.add(Calendar.DATE, duration);
                }
                Date endDate = c.getTime();

                if(endDate.after(new Date())){
                    //time is still running
                    long durationLeft = endDate.getTime() - (new Date().getTime());
                    new CountDownTimer(durationLeft, 1000) {
                        public void onTick(long m) {
                            long hari = TimeUnit.MILLISECONDS.toDays(m);
                            long jam = TimeUnit.MILLISECONDS.toHours(m) % 24;
                            long menit = TimeUnit.MILLISECONDS.toMinutes(m) % 60;
                            long detik = TimeUnit.MILLISECONDS.toSeconds(m) % 60;

                            tvTimeLeft.setText(
                                    String.format("%d Hari : %d Jam :  %d Menit : %d Detik", hari,jam,menit,detik)
                            );
                        }

                        public void onFinish() {
                            //times out
                            tvTimeLeft.setText("Waktu Berakhir");
                        }
                    }.start();
                }
                else{
                    //times out
                    tvTimeLeft.setText("Waktu Berakhir");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showFillDataDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FillDataDialog fillDataDialog = new FillDataDialog();
        fillDataDialog.show(fm, "fragment_fill_data");
    }

}