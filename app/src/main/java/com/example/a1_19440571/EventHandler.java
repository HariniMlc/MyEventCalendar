package com.example.a1_19440571;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1_19440571.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EventHandler extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    EditText name, location, eventdescription;
    TextView startTime;
    TextView endTime;
    TextView setDate;
    Button insert;
    CheckBox  reminder1,reminder2,reminder3;
    DBHelper DB;
    private String format = "";
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private MaterialTimePicker picker;
    private Calendar startTimeCalendar = Calendar.getInstance();
    private Calendar endTimeCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_handler);

        initWidgets();

        //--get date from previous intent
        Bundle bundle = getIntent().getExtras();
        String dayText = bundle.getString("dayText");
        String monthYearFromDate = bundle.getString("monthYearFromDate");
        String DayofWeek = bundle.getString("DayofWeek");
        DayofWeek = DayofWeek.substring(0, 1).toUpperCase()+DayofWeek.substring(1);
        setDate.setText(dayText + " " + monthYearFromDate + ", " + DayofWeek);

        //--Populating current date for the event
        Calendar date = Calendar.getInstance();
        Date targetTime = date.getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        startTime.setText(dateFormat.format(targetTime));
        long timeInSecs = date.getTimeInMillis();
        Date afterAdding60Mins = new Date(timeInSecs + (60 * 60 * 1000));
        endTime.setText(dateFormat.format(afterAdding60Mins));

        //--Create spinner for reminders
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner3 = findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        Spinner spinner1 = findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        //--Initiate database
        DB = new DBHelper(this);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(startTime, startTimeCalendar);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(endTime, endTimeCalendar);
            }
        });


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();
                String locationTXT = location.getText().toString();
                String eventdescriptionTXT = eventdescription.getText().toString();
                String event_date = setDate.getText().toString();
                String start_time = startTime.getText().toString();
                String end_time = endTime.getText().toString();

                //--convert date value
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd MMMM yyyy").parse(event_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                event_date = new SimpleDateFormat("dd/MM/yyyy").format(newDate);

                int reminderMinutes = 0;

                setReminder(nameTXT,locationTXT,eventdescriptionTXT,start_time,end_time,reminderMinutes);

                //--set reminders
                if(reminder1.isChecked()){
                    reminderMinutes = convertToMinutes(spinner1.getSelectedItem().toString());
                    setReminder(nameTXT,locationTXT,eventdescriptionTXT,start_time,end_time,reminderMinutes);
                }

                if(reminder2.isChecked()){
                    reminderMinutes = convertToMinutes(spinner1.getSelectedItem().toString());
                    setReminder(nameTXT,locationTXT,eventdescriptionTXT,start_time,end_time,reminderMinutes);
                }

                if(reminder3.isChecked()){
                    reminderMinutes = convertToMinutes(spinner1.getSelectedItem().toString());
                    setReminder(nameTXT,locationTXT,eventdescriptionTXT,start_time,end_time,reminderMinutes);
                }


                Boolean checkinsertdata = DB.insertuserdata(nameTXT, locationTXT, eventdescriptionTXT, event_date, start_time, end_time);
                if(checkinsertdata==true)
                    Toast.makeText(EventHandler.this, "Event Saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EventHandler.this, "Event not saved. Please try again", Toast.LENGTH_SHORT).show();
            }        });

    }

    private void initWidgets() {
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        eventdescription = findViewById(R.id.eventdescription);
        insert = findViewById(R.id.btnInsert);
        setDate = findViewById(R.id.seldate);

        reminder1 = findViewById(R.id.reminder1);
        reminder2 = findViewById(R.id.reminder2);
        reminder3 = findViewById(R.id.reminder3);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
    }

    //--get time from timepicker and apply it to calendar object
    private void showTime(TextView txtTime, final Calendar calendar1) {

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .build();

        picker.show(getSupportFragmentManager(),"TimePicker");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picker.getHour() > 12){
                    DecimalFormat formatter = new DecimalFormat("00");
                    txtTime.setText(
                            String.format((picker.getHour()-12)+ ":"+ picker.getMinute()+" PM")
                    );
                }
                else{
                    txtTime.setText(
                            String.format(picker.getHour()+ ":"+ picker.getMinute()+" AM")
                    );
                }

                calendar1.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar1.set(Calendar.MINUTE,picker.getMinute());
                calendar1.set(Calendar.SECOND,0);
                calendar1.set(Calendar.MILLISECOND,0);
            }
        });
    }

    //--set reminder
    private void setReminder(String nameTXT, String locationTXT, String eventdescriptionTXT, String start_time, String end_time, int reminderMinutes) {
        Intent intent = new Intent(this,ReminderReceiver.class);
        //Pass optional parameters
        Bundle bundle = new Bundle();
        bundle.putString("nameTXT", nameTXT);
        bundle.putString("locationTXT", locationTXT);
        bundle.putString("eventdescriptionTXT", eventdescriptionTXT);
        bundle.putString("start_time", start_time);
        bundle.putString("end_time", end_time);
        intent.putExtras(bundle);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_MUTABLE);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar(
                startTimeCalendar.get(Calendar.YEAR),
                startTimeCalendar.get(Calendar.MONTH),
                startTimeCalendar.get(Calendar.DATE),
                startTimeCalendar.get(Calendar.HOUR_OF_DAY),
                startTimeCalendar.get(Calendar.MINUTE),
                0
        );
        calendar.add(Calendar.MINUTE,-reminderMinutes);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent);
    }



    //--Convert the time interval of the reminder to minutes
    public int convertToMinutes(String DurationValue) {
        char lastChar = DurationValue.charAt(DurationValue.length()-1);
        int minutes = 0;
        int duration = 0;
        switch(lastChar){
            case 'h': minutes = 60; break;
            case 'm': minutes = 1; break;
            default:break;
        }
        duration = Integer.parseInt(DurationValue.substring(0, DurationValue.length() - 1));
        return (duration * minutes);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}