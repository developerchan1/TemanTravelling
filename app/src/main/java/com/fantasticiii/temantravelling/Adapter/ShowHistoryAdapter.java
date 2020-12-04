package com.fantasticiii.temantravelling.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantasticiii.temantravelling.Fragments.ShowHistory;
import com.fantasticiii.temantravelling.Model.History;
import com.fantasticiii.temantravelling.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowHistoryAdapter extends RecyclerView.Adapter<ShowHistoryAdapter.ShowHistoryViewHolder> {

    Context mContext;
    ArrayList<History> historyArrayList;
    ShowHistoryItemListener showHistoryItemListener;

    public ShowHistoryAdapter(Context context, ArrayList<History> histories, ShowHistoryItemListener showHistoryItemListener){
        this.historyArrayList = histories;
        this.mContext = context;
        this.showHistoryItemListener = showHistoryItemListener;
    }


    @NonNull
    @Override
    public ShowHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_single_item,parent, false);
        return new ShowHistoryViewHolder(view, showHistoryItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHistoryViewHolder holder, int position) {
        History history = historyArrayList.get(position);

        holder.numDays.setText(history.getNumDay() + " Days");
        holder.detail.setText(history.getDetail());
        holder.location.setText(history.getLocation());
        holder.imageView.setImageResource(history.getImage());
        holder.date.setText(history.getDate());
        holder.txtTitle1.setText(history.getTitle1());
        holder.txtTitle2.setText(history.getTitle2());

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class ShowHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTitle1,txtTitle2, location, numDays, date, detail;
        public ShowHistoryViewHolder(@NonNull View itemView, ShowHistoryItemListener showHistoryItemListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.id_iv_activity_history_single_item);
            txtTitle1 = itemView.findViewById(R.id.id_tv_title1_activity_history_single_item);
            txtTitle2 = itemView.findViewById(R.id.id_tv_title2_activity_history_single_item);
            location = itemView.findViewById(R.id.id_tv_location_activity_history_single_item);
            numDays = itemView.findViewById(R.id.id_tv_days_activity_history_single_item);
            date = itemView.findViewById(R.id.id_tv_date_activity_history_single_item);
            detail = itemView.findViewById(R.id.id_tv_detail_activity_history_single_item);

            itemView.setOnClickListener((view) -> {
                showHistoryItemListener.onShowHistoryItemClick(getAdapterPosition());
            });
        }
    }

    public interface ShowHistoryItemListener{
        void onShowHistoryItemClick(int position);
    }
}
