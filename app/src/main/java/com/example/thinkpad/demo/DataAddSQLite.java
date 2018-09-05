package com.example.thinkpad.demo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DataAddSQLite extends AppCompatActivity {

    private static final String TAG = "DataAddSQLite";
    DatabaseHelper mDatabaseHelper;

    private EditText etText;
    private Button btnSave, btnView;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView tvTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_add_sqlite);

        etText = (EditText) findViewById(R.id.etText);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnView = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = etText.getText().toString();
                if (etText.length() != 0) {
                    AddData(newEntry);
                    etText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataAddSQLite.this, ListLayout.class);
                startActivity(intent);
            }
        });

        tvTime = (TextView) findViewById(R.id.tvtime);
        mDisplayDate = (TextView) findViewById(R.id.tvdate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DataAddSQLite.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                Log.d(TAG, "onDateSet: mm/dd/yy: "+ month +"/" + day + "/"+year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        };

    }

    public void setTime(View view) {
        Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR);
        int minute = calender.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                DataAddSQLite.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay+":"+minute);
            }
        },hour,minute,false);
        timePickerDialog.show();

    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
