package zagurskiy.fit.bstu.todolist.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import zagurskiy.fit.bstu.todolist.models.Task;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB.db";
    private static final int SCHEMA = 1;
    private static final String TABLE_NAME = "ParameterTable";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + " ("
                        + "id integer primary key autoincrement,"
                        + "date text not null,"
                        + "category text not null,"
                        + "description text not null,"
                        + "done text not null);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(SQLiteDatabase db, Task task) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", task.getId());
        contentValues.put("date", task.getDate().toString());
        contentValues.put("category", task.getCategory());
        contentValues.put("description", task.getDescription());
        contentValues.put("done",String.valueOf(task.isDone()));

        db.insert(TABLE_NAME, null, contentValues);
    }


    public int update(SQLiteDatabase db, Task task) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", task.getId());
        contentValues.put("date", task.getDate().toString());
        contentValues.put("category", task.getCategory());
        contentValues.put("description", task.getDescription());
        contentValues.put("done",String.valueOf(task.isDone()));

        return db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{task.getId().toString()});
    }

    public int delete(SQLiteDatabase db, String id) {
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    @SneakyThrows
    public List<Task> selectTasks(SQLiteDatabase db, String category, String date) {

        Cursor cursor = db.rawQuery("select id,date,category,description,done " +
                "from " + TABLE_NAME +
                " where (category = '" + category + "' and" +
                " date = '" + date + "') or (done='false' and date <'"+date+"')"
                , null);

        List<Task> tasks = new ArrayList<>();

        while (cursor.moveToNext()) {
            Task task = new Task();
            task.setId(Integer.parseInt
                    (cursor.getString(cursor.getColumnIndexOrThrow("id"))));
            task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
            task.setDate(LocalDate.parse
                    (cursor.getString(cursor.getColumnIndexOrThrow("date"))));
            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            task.setDone(Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("done"))));
//            task.setCalendarId(Integer.parseInt
//                    (cursor.getString(cursor.getColumnIndexOrThrow("calendarId"))));

            tasks.add(task);
        }

        cursor.close();

        return tasks;
    }
}
