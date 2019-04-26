package ekarus.s_attendance.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;


/**
 * Created by root on 8/5/17.
 */

public class Individual_attendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Data_base_helper mydb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_attendance);
        mydb = new Data_base_helper(this);
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    public void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String currentDate = dateFormat.format(calendar.getTime());
        Bundle bundle = getIntent().getExtras();
        final String arif = bundle.getString("stuff");
        String datestring1 = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("arif", arif);
        Cursor cursor1 = mydb.getuser(arif);
        Log.d("arif1", arif);
        cursor1.moveToFirst();
        while(cursor1.moveToNext()) {
            Log.d("aaaa", cursor1.getString(1));
            Log.d("aaaa", cursor1.getString(2));

        }
        Log.d("arid", arif);

        Log.d("sss", "reach");
        mydb = new Data_base_helper(this);
        Cursor cursor = mydb.searchDatabase(currentDate, arif);
        Log.d("sss", "reach1");

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String datestring = cursor.getString(cursor.getColumnIndex("date"));
            Log.d("sss", "reach2");
            final ArrayList<String> al = new ArrayList<String>();
            al.add(datestring);
            Log.d("sss", "reach3");
            LinkedHashSet<String> studId = new LinkedHashSet<String>();
            if (cursor.moveToFirst()) {
                do {
                    String student_id = cursor.getString(cursor.getColumnIndex("student_id"));
                    studId.add(student_id);
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    if (!datestring.equals(date)) {
                        al.add(date);
                        datestring = date;
                    }
                } while (cursor.moveToNext());
            }
            for (int i = 0; i < al.size(); i++) {
                Log.d("mmm", al.get(i));
            }
            CharSequence[] items = new CharSequence[al.size()];

            for (int i = 0; i < al.size(); i++) {
                items[i] = al.get(i);
            }

            String temp;

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(Individual_attendance.this);
            builderSingle.setTitle("More than one record found on this date.");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < al.size(); i++) {
                arrayAdapter.add(al.get(i));
            }


            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //if (al.size() > 0) {
                    String strname;
                    strname = arrayAdapter.getItem(i);
                    mydb = new Data_base_helper(Individual_attendance.this);
                    Cursor result = mydb.findAttendanceviadate(strname, arif);
                    String print = "";
                    if (result.moveToFirst()) {
                        do {
                            String student_id = result.getString(result.getColumnIndex("student_id"));
                            print += "  " + student_id + "          ";
                            String date = result.getString(result.getColumnIndex("date"));
                            print += date + "                ";
                            String Attendance_1 = result.getString(result.getColumnIndex("attendance"));
                            print += Attendance_1 + "\n";
                        } while (result.moveToNext());
                    }
                    String temp = "";
                    temp += "  " + "StudentID" + "                ";
                    temp += "Date" + "                ";
                    temp += "Attendance";
                    TextView header = (TextView) findViewById(R.id.textView1);
                    SpannableString underline = new SpannableString(temp);
                    underline.setSpan(new UnderlineSpan(), 0, temp.length(), 0);
                    header.setText(underline);
                    TextView text = (TextView) findViewById(R.id.textView);
                    text.setText(print);
                }
            });
            builderSingle.show();

        }

        else
        {
            Toast.makeText(getApplication(),
                    "No record found on this date.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }
}
