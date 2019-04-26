package ekarus.s_attendance.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;

/**
 * Created by auny on 8/18/2017.
 */

public class AddCourse extends AppCompatActivity {
    Data_base_helper db;
    String course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);

        //font add
        String fontPath = "fonts/herofont.ttf";
        TextView txtGhost = (TextView) findViewById(R.id.editText);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Button bt = (Button) findViewById(R.id.button);
        txtGhost.setTypeface(tf);
        bt.setTypeface(tf);
        ///end

        db = new Data_base_helper(this);
        final EditText semester = (EditText) findViewById(R.id.textInputLayoutSemester);
        final EditText courseName = (EditText) findViewById(R.id.textInputLayoutCourseName);
        final EditText courseCode = (EditText) findViewById(R.id.textInputLayoutCourseCode);
        final EditText courseCredit = (EditText) findViewById(R.id.textInputLayoutCourseCredit);


        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String sem = semester.getText().toString();
                String code = courseCode.getText().toString();
                String name = courseName.getText().toString();
                String credit = courseCredit.getText()
                        .toString();
                if (sem.equals("") || code.equals("") || name.equals("") || name.equals("")) {

                    Toast.makeText(getApplicationContext(), "Field Vaccant",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    db.insertCourseData(sem, code, name, credit);
                    db.createAttendancetable(code);
                    Toast.makeText(getApplicationContext(),
                            "Course Added Successfully", Toast.LENGTH_LONG)
                            .show();
                    Cursor c=db.findCourse(code);
                    c.moveToFirst();
                    course_id="student_"+c.getString(0);
                    Log.d("z",course_id);
                    db.createAttendancetable(course_id);
                    Intent intent;
                    intent = new Intent(AddCourse.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });






    }
}
