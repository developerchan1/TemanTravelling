package com.fantasticiii.temantravelling.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Manager.FragmentChangeListener;
import com.fantasticiii.temantravelling.Model.DirectOrder;
import com.fantasticiii.temantravelling.Model.PlanOrder;
import com.fantasticiii.temantravelling.R;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class FormOrderPlan extends Fragment {

    Toolbar toolbar;
    Spinner city, language, time, payMethod;
    EditText etStartDate, etStartTime, etDurationTime;
    MaterialButton btnOrderNow;
    TextView tvPrice;
    Calendar c;
    RadioGroup rgNeedVehicle;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    int price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_order_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        city = view.findViewById(R.id.spinner_city);
        language = view.findViewById(R.id.spinner_language);
        time = view.findViewById(R.id.spinner_time);
        payMethod = view.findViewById(R.id.spinner_pay_method);
        etStartDate = view.findViewById(R.id.et_start_date);
        etStartTime = view.findViewById(R.id.et_start_time);
        etDurationTime = view.findViewById(R.id.et_duration_time);
        btnOrderNow = view.findViewById(R.id.btn_order_now);
        rgNeedVehicle = view.findViewById(R.id.rg_vechicle);
        tvPrice = view.findViewById(R.id.tv_price);
        price = 100000;

        // Get Current Date
        c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        addBackArrow();
        setSpinnerCity();
        setSpinnerLanguage();
        setSpinnerTime();
        setSpinnerPayMethod();

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

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
                    String selectedCity = city.getSelectedItem().toString();
                    String selectedLanguage = language.getSelectedItem().toString();
                    int mDuration = Integer.parseInt(etDurationTime.getText().toString());
                    String selectedTime = time.getSelectedItem().toString();
                    boolean needVehicle = rgNeedVehicle.getCheckedRadioButtonId() == R.id.rb_need_vehicle;
                    String selectedMethod = payMethod.getSelectedItem().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("order_type", "plan");
                    bundle.putParcelable("order",new PlanOrder(
                            selectedCity,
                            selectedLanguage,
                            formatDateAndTime(etStartDate.getText().toString(),etStartTime.getText().toString()),
                            mDuration,
                            selectedTime,
                            needVehicle,
                            selectedMethod,
                            price
                    ));

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

    @SuppressLint("SimpleDateFormat")
    private String formatDateAndTime(String startDate, String startTime) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(startDate+" "+startTime);
            assert date != null;
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void addBackArrow(){
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(view1 -> Objects.requireNonNull(getActivity()).onBackPressed());
    }

    private void showDateDialog(){
        DatePickerDialog dateDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etStartDate.setText(i2 + "/" + (i1 + 1) + "/" + i);
                        mYear = i;
                        mMonth = i1;
                        mDay = i2;
                    }
                },
                mYear,
                mMonth,
                mDay
        );

        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() +24*60*60*1000);
        dateDialog.updateDate(mYear,mMonth,mDay);
        dateDialog.show();
    }

    private void showTimeDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        etStartTime.setText(String.format("%02d:%02d",i,i1));
                        mHour = i;
                        mMinute = i1;
                    }
                },
                12,
                0,
                true
        );

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(mHour,mMinute);
        timePickerDialog.show();
    }

    private boolean inputsIsValid(){
        if(etStartDate.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Mohon untuk memilih tanggal pemesanan", Toast.LENGTH_LONG).show();
            return false;
        }

        if(etStartTime.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Mohon untuk memilih jam pemesanan", Toast.LENGTH_LONG).show();
            return false;
        }

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

    private void setSpinnerCity(){
        String[] cities = {"Jakarta","Surabaya","Medan","Solo","Kudus"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_list, cities);

        city.setAdapter(adapter);
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

    private void setSpinnerLanguage(){
        String[] languages = {"Bahasa Indonesia","English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_list, languages);

        language.setAdapter(adapter);
    }


}