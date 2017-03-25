package watsalacanoa.todolisttest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import java.util.Formatter;
import java.util.Locale;

import watsalacanoa.todolisttest.caldroidStuff.CalendioliDialogBuilder;

public class Calendioli extends AppCompatActivity {

    Button home;
    TextView title;
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    CaldroidFragment caldroidFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        title = (TextView) findViewById(R.id.calendar_textView);
        home = (Button) findViewById(R.id.calendar_button);


        title.setText("Calendioli");
        home.setText("Home");



        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(listener);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

    }

    public void goHome(View v){
        Intent i = new Intent();
        i.putExtra("message","You visited Calendioli");
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    final CaldroidListener listener = new CaldroidListener(){
        @Override
        public void onSelectDate(Date date, View view) {
            String selectedDate = formatter.format(date);
            Toast.makeText(getApplicationContext(), selectedDate,
                    Toast.LENGTH_SHORT).show();

        }
        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            CalendioliDialogBuilder cdb = new CalendioliDialogBuilder();
            String time = new SimpleDateFormat("yyyyMMdd").format(date);
            cdb.setDate(time);
            //cdb.show(g, "HOLI");
            cdb.show(getFragmentManager(), "hh");


        }

        @Override
        public void onCaldroidViewCreated() {
            Toast.makeText(getApplicationContext(),
                    "Caldroid view is created",
                    Toast.LENGTH_SHORT).show();
        }
    };
}
