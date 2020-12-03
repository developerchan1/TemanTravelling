package com.fantasticiii.temantravelling.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantasticiii.temantravelling.Manager.FragmentChangeListener;
import com.fantasticiii.temantravelling.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ChooseOrderType extends Fragment {

    Toolbar toolbar;
    MaterialButton btnOrderPlan, btnOrderDirect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_order_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        btnOrderPlan = view.findViewById(R.id.btn_order_plan);
        btnOrderDirect = view.findViewById(R.id.btn_order_direct);

        addBackArrow();

        btnOrderPlan.setOnClickListener((btnView) -> {
            FragmentChangeListener fc = (FragmentChangeListener) getActivity();
            assert fc != null;
            fc.replaceFragment(new FormOrderPlan());
        });

        btnOrderDirect.setOnClickListener((btnView) -> {
            FragmentChangeListener fc = (FragmentChangeListener) getActivity();
            assert fc != null;
            fc.replaceFragment(new FormOrderDirect());
        });
    }

    private void addBackArrow(){
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(view1 -> Objects.requireNonNull(getActivity()).onBackPressed());
    }
}