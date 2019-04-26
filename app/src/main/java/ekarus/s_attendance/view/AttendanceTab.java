package ekarus.s_attendance.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;

public class AttendanceTab extends  Fragment {
    Data_base_helper db;
    String code;
    String id;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.attendance_tab, container, false);
        //font add
        String fontPath = "fonts/MetaSerifPro-Book.otf";
        TextView txt_course = (TextView) rootView.findViewById(R.id.course_info);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        txt_course.setTypeface(tf);
        ///end

        Bundle bundle = getActivity().getIntent().getExtras();
        String stuff = bundle.getString("stuff");
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
        final String code2 =  code.trim();
        db= new Data_base_helper(getActivity());
        Cursor cursor = db.findCourse(code2);
            cursor.moveToFirst();
            TextView textView = (TextView) rootView.findViewById(R.id.course_info);
            String course="Course Name: ";
            course += cursor.getString(3)+"\n";
            course+="Semester: "+cursor.getString(1)+"\n";
            course+="Course Code: "+cursor.getString(2)+"\n";
            course+="Course Credit: "+cursor.getString(4)+"\n\n";
            textView.setText(course);
            db.close();
        id= "student_";
        for (int i = 0; i < stuff.length(); i++)
        {
            if(stuff.charAt(i)=='.')
            {
                break;
            }
            else
            {
                id+=stuff.charAt(i);
            }
        }

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),AttendanceProcessing.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuff", id);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        return rootView;

    }


}