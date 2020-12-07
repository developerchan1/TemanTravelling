package com.fantasticiii.temantravelling.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Activities.HistoryActivity;
import com.fantasticiii.temantravelling.Adapter.ShowHistoryAdapter;
import com.fantasticiii.temantravelling.Model.History;
import com.fantasticiii.temantravelling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class ShowHistory extends Fragment implements ShowHistoryAdapter.ShowHistoryItemListener {

    ShowHistoryAdapter showHistoryAdapter;
    ArrayList<History> historyArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayout empty;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_history, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);

        toolbar = view.findViewById(R.id.toolbar);
        empty = view.findViewById(R.id.empty);
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog();

        addBackArrow();
        prepareData();

    }

    private void prepareData(){
        showDialog();
        db.collection("history")
                .orderBy("historyTimestamp", Query.Direction.DESCENDING)
                .whereEqualTo("userUID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                String partnerUID = document.getString("partnerUID");
                                String city = document.getString("city");
                                String language = document.getString("language");
                                String dateAndTime = document.getString("dateAndTime");
                                int duration = document.getLong("duration").intValue();
                                String timeType = document.getString("timeType");
                                boolean needVehicle =  document.getBoolean("needVehicle");
                                String paymentMethod = document.getString("paymentMethod");
                                long price = document.getLong("price");
                                String status = document.getString("status");

                                historyArrayList.add(new History(partnerUID,city,language,dateAndTime,duration,timeType,needVehicle,paymentMethod,price,status));
                            }

                            if(historyArrayList.isEmpty()){
                                //show empty layout
                                empty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else{
                                //show history layout
                                empty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                showHistoryAdapter = new ShowHistoryAdapter(getActivity(), historyArrayList, ShowHistory.this::onShowHistoryItemClick);
                                recyclerView.setAdapter(showHistoryAdapter);
                                showHistoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                            //show empty layout
                            empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        loadingDialog.dismiss();
                    }
                });
    }


    public void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewShowHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onShowHistoryItemClick(int position) {
        History history = historyArrayList.get(position);
      //  Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        loadingDialog.show(fm, "fragment_loading_dialog");
    }

    private void addBackArrow(){
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(view1 -> Objects.requireNonNull(getActivity()).onBackPressed());
    }

}