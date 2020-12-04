package com.fantasticiii.temantravelling.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Activities.HistoryActivity;
import com.fantasticiii.temantravelling.Adapter.ShowHistoryAdapter;
import com.fantasticiii.temantravelling.Model.History;
import com.fantasticiii.temantravelling.R;

import java.util.ArrayList;


public class ShowHistory extends Fragment implements ShowHistoryAdapter.ShowHistoryItemListener {

    ShowHistoryAdapter showHistoryAdapter;
    ArrayList<History> historyArrayList = new ArrayList<>();
    RecyclerView recyclerView;

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
        prepareData();

    }

    private void prepareData(){
        String[] detail = {"", ""};
        String[] date = {"20 - 21 Januari 2020", "20 - 25 Oktober 2020"};
        int[] numDay = {2,6};
        String[] location = {"Central Java, Indonesia", "East Nusa Tenggara"};
        String[] title1 = {"Explore to", "Explore to"};
        String[] title2 = {"Jogjakarta", "Labuan Bajo"};
        int[] image = {R.drawable.jogja, R.drawable.labuanbajo};

        if (historyArrayList.size() != 0 ) {
            return;
        }

        for ( int i = 0; i < detail.length; i++) {
            History history = new History();
            history.setImage(image[i]);
            history.setDetail(detail[i]);
            history.setDate(date[i]);
            history.setNumDay(numDay[i]);
            history.setLocation(location[i]);
            history.setTitle1(title1[i]);
            history.setTitle2(title2[i]);

            historyArrayList.add(history);
            Log.d("12345", "prepareData: " + history.getNumDay());
        }

        showHistoryAdapter = new ShowHistoryAdapter(getActivity(), historyArrayList, this::onShowHistoryItemClick);
        recyclerView.setAdapter(showHistoryAdapter);

        showHistoryAdapter.notifyDataSetChanged();
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
}