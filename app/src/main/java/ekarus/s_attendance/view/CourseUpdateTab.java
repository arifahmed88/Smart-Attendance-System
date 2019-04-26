package ekarus.s_attendance.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;

public class CourseUpdateTab extends Fragment {

    Data_base_helper db;
    String code;

    EditText semester,courseCode,courseName,courseCredit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.update_course, container, false);

        //font add
        String fontPath = "fonts/herofont.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        Button bt = (Button) rootView.findViewById(R.id.button);
        bt.setTypeface(tf);
        ///end
        db = new Data_base_helper(getActivity());
        Bundle bundle = getActivity().getIntent().getExtras();
        final String stuff = bundle.getString("stuff");
        code="";
        for(int i=0;i<stuff.length();i++)
        {
            if(stuff.charAt(i)=='.') code="";
            else if(stuff.charAt(i)==',') break;
            else
            {
                code+=stuff.charAt(i);
            }

        }
        String code2 =  code.trim();
        Cursor c=db.findCourse(code2);

        final String id = c.getString(0);
        Log.d("id", id);

        semester = (EditText) rootView.findViewById(R.id.textInputLayoutSemester);
        courseCode = (EditText) rootView.findViewById(R.id.textInputLayoutCourseCode);
        courseName = (EditText) rootView.findViewById(R.id.textInputLayoutCourseName);
        courseCredit = (EditText) rootView.findViewById(R.id.textInputLayoutCourseCredit);
        semester.setText(c.getString(1));
        courseCode.setText(c.getString(2));
        courseName.setText(c.getString(3));
        courseCredit.setText(c.getString(4));

        Button btn = (Button) rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String sem = semester.getText().toString();
                String code = courseCode.getText().toString();
                String name = courseName.getText().toString();
                String credit = courseCredit.getText().toString();
                if (sem.equals("") || code.equals("") || name.equals("") || name.equals("")) {

                    Toast.makeText(getActivity().getApplicationContext(), "Field Vaccant",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    db.updateCourse(id, sem, code, name, credit);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Course Added Successfully", Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });








        return rootView;
    }

}

