package ekarus.s_attendance.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ekarus.s_attendance.R;
import ekarus.s_attendance.model.Data_base_helper;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> data = new ArrayList<String>();
    Data_base_helper db;
    String code_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //font add
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/herofont.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView myTitle = (TextView) toolbar.getChildAt(0);
        myTitle.setTypeface(tf);

        ///end

        ListView lv = (ListView) findViewById(R.id.listview);
        generateListContent();
        lv.setAdapter(new MyListAdaper(this, R.layout.main_activity_course_info, data));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, AddCourse.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private boolean generateListContent() {
        db= new Data_base_helper(this);
        Cursor cursor = db.viewCourse();
        cursor.moveToFirst();
        if(cursor.getCount()==0)
        {
           return false;
        }
        else
        {
            cursor.moveToFirst();
            do {
                String course=cursor.getString(0)+". "+cursor.getString(2)+", "+cursor.getString(1)+" Semester, " +cursor.getString(4)+" Credit";
                data.add(course);
            }while(cursor.moveToNext());
            db.close();
            return true;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent;
            intent = new Intent(MainActivity.this,AddCourse.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private String st;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
                Typeface childFont = Typeface.createFromAsset(getAssets(), "fonts/MetaSerifPro-Book.otf");
                viewHolder.title.setTypeface(childFont);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(MainActivity.this,Course.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("stuff",data.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }
    public class ViewHolder {
        TextView title;
    }


    ///end
}
