package ekarus.s_attendance.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;
import ekarus.s_attendance.network.ArduinoDataTransfer;
import ekarus.s_attendance.network.StopAttendanceButtonObservable;

/**
 * Created by root on 8/1/17.
 */

public class AttendanceProcessing extends AppCompatActivity {
    StopAttendanceButtonObservable sabo = null;
    Button addbutton, viewbutton;
    Data_base_helper mydb;
    Random rand;
    ArduinoDataTransfer arduinoDataTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atten_processing);
        addbutton = (Button) findViewById(R.id.button1);
        Log.d("myapp", "joy");
        mydb = new Data_base_helper(this);
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        Bundle bundle = getIntent().getExtras();
       String stuff = bundle.getString("stuff");
        stopAT();
    }

    public void stopAT() {
        Log.d("app1", "joy");
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addbutton.setEnabled(false);
                if (sabo != null) {
                    sabo.buttonPressed();
                }
                Bundle bundle = getIntent().getExtras();
                String stuff = bundle.getString("stuff");
// get unique id from database
                Cursor cursor = mydb.getAllData(stuff);
                cursor.moveToFirst();
                String datestring1 = DateFormat.getDateTimeInstance().format(new Date());
                LinkedHashSet<String> studId = new LinkedHashSet<String>();
                studId.addAll(arduinoDataTransfer.studentsId);
                if (cursor.moveToFirst()) {
                    do {
                        String student_id = cursor.getString(cursor.getColumnIndex("student_id"));
                        studId.add(student_id);
                    } while (cursor.moveToNext());
                }

                Iterator<String> itr = studId.iterator();
                ArrayList<String> s_id = new ArrayList<String>();
                while (itr.hasNext()) {
                    s_id.add(itr.next());
                }

                for(int i=0; i<s_id.size(); i++)
                {
                    if(arduinoDataTransfer.studentsId.contains(s_id.get(i))) {
                        mydb.insertData(s_id.get(i), datestring1, "P", stuff);
                    }
                    else
                    {
                        mydb.insertData(s_id.get(i), datestring1, "A", stuff);
                    }
                }

                Toast.makeText(getApplicationContext(),
                        "Data inserted Successfully.", Toast.LENGTH_LONG)
                        .show();


            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("myapp", "here");
            arduinoDataTransfer = new ArduinoDataTransfer();
            try {
                arduinoDataTransfer.startAttendance();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sabo = new StopAttendanceButtonObservable();
            sabo.addObserver(arduinoDataTransfer);

            while (!arduinoDataTransfer.finished) ;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }
}
