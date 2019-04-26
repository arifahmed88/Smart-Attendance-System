package ekarus.s_attendance.view;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by root on 8/12/17.
 */

public class FullAttendanceShow extends AppCompatActivity {
    Data_base_helper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_attendance_show);
        mydb = new Data_base_helper(this);
        ShowFullAttendance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExportAttendance();
                Snackbar.make(view, "Data Exported in Excel Sheet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public String ShowFullAttendance() {
        Bundle bundle = getIntent().getExtras();
        String arif = bundle.getString("stuff");
        Cursor cursor = mydb.getuser(arif);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String datestring = cursor.getString(cursor.getColumnIndex("date"));
            Log.d("aaaaa", datestring);
            ArrayList<String> al = new ArrayList<String>();
            al.add(datestring);
            LinkedHashSet<String> studId = new LinkedHashSet<String>();
            if (cursor.moveToFirst()) {
                do {
                    String student_id = cursor.getString(cursor.getColumnIndex("student_id"));
                    studId.add(student_id);
                    // Log.d("mmm", student_id);
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    if (!datestring.equals(date)) {
                        //Log.d("mmm", date);
                        al.add(date);
                        datestring = date;
                    }
                } while (cursor.moveToNext());
            }
            for (int i = 0; i < al.size(); i++) {
                Log.d("mmm", al.get(i));
            }
            Iterator<String> itr = studId.iterator();
            ArrayList<String> s_id = new ArrayList<String>();
            while (itr.hasNext()) {
                s_id.add(itr.next());
            }
            String header = "";
            header += "Student ID" + "      ";
            int i;
            for (i = 0; i < al.size(); i++) {
                header += al.get(i) + "    ";
            }
            header += "Percentage";
            TextView textView = (TextView) findViewById(R.id.headershow);
            SpannableString underline = new SpannableString(header);
            Log.d("qqqq", header);
            underline.setSpan(new UnderlineSpan(), 0, header.length(), 0);
            textView.setText(underline);
            double percentage;
            int sum = 0, k;
            String headerbelow = "";
            for (int l = 0; l < s_id.size(); l++) {
                headerbelow += s_id.get(l) + "                                   ";
                sum = 0;
                for (k = 0; k < al.size(); k++) {
                    Cursor cursor1 = mydb.findAttendance(s_id.get(l), al.get(k), arif);
                    cursor1.moveToFirst();
                    String Attendance_1 = cursor1.getString(cursor1.getColumnIndex("attendance"));
                    if (k < al.size() - 1) {
                        headerbelow += Attendance_1 + "                                                                 ";
                    } else headerbelow += Attendance_1 + "                             ";
                    if (Attendance_1.equals("P")) {
                        sum += 1;
                    }
                }
                percentage = (double) sum / al.size() * 100;
                Double toBeTruncated = new Double("" + percentage);
                Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                Log.d("per", "" + truncatedDouble);
                headerbelow += truncatedDouble + "%\n";

            }


            TextView textView1 = (TextView) findViewById(R.id.attendance_show);
            Log.d("wwww", headerbelow);
            textView1.setText(headerbelow);
        }
            else
            {
                Toast.makeText(getApplication(),
                        "No record found in database.", Toast.LENGTH_SHORT).show();
            }
        cursor.close();
        return "joy";
    }

    public String ExportAttendance() {
        Bundle bundle = getIntent().getExtras();
        String arif = bundle.getString("stuff");
        final Cursor cursor = mydb.getuser(arif);
        File sd = new File(Environment.getExternalStorageDirectory(), "SMARTATTENDANCE");
        String csvFile = "ExportAttendance.xls";
        File directory = new File(sd.getAbsolutePath());
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            //file path
            File file = new File(directory, csvFile);
            Log.d("jjj", "joy");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            cursor.moveToFirst();
            String datestring = cursor.getString(cursor.getColumnIndex("date"));
            ArrayList<String> al = new ArrayList<String>();
            al.add(datestring);
            LinkedHashSet<String> studId = new LinkedHashSet<String>();
            if (cursor.moveToFirst()) {
                do {
                    String student_id = cursor.getString(cursor.getColumnIndex("student_id"));
                    studId.add(student_id);
                    // Log.d("mmm", student_id);
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    if (!datestring.equals(date)) {
                        //Log.d("mmm", date);
                        al.add(date);
                        datestring = date;
                    }
                } while (cursor.moveToNext());
            }
            for (int i = 0; i < al.size(); i++) {
                Log.d("mmm", al.get(i));
            }
            Iterator<String> itr = studId.iterator();
            ArrayList<String> s_id = new ArrayList<String>();
            while (itr.hasNext()) {
                s_id.add(itr.next());
            }
            sheet.addCell(new Label(0, 0, "Student ID"));
            int i;
            for (i = 0; i < al.size(); i++) {
                sheet.addCell(new Label(i + 1, 0, al.get(i)));
            }
            sheet.addCell(new Label(i + 1, 0, "Percentage"));
            double percentage;
            int sum = 0, k;
            for (int l = 0; l < s_id.size(); l++) {
                sheet.addCell(new Label(0, l + 1, s_id.get(l)));
                sum = 0;
                for (k = 0; k < al.size(); k++) {
                    Cursor cursor1 = mydb.findAttendance(s_id.get(l), al.get(k), arif);
                    cursor1.moveToFirst();
                    String Attendance_1 = cursor1.getString(cursor1.getColumnIndex("attendance"));
                    sheet.addCell(new Label(k + 1, l + 1, Attendance_1));
                    if (Attendance_1.equals("P")) {
                        sum += 1;
                    }
                }
                percentage = (double) sum / al.size() * 100;
                Double toBeTruncated = new Double("" + percentage);
                Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                Log.d("per", "" + truncatedDouble);
                sheet.addCell(new Label(k + 1, l + 1, "" + truncatedDouble + "%"));
            }
            cursor.close();
            workbook.write();
            workbook.close();
           /* Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        File file = new File(Environment.getExternalStorageDirectory() + "/SMARTATTENDANCE/" + "ExportAttendance.xls");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
        startActivity(intent);*/
        return "joy";
    }
}
