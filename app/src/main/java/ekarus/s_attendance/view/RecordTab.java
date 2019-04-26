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

import static android.R.attr.data;

public class RecordTab extends Fragment {

    Data_base_helper db1;
    String course_code;
   String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.record_tab, container, false);
        db1 = new Data_base_helper(getActivity());
        //font add
        String fontPath = "fonts/herofont.ttf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        Button bt1 = (Button) rootView.findViewById(R.id.btn1);
        Button bt2 = (Button) rootView.findViewById(R.id.btn2);
        bt1.setTypeface(tf);
        bt2.setTypeface(tf);
        ///end
        Bundle bundle = getActivity().getIntent().getExtras();
        final String stuff = bundle.getString("stuff");
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

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ron",stuff);
                Intent intent= new Intent(getActivity(),Individual_attendance.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuff", id);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ron1",stuff);
                Intent intent= new Intent(getActivity(),FullAttendanceShow.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuff", id);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        return rootView;
    }

}

