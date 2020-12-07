package com.fantasticiii.temantravelling.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fantasticiii.temantravelling.Activities.TourGuideActivity;
import com.fantasticiii.temantravelling.R;
import com.google.android.material.button.MaterialButton;

public class PaymentConfirmationDialog extends DialogFragment {
    private MaterialButton btnTidak;
    private TextView tvYa;

    public PaymentConfirmationDialog() {
        // Constructor kosong diperlukan untuk DialogFragment.
        // Pastikan tidak memberikan argument/parameter apapun ke
        // constructor ini.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_confirmation, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCancelable(false);
    }
}
