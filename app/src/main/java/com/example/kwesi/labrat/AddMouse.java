package com.example.kwesi.labrat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddMouse extends AppCompatActivity {

    MouseDatabase mouseDB;
    //List<Mouse> mice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mouse);

        //Init Database and List of Mice
        mouseDB = MouseDatabase.getAppDatabase(this);
        //mice = mouseDB.userDao().getAll();


        /*Create Calendar Date Picker for */
        final Calendar calendar = Calendar.getInstance();

        final EditText pairedBox = findViewById(R.id.paired);
        final EditText dobBox = findViewById(R.id.mouseDob);

        final DatePickerDialog.OnDateSetListener datePaired = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(pairedBox,calendar);
            }
        };

        final DatePickerDialog.OnDateSetListener dateDob = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dobBox,calendar);
            }
        };



        dobBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMouse.this, dateDob, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pairedBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMouse.this, datePaired, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button addMouseButton = findViewById(R.id.addMouseButton);
        addMouseButton.setOnClickListener(createAddMouseListener(this));




    }

    private void addMouseToDatabase(Mouse mouse){
        mouseDB.userDao().insert(mouse);
    }

    private void updateLabel(EditText editText,Calendar calendar) {
        String myFormat = "MM.dd.yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private View.OnClickListener createAddMouseListener(final Context context){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parentDesc = ((EditText)findViewById(R.id.parentDescription)).getText().toString();
                String color = ((EditText)findViewById(R.id.mouseColor)).getText().toString();
                String dateStringPaired = ((EditText)findViewById(R.id.paired)).getText().toString();
                String dateStringDob = ((EditText)findViewById(R.id.mouseDob)).getText().toString();
                String dateFormat = "MM.dd.yy";

                Mouse mouse;

                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                if(parentDesc == ""){
                    Toast.makeText(context,"Enter Parent Description",Toast.LENGTH_SHORT).show();
                }
                else if(color == ""){
                    Toast.makeText(context,"Enter Color",Toast.LENGTH_SHORT).show();
                }

                try {
                    Date datePaired = sdf.parse(dateStringPaired);
                    Date dateDob = sdf.parse(dateStringDob);
                    mouse = new Mouse(parentDesc,datePaired,dateDob,color);
                    addMouseToDatabase(mouse);
                    Toast.makeText(context,"Mouse Added",Toast.LENGTH_SHORT).show();
                }
                catch (ParseException p){
                    Toast.makeText(context,"Invalid Date",Toast.LENGTH_SHORT).show();
                }


            }
        };
        return listener;
    }


}
