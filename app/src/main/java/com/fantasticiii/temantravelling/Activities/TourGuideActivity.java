package com.fantasticiii.temantravelling.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Fragments.ConfirmCancelationDialog;
import com.fantasticiii.temantravelling.Fragments.ConfirmDoneDialog;
import com.fantasticiii.temantravelling.Fragments.PaymentConfirmationDialog;
import com.fantasticiii.temantravelling.Manager.PreferencesManager;
import com.fantasticiii.temantravelling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TourGuideActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseFirestore db;
    String progressId, partnerUID, city, language, dateAndTime, timeType, paymentMethod, partnerNoWA, countyCode;
    int duration;
    long price;
    boolean needVehicle;
    TextView tvName, tvPrice, tvPaymentMethod, tvCity, tvLeftTime, tvRedirectGoogleMaps;
    MaterialButton btnCancel, btnComplete;
    ImageView ivWhatsapp;
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide);

        toolbar = findViewById(R.id.toolbar);
        tvName = findViewById(R.id.tv_name);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvCity = findViewById(R.id.tv_city);
        tvLeftTime = findViewById(R.id.tv_left_time);
        tvPrice = findViewById(R.id.tv_price);
        ivWhatsapp = findViewById(R.id.iv_whatsapp);
        tvRedirectGoogleMaps = findViewById(R.id.tv_redirect_google_maps);
        btnCancel = findViewById(R.id.btn_cancel);
        btnComplete = findViewById(R.id.btn_complete);
        db = FirebaseFirestore.getInstance();
        preferencesManager = new PreferencesManager(this);

        addBackArrow();

        //noinspection deprecation
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        progressId = preferencesManager.getProgressId();
        //progressId = "MhxBFFzkgPGnfWzGk1nS";
        getDataFromProgressId();

        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appInstalled("com.whatsapp")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+countyCode+partnerNoWA));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(TourGuideActivity.this, "WhatsApp tidak terinstall di perangkat anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRedirectGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appInstalled("com.google.android.apps.maps")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.google.com/maps/search/?api=1&query="+tvCity.getText().toString()));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(TourGuideActivity.this, "Google Maps tidak terinstall di perangkat anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCancelation();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDone();
            }
        });
    }

    private boolean appInstalled(String url) {
        PackageManager packageManager =  getPackageManager();

        try{
            packageManager.getPackageInfo(url,packageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getDataFromProgressId() {

        final DocumentReference docRef = db.collection("onProgress").document(progressId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Success", "DocumentSnapshot data: " + snapshot.getData());

                    partnerUID = snapshot.getString("partnerUID");
                    city = snapshot.getString("city");
                    language = snapshot.getString("language");
                    dateAndTime = snapshot.getString("dateAndTime");
                    duration = snapshot.getLong("duration").intValue();
                    timeType =  snapshot.getString("timeType");
                    needVehicle =  snapshot.getBoolean("needVehicle");
                    paymentMethod = snapshot.getString("paymentMethod");
                    price = snapshot.getLong("price");

                    tvCity.setText(city);
                    tvPrice.setText("Rp "+price);
                    tvPaymentMethod.setText(paymentMethod);
                    setTimeLeft();

                    getPartnerData();

                    if(snapshot.getBoolean("paymentConfirmation") != null && snapshot.getBoolean("paymentConfirmation")){
                        showDialogPaymentConfirmation();
                    }
                } else {
                    Log.d("Error", "Current data: null");
                    preferencesManager.setProgressId("");
                    finish();
                }
            }
        });
    }

    private void getPartnerData() {
        db.collection("partners").document(partnerUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                Log.d("Success", "DocumentSnapshot data: " + document.getData());
                                tvName.setText(document.getString("nama"));
                                partnerNoWA = document.getString("noWhatsapp");
                                countyCode = document.getString("kodeNegara");
                            } else {
                                Log.d("Error", "No such document");
                            }
                        } else {
                            Log.d("Error", "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void setTimeLeft() {
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAndTime);

            if(startDate.after(new Date())){
                //timer not started
                tvLeftTime.setText("Waktu belum berjalan");
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

                            tvLeftTime.setText(
                                    String.format("%d Hari : %d Jam :  %d Menit : %d Detik", hari,jam,menit,detik)
                            );
                        }

                        public void onFinish() {
                            //times out
                            tvLeftTime.setText("Waktu Berakhir");
                        }
                    }.start();
                }
                else{
                    //times out
                    tvLeftTime.setText("Waktu Berakhir");
                    orderCompleted();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showDialogCancelation() {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmCancelationDialog confirmCancelationDialog = new ConfirmCancelationDialog();
        confirmCancelationDialog.show(fm, "fragment_confirm_cancelation");
    }

    private void showDialogDone() {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmDoneDialog confirmDoneDialog = new ConfirmDoneDialog();
        confirmDoneDialog.show(fm, "fragment_confirm_done");
    }

    private void showDialogPaymentConfirmation() {
        FragmentManager fm = getSupportFragmentManager();
        PaymentConfirmationDialog paymentConfirmationDialog = new PaymentConfirmationDialog();
        paymentConfirmationDialog.show(fm, "fragment_payment_confirmation");
    }

    public void cancelThisOrder(){
        //save this progress to history collection
        saveProgressToHistory();
    }

    private void saveProgressToHistory() {
        Map<String, Object> history = new HashMap<>();
        history.put("userUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        history.put("partnerUID", partnerUID);
        history.put("city", city);
        history.put("language", language);
        history.put("dateAndTime", dateAndTime);
        history.put("duration", duration);
        history.put("timeType", timeType);
        history.put("needVehicle", needVehicle);
        history.put("paymentMethod", paymentMethod);
        history.put("price", price);
        history.put("status","Dibatalkan oleh user");
        history.put("historyTimestamp",new Timestamp(new Date()));

        db.collection("history").document(progressId)
                .set(history)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully written!");
                        //delete this progress from onProgress collection
                        removeProgress();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error writing document", e);
                    }
                });
    }

    private void removeProgress() {
        db.collection("onProgress").document(progressId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TourGuideActivity.this, "Pemesanan telah berhasil dibatalkan", Toast.LENGTH_LONG).show();
                        Log.d("Success", "DocumentSnapshot successfully deleted!");
                        //reset progress id in shared preferences
                        preferencesManager.setProgressId("");
                        //back to Main Activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error deleting document", e);
                    }
                });
    }

    public void orderCompleted(){
        Map<String, Object> data = new HashMap<>();
        data.put("paymentConfirmation",true);

        db.collection("onProgress")
                .document(preferencesManager.getProgressId())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating document", e);
                    }
                });
    }


    private void addBackArrow(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}