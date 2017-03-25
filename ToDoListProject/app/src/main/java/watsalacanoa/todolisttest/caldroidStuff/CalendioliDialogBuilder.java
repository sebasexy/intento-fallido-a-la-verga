package watsalacanoa.todolisttest.caldroidStuff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hirondelle.date4j.DateTime;
import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

/**
 * Created by spide on 17/3/2017.
 */

public class CalendioliDialogBuilder extends DialogFragment{

    private EditText etTitle,
                     etEvent;
    private TextView tvDate;
    private String dateText;
    private String title;
    private String event;
   private TaskHelper calendioliDB;

    public void setDate(String date){
       this.dateText = date;
    }
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add event");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calendioli_dialog, null);
        calendioliDB = new TaskHelper(getActivity());
        etTitle = (EditText)view.findViewById(R.id.etTitle);
        etEvent = (EditText)view.findViewById(R.id.etEvent);
        tvDate = (TextView)view.findViewById(R.id.tvDate);

        tvDate.setText(this.dateText);
        Toast.makeText(getActivity(), "DATE: " + dateText, Toast.LENGTH_SHORT).show();
        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        title = etTitle.getText().toString();
                        event = etEvent.getText().toString();
                        SQLiteDatabase db = calendioliDB.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.COLUMN_ID, dateText);
                        values.put(Task.CALENDIOLI_TITLE, title);
                        values.put(Task.CALENDIOLI_EVENT, event);
                        db.insertWithOnConflict(Task.TABLE_NOTIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        //Toast.makeText(getActivity(), "Title: " + title, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), "Event: " + event, Toast.LENGTH_SHORT).show()
                        /*
                        SQLiteDatabase db = calendioliDB.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.CALENDIOLI_ID, "1");
                        values.put(Task.CALENDIOLI_TITLE, title);
                        values.put(Task.CALENDIOLI_EVENT, event);
                        db.insertWithOnConflict(Task.TABLE_CALENDIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        */
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CalendioliDialogBuilder.this.getDialog().cancel();
            }
        });
        return  builder.create();
    }
}
