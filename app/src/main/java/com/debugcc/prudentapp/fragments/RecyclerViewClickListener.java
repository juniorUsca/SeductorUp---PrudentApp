package com.debugcc.prudentapp.fragments;

import android.view.View;

import com.debugcc.prudentapp.adapters.MonthAdapter;
import com.debugcc.prudentapp.models.DayItem;

import java.util.ArrayList;

/**
 * Created by dubgcc on 24/11/15.
 */
public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, int position, ArrayList<DayItem> days, MonthAdapter adp);
    public void recyclerViewListLongClicked(View v, int position, ArrayList<DayItem> days, MonthAdapter adp);
}
