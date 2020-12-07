package com.fantasticiii.temantravelling.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fantasticiii.temantravelling.R;


public class LoadingDialog extends DialogFragment {
    public LoadingDialog() {
        // Constructor kosong diperlukan untuk DialogFragment.
        // Pastikan tidak memberikan argument/parameter apapun ke
        // constructor ini.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
    }
}
