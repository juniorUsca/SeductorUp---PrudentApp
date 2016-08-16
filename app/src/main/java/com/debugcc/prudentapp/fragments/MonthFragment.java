package com.debugcc.prudentapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debugcc.prudentapp.R;
import com.debugcc.prudentapp.adapters.MonthAdapter;
import com.debugcc.prudentapp.db.PrudentAppDB;
import com.debugcc.prudentapp.models.DayItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthFragment extends Fragment implements RecyclerViewClickListener{

    final String TAG = "MonthFragment";

    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private MonthAdapter mAdapter;

    private ArrayList<DayItem> mDayItems;
    private GregorianCalendar mCalendar;
    private Calendar mCalendarToday;
    private final String[] mStates = { "Día de Flujo", "Día Seguro", "Día Inseguro", "Día Fértil" };
    private final String[] mDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
    private final int[] mDaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };


    private static final String ARG_MONTH = "month";
    private static final String ARG_YEAR = "year";

    private int mMonth;
    private int mYear;
    private FragmentManager fm;
    private FragmentPagerAdapter pa;

    PrudentAppDB mDB;

    private int mLastClickPos = 0;


    public static MonthFragment newInstance(int month,int year) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }
    public MonthFragment() {
    }

    public void set_fragmentManager(FragmentManager fm) {
        this.fm = fm;
    }
    public void set_pagerAdapter(FragmentPagerAdapter pa) {
        this.pa = pa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// se pueden obtener argumentos
        if (getArguments() != null) {
            mMonth = getArguments().getInt(ARG_MONTH);
            mYear = getArguments().getInt(ARG_YEAR);
        }

        mDB = new PrudentAppDB(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);


        /// generamos datos
        mDayItems = populateMonth(mYear,mMonth);
        mAdapter = new MonthAdapter(mDayItems, R.layout.item_calendar, this);
        // Set the adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.calendar_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        consultar_mes(mDayItems);
        mAdapter.notifyDataSetChanged();

        TextView mes_anio = (TextView) view.findViewById(R.id.mes_anio);
        if (mMonth == 0)
            mes_anio.setText("Enero del "+mYear);
        else if (mMonth == 1)
            mes_anio.setText("Febrero del "+mYear);
        else if (mMonth == 2)
            mes_anio.setText("Marzo del "+mYear);
        else if (mMonth == 3)
            mes_anio.setText("Abril del "+mYear);
        else if (mMonth == 4)
            mes_anio.setText("Mayo del "+mYear);
        else if (mMonth == 5)
            mes_anio.setText("Junio del "+mYear);
        else if (mMonth == 6)
            mes_anio.setText("Julio del "+mYear);
        else if (mMonth == 7)
            mes_anio.setText("Agosto del "+mYear);
        else if (mMonth == 8)
            mes_anio.setText("Septiembre del "+mYear);
        else if (mMonth == 9)
            mes_anio.setText("Octubre del "+mYear);
        else if (mMonth == 10)
            mes_anio.setText("Noviembre del "+mYear);
        else if (mMonth == 11)
            mes_anio.setText("Diciembre del "+mYear);

        return view;
    }


    /// funciones para poblar el mes
    private int getDay(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }
    private int daysInMonth(int month, int mYear) {
        int daysInMonth = mDaysInMonth[month];
        if (month == 1 && mCalendar.isLeapYear(mYear))
            daysInMonth++;
        return daysInMonth;
    }

    private ArrayList<DayItem> populateMonth(int year, int month) {
        mCalendarToday = Calendar.getInstance();

        int mDaysShown = 0;
        int mDaysNextMonth;

        ArrayList<DayItem> dayItems = new ArrayList<DayItem>();

        mCalendar = new GregorianCalendar(year, month, 1);

        int firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
        int prevDay;

        if (month == 0) {
            prevDay = daysInMonth(11, year) - firstDay + 1;
        }else{
            prevDay = daysInMonth(month - 1,year) - firstDay + 1;
        }

        for (int i = 0; i < firstDay; i++) {
            //mItems.add(String.valueOf(prevDay + i));
            DayItem dia = new DayItem();
            dia.setDay(prevDay + i);
            if (month == 0) {
                dia.setMonth(11);
                dia.setYear(year - 1);
            } else {
                dia.setMonth(month - 1);
                dia.setYear(year);
            }
            dia.setBackgroundColor(R.drawable.white_circle);
            dia.setTextColor(R.color.grey_400);
            dia.setState("");
            dayItems.add(dia);

            mDaysShown++;
        }

        int daysInMonth = daysInMonth(month,year);
        for (int i = 1; i <= daysInMonth; i++) {
            //mItems.add(String.valueOf(i));
            DayItem dia = new DayItem();
            dia.setDay(i);
            dia.setMonth(month);
            dia.setYear(year);
            dia.setBackgroundColor(R.drawable.white_circle);
            dia.setTextColor(R.color.grey_800);
            dia.setState("");
            dayItems.add(dia);

            mDaysShown++;
        }

        mDaysNextMonth = 1;
        while (mDaysShown % 7 != 0) {
            //mItems.add(String.valueOf(mDaysNextMonth));
            DayItem dia = new DayItem();
            dia.setDay(mDaysNextMonth);
            if (month == 11) {
                dia.setMonth(0);
                dia.setYear(year + 1);
            } else {
                dia.setMonth(month + 1);
                dia.setYear(year);
            }

            dia.setBackgroundColor(R.drawable.white_circle);
            dia.setTextColor(R.color.grey_400);
            dia.setState("");
            dayItems.add(dia);

            mDaysShown++;
            mDaysNextMonth++;
        }
        return dayItems;

    }

    @Override
    public void recyclerViewListClicked(View v, int position, ArrayList<DayItem> days,MonthAdapter adp) {

        /*DayItem day_item = days.get(mLastClickPos);
        String state = day_item.getState();
        if(state == mStates[0]) {
            day_item.setBackgroundColor(R.drawable.flujo_circle);
        } else if(state == mStates[1]) {
            day_item.setBackgroundColor(R.drawable.seguro_circle);
        } else if(state == mStates[2]) {
            day_item.setBackgroundColor(R.drawable.inseguro_circle);
        }else if(state == mStates[3]) {
            day_item.setBackgroundColor(R.drawable.fertil_circle);
        }else
            day_item.setBackgroundColor(R.drawable.white_circle);
        day_item.setTextColor(R.color.grey_800);
        days.set(mLastClickPos,day_item);*/



        DayItem day_item = days.get(position);
        TextView text_state = (TextView) getActivity().findViewById(R.id.text_state);
        String state = day_item.getState();
        text_state.setText(state);
        /*day_item.setTextColor(R.color.grey_100);
        if(state == mStates[0]) {
            day_item.setBackgroundColor(R.color.red_900);
        } else if(state == mStates[1]) {
            day_item.setBackgroundColor(R.color.amber_900);
        } else if(state == mStates[2]) {
            day_item.setBackgroundColor(R.color.teal_A700);
        }else if(state == mStates[3]) {
            day_item.setBackgroundColor(R.color.teal_900);
        }else
            day_item.setBackgroundColor(R.color.colorPrimary);*/
        days.set(position,day_item);
        adp.notifyDataSetChanged();
        mLastClickPos = position;
    }

    @Override
    public void recyclerViewListLongClicked(View v, int position, ArrayList<DayItem> days,MonthAdapter adp) {

        Cursor c = mDB.selectRecords();
        if (c.isAfterLast() == false) {
            int id = Integer.parseInt(c.getString(c.getColumnIndex("_id")));
            mDB.deleteRecords(id);

            int year = days.get(8).getYear();
            int month = days.get(8).getMonth();

            days = populateMonth(year,month);

            adp.set_days(days);

            //mAdapter.notifyDataSetChanged();
            adp.notifyDataSetChanged();

            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //FragmentManager.BackStackEntry first = fm.getBackStackEntryAt(0);
            //fm.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.e(TAG, "DESPUES DE ELIMINAR");
            pa.notifyDataSetChanged();

            return;
        }


        DayItem day_item = days.get(position);
        int year = day_item.getYear();
        int month = day_item.getMonth();
        int day = day_item.getDay();

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day);
        long milliseconds = cal.getTimeInMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(milliseconds);
        Log.e(TAG, "Dia seleccionado : " + date);


        cal.set(year, month, day);
        long start_date = cal.getTimeInMillis();
        cal.set(year, month, day + 4);
        long end_date = cal.getTimeInMillis();

        mDB.createRecord(start_date, end_date, position, position + 4);
        consultar_mes(days);
        //mAdapter.notifyDataSetChanged();
        adp.notifyDataSetChanged();


        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //FragmentManager.BackStackEntry first = fm.getBackStackEntryAt(0);
        //fm.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        pa.notifyDataSetChanged();

    }


    void consultar_mes(ArrayList<DayItem> days){
        Log.e(TAG, "consultando_mes " );
        DayItem first_day = days.get(0);
        DayItem last_day = days.get(days.size() - 1);

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(first_day.getYear(), first_day.getMonth(), first_day.getDay());
        long l_first_day = cal.getTimeInMillis();
        cal.set(last_day.getYear(), last_day.getMonth(), last_day.getDay());
        long l_last_day = cal.getTimeInMillis();


        //Cursor c = mDB.selectRecords(l_first_day, l_last_day);
        Cursor c = mDB.selectRecords();

        int ini_pos, end_pos, current, end_e;
        long start_date, end_date;

        //while(c.isAfterLast() == false){
        if (c.isAfterLast() == false) {
            ini_pos = Integer.parseInt(c.getString(c.getColumnIndex("ini_pos")));
            end_pos = Integer.parseInt(c.getString(c.getColumnIndex("end_pos")));
            start_date = Long.parseLong(c.getString(c.getColumnIndex("start_date")));
            end_date = Long.parseLong(c.getString(c.getColumnIndex("end_date")));


            long diff = l_first_day-start_date;
            long diff_days = diff / (24 * 60 *60 * 1000);

            if(diff > 0){ /// estan en un mes superior
                Log.e(TAG, "consultar_mes superior: ENTROOO!");

                ini_pos = (int) (28 - (diff_days % 28));

                /// coloreamos dias anteriores a una fecha especial
                if(ini_pos > 0 ) {
                    current = ini_pos - 1;
                    int cont = 0;
                    int flag = 0;
                    for (; current >= 0; --current) {
                        if(flag == 0) {

                            DayItem d = days.get(current);
                            d.setBackgroundColor(R.drawable.seguro_circle);
                            d.setState(mStates[1]);
                            days.set(current, d);
                            ++cont;
                            if(cont == 9){
                                flag = 1;
                                cont = 0;
                                continue;
                            }
                        } else if(flag == 1) {
                            DayItem d = days.get(current);
                            d.setBackgroundColor(R.drawable.inseguro_circle);
                            d.setState(mStates[2]);
                            days.set(current, d);
                            ++cont;
                            if(cont == 4){
                                flag = 2;
                                cont = 0;
                                continue;
                            }
                        } else if (flag == 2) {
                            DayItem d = days.get(current);
                            d.setBackgroundColor(R.drawable.fertil_circle);
                            d.setState(mStates[3]);
                            days.set(current, d);
                            ++cont;
                            if(cont == 3){
                                flag = 3;
                                cont = 0;
                                continue;
                            }
                        } else if (flag == 3) {
                            DayItem d = days.get(current);
                            d.setBackgroundColor(R.drawable.inseguro_circle);
                            d.setState(mStates[2]);
                            days.set(current, d);
                            ++cont;
                            if(cont == 5){
                                flag = 4;
                                cont = 0;
                                continue;
                            }
                        } else if (flag==4) {
                            DayItem d = days.get(current);
                            d.setBackgroundColor(R.drawable.seguro_circle);
                            d.setState(mStates[1]);
                            days.set(current, d);
                            ++cont;
                            if(cont == 2){
                                flag = 5;
                                cont = 0;
                                continue;
                            }
                        } else if(flag == 5) {
                            DayItem d = days.get(current);
                            d.setBackgroundColor(R.drawable.flujo_circle);
                            d.setState(mStates[0]);
                            days.set(current, d);
                            ++cont;
                            if(cont == 5){
                                flag = 6;
                                cont = 0;
                                continue;
                            }
                        }

                    }
                }


            } else {
                if (diff <= 0) {
                    /// no es el mes actual
                    Log.e(TAG, "consultar_mes: " + ini_pos + " " + diff_days);
                    if( ini_pos != (-diff_days)+1 ){

                        c.close();
                        return;

                    }
                }
            }

            /// dias centrales
            int max = days.size();
            /// 5 o x dias de flujo
            end_e = ini_pos + 5;
            //end_pos = end_e;
            for(current = ini_pos; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setState(mStates[0]);
                d.setBackgroundColor(R.drawable.flujo_circle);
                days.set(current, d);
            }
            /// (7-dias_flujo) = 2 de dias seguros
            //end_e = end_pos+(7-(end_pos-ini_pos));
            end_e = current + 2;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.seguro_circle);
                d.setState(mStates[1]);
                days.set(current, d);
            }
            /// 5 dias inseguros
            end_e = current + 5;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.inseguro_circle);
                d.setState(mStates[2]);
                days.set(current, d);
            }
            /// 3 dias fertiles
            end_e = current + 3;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.fertil_circle);
                d.setState(mStates[3]);
                days.set(current, d);
            }
            /// 4 dias inseguros
            end_e = current + 4;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.inseguro_circle);
                d.setState(mStates[2]);
                days.set(current, d);
            }
            /// 9 dias seguros
            end_e = current + 9;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.seguro_circle);
                d.setState(mStates[1]);
                days.set(current, d);
            }

            /// dias finales

            end_e = current + 5;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.flujo_circle);
                d.setState(mStates[0]);
                days.set(current, d);
            }
            /// (7-dias_flujo) = 2 de dias seguros
            end_e = current + 2;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.seguro_circle);
                d.setState(mStates[1]);
                days.set(current, d);
            }
            /// 5 dias inseguros
            end_e = current + 5;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.inseguro_circle);
                d.setState(mStates[2]);
                days.set(current, d);
            }
            /// 3 dias fertiles
            end_e = current + 3;
            for( ; current<end_e;++current){
                if(current >=  max)
                    break;
                DayItem d = days.get(current);
                d.setBackgroundColor(R.drawable.fertil_circle);
                d.setState(mStates[3]);
                days.set(current, d);
            }

            mAdapter.set_days(days);

            //c.moveToNext();
        }
        else{
            Log.e(TAG, "consultar_mes: entro al else" );
            /*int year = days.get(8).getYear();
            int month = days.get(8).getMonth();
            days = populateMonth(year,month);
            mAdapter.set_days(days);*/
        }
        c.close();
    }

}
