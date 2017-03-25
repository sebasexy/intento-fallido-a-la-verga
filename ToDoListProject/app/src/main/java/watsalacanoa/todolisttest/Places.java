package watsalacanoa.todolisttest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Places extends AppCompatActivity {

    Button home;
    TextView title;
    EditText note1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        title = (TextView) findViewById(R.id.places_textView);
        home = (Button) findViewById(R.id.places_button);
        note1 = (EditText) findViewById(R.id.places_editText);
        title.setText("Places");
        home.setText("Home");
        note1.setText("Place 1");
    }

    public void goHome(View v){
        Intent i = new Intent();
        i.putExtra("message","You visited Places");
        setResult(Activity.RESULT_OK,i);
        finish();
    }
}

