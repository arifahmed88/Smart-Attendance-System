package ekarus.s_attendance.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;


public class Data_base_helper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "studytutorial";
    private static final int DATABASE_VERSION = 1;
    private HashMap hp;
    public String table_name = "user";
    public String table_name1= "course";


    public Data_base_helper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + table_name1 +
                        "(id integer primary key,semester text, course_code text, course_name text, course_credit text)"

        );
    }
///attendance
    public void insertData(String StudentId, String date, String attendance, String tableName){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("student_id", StudentId);
        contentValues.put("date", date);
        contentValues.put("attendance", attendance);
        db1.insert(tableName, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name1);
    }

    public Cursor getuser(String tableName) {
        Log.d("query", String.valueOf(tableName.length()));
        SQLiteDatabase db1 = this.getReadableDatabase();
        Log.d("query", tableName);
       Cursor res = db1.rawQuery("SELECT * FROM " + tableName , null);
        Log.d("query", tableName);
        res.moveToFirst();
        while(res.moveToNext());
      //Cursor res = null;

        return res;
    }

    public Cursor searchDatabase(String currentDate, String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String newCurrentdate =  currentDate + '%';
        String[] params = new String[]{ newCurrentdate };
        Cursor c = db.rawQuery("SELECT * FROM "+tableName+" WHERE date LIKE ?",
                params);

        while (c.moveToNext());
        return c;

    }

    public Cursor findAttendance(String atten, String date, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{date, atten};
        Cursor c = db.rawQuery("SELECT * FROM " +tableName + " WHERE date = ? AND student_id = ?",
                params);
        while (c.moveToNext()) ;
        return c;
    }
    public Cursor findAttendanceviadate(String date, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{date};
        Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE date = ?",
                params);
        while (c.moveToNext());
        return c;
    }

    ///end

    ///course

    public void insertCourseData(String semester ,String course_code ,String course_name, String course_credit){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("semester",semester );
        contentValues.put("course_code",course_code );
        contentValues.put("course_name", course_name);
        contentValues.put("course_credit", course_credit);
        db1.insert(table_name1, null, contentValues);
    }

    public Cursor viewCourse() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name1 + " ",
                null);
        res.moveToFirst();
        return res;
    }

    public Cursor findCourse(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] params = new String[]{code};
        Log.d("zz", String.valueOf(code.length()));
        Cursor c = db.rawQuery("SELECT * FROM " + table_name1 + " WHERE course_code = ?",
                params);
        c.moveToFirst();
        Log.d("zz", String.valueOf(c.getCount()));
        return c;
    }

    public void delete_course_data(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            String whereClause = "course_code=?";
            String whereArgs[] = {code};
            db.delete(table_name1, whereClause, whereArgs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }

        return ;
    }

    public void updateCourse(String id, String semester ,String course_code ,String course_name, String course_credit)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("aa", "joy");
        ContentValues contentValues = new ContentValues();
        contentValues.put("semester",semester );
        contentValues.put("course_code",course_code );
        contentValues.put("course_name", course_name);
        contentValues.put("course_credit", course_credit);
        String[] params = new String[]{id};
        Log.d("aa", "joy");
        db.update(table_name1, contentValues, " id="+id, null);
        Log.d("aa", "joy");
        Cursor c = viewCourse();
        c.moveToFirst();
        do {
            Log.d("aa", c.getString(0));
            Log.d("aa", c.getString(1));
            Log.d("aa", c.getString(2));
            Log.d("aa", c.getString(3));
            Log.d("aa", c.getString(4));
        }while(c.moveToNext());

    }

    public void createAttendancetable(String courseCode)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + courseCode +
                        "(id integer primary key, student_id text, date text, attendance text)"

        );

    }
    ///end
    public Cursor getAllData(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + tableName + " ",
                null);
        while(res.moveToNext())
        {
            Log.d("rr", res.getString(2));
        }

        return res;
    }
}
