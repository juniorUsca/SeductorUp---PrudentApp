package com.debugcc.prudentapp.adapters;

import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.debugcc.prudentapp.R;
import com.debugcc.prudentapp.fragments.MonthFragment;
import com.debugcc.prudentapp.models.DayItem;

import java.util.ArrayList;

/**
 * Created by dubgcc on 24/11/15.
 */
public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder>{

    private ArrayList<DayItem> days;
    private int itemLayout;
    private static MonthFragment itemListener;
    private MonthAdapter adp;

    public MonthAdapter(ArrayList<DayItem> days, int itemLayout, MonthFragment itemListener) {
        this.days = days;
        this.itemLayout = itemLayout;
        this.itemListener = itemListener;
        adp = this;
    }

    public void set_days(ArrayList<DayItem> days){
        this.days = days;
    }

    @Override
    public MonthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MonthAdapter.ViewHolder holder, int position) {
        DayItem day = days.get(position);

        holder.dayCircle.setText(Integer.toString(day.getDay()));
        holder.dayCircle.setBackgroundResource(day.getBackgroundColor());
        holder.dayCircle.setTextColor(holder.dayCircle.getResources().getColor(day.getTextColor()));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public Button dayCircle;


        public ViewHolder(View itemView) {
            super(itemView);
            dayCircle = (Button) itemView.findViewById(R.id.circle_day);
            dayCircle.setClickable(false);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
//            v = itemListener.getView();
            itemListener.recyclerViewListClicked(v,this.getLayoutPosition(),days,adp);
        }

        @Override
        public boolean onLongClick(View v) {
            Vibrator vi = (Vibrator) itemListener.getContext().getSystemService(itemListener.getContext().VIBRATOR_SERVICE);
            vi.vibrate(500);
            itemListener.recyclerViewListLongClicked(v, this.getLayoutPosition(),days,adp);
            return false;
        }
    }
}
