package watsalacanoa.todolisttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Button checklist,notioli,places,calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        checklist = (Button) findViewById(R.id.button);
        notioli = (Button) findViewById(R.id.button2);
        places = (Button) findViewById(R.id.button3);
        calendar = (Button) findViewById(R.id.button4);

        checklist.setText("Checklist");
        notioli.setText("Notioli");
        places.setText("Places");
        calendar.setText("Calendioli");
    }

    public void checklist(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivityForResult(i,0);
    }

    public void notioli(View v){
        Intent i = new Intent(this,Notioli.class);
        startActivityForResult(i,1);
    }

    public void places(View v){
        Intent i = new Intent(this,Places.class);
        startActivityForResult(i,2);
    }

    public void calendar(View v){
        Intent i = new Intent(this,Calendioli.class);
        startActivityForResult(i,3);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this,data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }
    }
}
