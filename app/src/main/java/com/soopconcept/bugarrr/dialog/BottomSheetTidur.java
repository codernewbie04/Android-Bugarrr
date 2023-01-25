package com.soopconcept.bugarrr.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.soopconcept.bugarrr.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetTidur extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText et_end_time, et_start_time;
    private Context mContext;
    View root;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    int insert_type = 1;
    BottomSheetTidur frag;
    private BottomSheetListener mListener;
    private Button btn_tambah;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.bottom_sheet_tidur, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initEvent();
    }

    private void initUI() {
        mContext=getContext();
        frag = this;
        et_end_time = root.findViewById(R.id.et_end_time);
        et_start_time = root.findViewById(R.id.et_start_time);
        btn_tambah = root.findViewById(R.id.btn_tambah);
    }

    private void initEvent() {
        et_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_type = 1;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, frag ,year, month,day);
                datePickerDialog.show();
            }
        });

        et_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_type = 2;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, frag ,year, month,day);
                datePickerDialog.show();
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp start_time = null, end_time = null;
                Map<String, Timestamp> sleep_data = new HashMap<>();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    java.util.Date date = dateFormatter.parse(et_start_time.getText().toString());
                    java.util.Date date2 = dateFormatter.parse(et_end_time.getText().toString());
                    start_time = new Timestamp(date);
                    end_time = new Timestamp(date2);
                } catch (ParseException e) {
//                    start_time = new Timestamp(new java.util.Date());
//                    end_time = new Timestamp(new java.util.Date());
                    e.printStackTrace();
                }

                sleep_data.put("start", start_time);
                sleep_data.put("end", end_time);

                mListener.onTambahClicked(sleep_data);
                dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        Toast.makeText(mContext, "Day:" +day, Toast.LENGTH_SHORT).show();
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, frag, hour, minute, DateFormat.is24HourFormat(mContext));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        String date_format = myday+"-"+(myMonth+1)+"-"+myYear+" "+myHour+":"+myMinute+":00";
        if(insert_type == 1){
            et_start_time.setText(date_format);
        } else {
            et_end_time.setText(date_format);
        }
    }

    public interface BottomSheetListener{
        void onTambahClicked(Map<String, Timestamp> data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Error");
        }
        Log.e("tastast", context.toString());
    }
}
