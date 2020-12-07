package com.fantasticiii.temantravelling.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Activities.TourGuideActivity;
import com.fantasticiii.temantravelling.Manager.FragmentChangeListener;
import com.fantasticiii.temantravelling.Manager.PreferencesManager;
import com.fantasticiii.temantravelling.Model.DirectOrder;
import com.fantasticiii.temantravelling.Model.PlanOrder;
import com.fantasticiii.temantravelling.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoadingScreen extends Fragment {

    private FirebaseFirestore db;
    FusedLocationProviderClient fusedLocationProviderClient;
    private double latitude, longitude;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        assert bundle != null;
        if (bundle.getString("order_type").equals("direct")) {
            //check location permission
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                //when permission is not granted
                //request permission
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        },
                        100);
            }
        } else {
            //search by city
            planOrder();
        }
    }

    private void planOrder() {
        PlanOrder planOrder = bundle.getParcelable("order");
        String city = planOrder.getCity();
        String language = planOrder.getLanguage();
        String dateAndTime = planOrder.getStartDateAndTime();
        int duration = planOrder.getDuration();
        String timeType = planOrder.getTimeType();
        boolean needVehicle = planOrder.isNeedVehicle();
        String paymentMethod = planOrder.getPaymentMethod();
        int price = planOrder.getPrice();

        db.collection("onWaiting")
                .whereEqualTo("city", city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //data will be just one or empty
                            if (Objects.requireNonNull(task.getResult()).getDocuments().size() > 0) {
                                DocumentSnapshot document = Objects.requireNonNull(task.getResult()).getDocuments().get(0);
                                String partnerUID = document.getId();
                                proccessNewOrder(partnerUID, city,
                                        language,
                                        dateAndTime,
                                        duration,
                                        timeType,
                                        needVehicle,
                                        paymentMethod,
                                        price);
                            } else {
                                Toast.makeText(getContext(), "Tour guide tidak ditemukan. Tunggu beberapa saat dan silakan coba kembali.", Toast.LENGTH_LONG).show();
                                assert getFragmentManager() != null;
                                getFragmentManager().popBackStack();
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "Ditemukan masalah ketika mencoba mencari tour guide. Pastikan koneksi anda lancar dan silahkan coba kembali.", Toast.LENGTH_LONG).show();
                            assert getFragmentManager() != null;
                            getFragmentManager().popBackStack();
                        }
                    }
                });
    }

    private void directOrder() {
        DirectOrder directOrder = bundle.getParcelable("order");
        String language = directOrder.getLanguage();
        String dateAndTime = directOrder.getDateAndTime();
        int duration = directOrder.getDuration();
        String timeType = directOrder.getTimeType();
        boolean needVehicle = directOrder.isNeedVehicle();
        String paymentMethod = directOrder.getPaymentMethod();
        int price = directOrder.getPrice();

        //get all data in onWaiting collection
        db.collection("onWaiting")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isFound = false;
                            //count distance between each tour guide location and customer location
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                double tourGuideLat = document.getDouble("latitude");
                                double tourGuideLng = document.getDouble("longitude");
                                if (euclideanDistance(latitude,longitude,tourGuideLat,tourGuideLng) <= 10){
                                    isFound = true;
                                    String partnerUID = document.getId();
                                    String city = document.getString("city");
                                    proccessNewOrder(partnerUID,
                                            city,
                                            language,
                                            dateAndTime,
                                            duration,
                                            timeType,
                                            needVehicle,
                                            paymentMethod,
                                            price);
                                    break;
                                }
                            }

                            if(!isFound){
                                Toast.makeText(getContext(), "Tour guide tidak ditemukan. Tunggu beberapa saat dan silakan coba kembali.", Toast.LENGTH_LONG).show();
                                assert getFragmentManager() != null;
                                getFragmentManager().popBackStack();
                            }

                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "Ditemukan masalah ketika mencoba mencari tour guide. Pastikan koneksi anda lancar dan silahkan coba kembali.", Toast.LENGTH_LONG).show();
                            assert getFragmentManager() != null;
                            getFragmentManager().popBackStack();
                        }
                    }
                });
    }

    private void proccessNewOrder(String partnerUID,
                                      String city,
                                      String language,
                                      String dateAndTime,
                                      int duration,
                                      String timeType,
                                      boolean needVehicle,
                                      String paymentMethod,
                                      int price) {

        Map<String, Object> newOrder = new HashMap<>();
        newOrder.put("userUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        newOrder.put("partnerUID", partnerUID);
        newOrder.put("city", city);
        newOrder.put("language", language);
        newOrder.put("dateAndTime", dateAndTime);
        newOrder.put("duration", duration);
        newOrder.put("timeType", timeType);
        newOrder.put("needVehicle", needVehicle);
        newOrder.put("paymentMethod", paymentMethod);
        newOrder.put("price", price);

        DocumentReference onProgressRef = db.collection("onProgress").document();
        onProgressRef
                .set(newOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //delete partner from onWaiting collection
                        removePartnerFromWaitingList(partnerUID, onProgressRef.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error writing document", e);
                    }
                });
    }


    private void removePartnerFromWaitingList(String partnerUID, String progressId) {
        db.collection("onWaiting").document(partnerUID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully deleted!");
                        //save progressId
                        new PreferencesManager(getContext()).setProgressId(progressId);

                        //move to Tour Guide Activity
                        //send progress id to Tour Guide Activity
                        Intent i = new Intent(getActivity(), TourGuideActivity.class);
                        startActivity(i);
                        Objects.requireNonNull(getActivity()).finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error deleting document", e);
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //initialize location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(
                Context.LOCATION_SERVICE
        );

        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    if (location != null) {
                        //when location result is not null
                        //set latitude
                        latitude = location.getLatitude();
                        //set longitude
                        longitude = location.getLongitude();

                        //search using euclidean algorithm
                        directOrder();
                    } else {
                        //when location is null
                        //initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                //initialize location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                latitude = location1.getLatitude();
                                //set longitude
                                longitude = location1.getLongitude();

                                //search using euclidean algorithm
                                directOrder();
                            }
                        };
                        //request location update
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                }
            });

        }else{
            //when location service is not enable
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private double euclideanDistance(double lat1, double lng1, double lat2, double lng2) {
        double latDifference = lat2 - lat1;
        double lngDifference = lng2 - lng1;

        return Math.sqrt(latDifference * latDifference + lngDifference * lngDifference) * 111.319;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100
                && grantResults.length > 0
                && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }
        else{
            ///permision denied
            Toast.makeText(getContext(), "Aplikasi memerlukan izin lokasi untuk dapat memulai proses pencarian Tour Guide. Berikan izin lokasi kepada aplikasi dan silahkan coba kembali.", Toast.LENGTH_LONG).show();
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        }
    }
}