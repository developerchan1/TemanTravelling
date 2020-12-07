package com.fantasticiii.temantravelling.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Manager.FragmentChangeListener;
import com.fantasticiii.temantravelling.Model.DirectOrder;
import com.fantasticiii.temantravelling.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class FormOrderDirect extends Fragment {

    Toolbar toolbar;
    Spinner language, time, payMethod;
    EditText etDurationTime;
    RadioGroup rgNeedVehicle;
    TextView tvPrice;
    MaterialButton btnOrderNow;
    int price = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_order_direct, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        language = view.findViewById(R.id.spinner_language);
        time = view.findViewById(R.id.spinner_time);
        payMethod = view.findViewById(R.id.spinner_pay_method);
        etDurationTime = view.findViewById(R.id.et_duration_time);
        btnOrderNow = view.findViewById(R.id.btn_order_now);
        rgNeedVehicle = view.findViewById(R.id.rg_vechicle);
        tvPrice = view.findViewById(R.id.tv_price);
        price = 100000;

        addBackArrow();
        setSpinnerLanguage();
        setSpinnerTime();
        setSpinnerPayMethod();

        etDurationTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setPrice();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputsIsValid()){
                    String selectedLanguage = language.getSelectedItem().toString();
                    int mDuration = Integer.parseInt(etDurationTime.getText().toString());
                    String selectedTime = time.getSelectedItem().toString();
                    boolean needVehicle = rgNeedVehicle.getCheckedRadioButtonId() == R.id.rb_need_vehicle;
                    String selectedMethod = payMethod.getSelectedItem().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("order_type", "direct");
                    bundle.putParcelable("order",new DirectOrder(selectedLanguage,
                            getCurrentDateAndTime(),
                            mDuration,
                            selectedTime,
                            needVehicle,
                            selectedMethod,
                            price));

                    LoadingScreen loadingScreen = new LoadingScreen();
                    loadingScreen.setArguments(bundle);
                    FragmentChangeListener fc = (FragmentChangeListener) getActivity();
                    assert fc != null;
                    fc.replaceFragment(loadingScreen);
                }
            }
        });
    }

    private void setPrice() {
        if(etDurationTime.getText().toString().isEmpty()){
            tvPrice.setText("Rp -");
            price = 0;
        }
        else{
            if(time.getSelectedItem().toString().equals("Jam")){
                price = Integer.parseInt(etDurationTime.getText().toString()) * 75000;
            }
            else{
                price = Integer.parseInt(etDurationTime.getText().toString()) * 500000;
            }
            tvPrice.setText("Rp "+price);
        }
    }

    private void addBackArrow(){
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(view1 -> Objects.requireNonNull(getActivity()).onBackPressed());
    }

    private boolean inputsIsValid(){
        if(etDurationTime.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Mohon untuk mengisi durasi pemesanan", Toast.LENGTH_LONG).show();
            return false;
        }

        if(Integer.parseInt(etDurationTime.getText().toString()) < 1 || Integer.parseInt(etDurationTime.getText().toString()) > 12){
            Toast.makeText(getContext(), "Durasi pemesanan tidak boleh lebih dari 12 jam atau 12 hari", Toast.LENGTH_LONG).show();
            return false;
        }

        if(rgNeedVehicle.getCheckedRadioButtonId() == -1){
            Toast.makeText(getContext(), "Mohon untuk memilih apakah anda memerlukan kendaraan atau tidak", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void setSpinnerTime(){
        String[] times = {"Jam","Hari"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_list, times);

        time.setAdapter(adapter);

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerPayMethod(){
        String[] payMethods = {"Cash"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_list, payMethods);

        payMethod.setAdapter(adapter);
    }

    private void setSpinnerLanguage() {
        String[] languages = {"Bahasa Indonesia", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_list, languages);

        language.setAdapter(adapter);
    }

    private String getCurrentDateAndTime(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}