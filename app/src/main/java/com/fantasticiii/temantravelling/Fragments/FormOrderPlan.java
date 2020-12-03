package com.fantasticiii.temantravelling.Fragments;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fantasticiii.temantravelling.R;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class FormOrderPlan extends Fragment {

    Toolbar toolbar;
    Spinner city, language, time, payMethod;
    EditText etStartDate, etStartTime, etDurationTime;
    Calendar c;
    int mYear, mMonth, mDay;
    int mHour, mMinute;

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

        // Get Current Date
        Calendar c = Calendar.getInstance();
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

        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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

    private void setSpinnerCity(){
        String[] cities = {"Jakarta","Surabaya","Medan","Solo","Kudus"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_list, cities);

        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerTime(){
        String[] times = {"Jam","Hari"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_list, times);

        time.setAdapter(adapter);

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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

        payMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerLanguage(){
        String[] languages = {"Bahasa Indonesia","English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_list, languages);

        language.setAdapter(adapter);

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}