package zagurskiy.fit.bstu.todolist.activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

import zagurskiy.fit.bstu.todolist.MainActivity;
import zagurskiy.fit.bstu.todolist.R;
import zagurskiy.fit.bstu.todolist.models.Task;
import zagurskiy.fit.bstu.todolist.repository.DBHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class AddTaskActivity extends AppCompatActivity {

    ActionBar actionBar;
    EditText description;
    TextView date;
    Button save;

    DBHelper dbHelper;
    SQLiteDatabase db;
    String displayCategory;
    LocalDate taskDate;
    ActivityType activityType;
    Task editedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        binding();
        setListeners();
        setData();
    }

    private void binding() {
        actionBar = getSupportActionBar();
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        save = findViewById(R.id.btnSave);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    private void setData() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        editedTask = null;

        Intent intent = getIntent();
        taskDate = LocalDate.parse(intent.getExtras().get("selectedDate").toString());
        this.date.setText("Дата: " + taskDate);

        editedTask = (Task) intent.getSerializableExtra("taskToEdit");
        if (editedTask == null) return;

        description.setText(editedTask.getDescription());
    }

    private void setListeners() {
        save.setOnClickListener(view -> {
            String descriptionTask = description.getText().toString();
            if (descriptionTask.length() == 0) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent backActivityIntent = getIntent();
            activityType = (ActivityType) backActivityIntent.getExtras().get("activityType");
            displayCategory = activityType.getDisplayName();

            if (editedTask != null) {
                editedTask.setDescription(description.getText().toString());
                dbHelper.update(db, editedTask);
                providerUpdate(editedTask);
            } else {
                Task task = Task.builder()
                        .description(descriptionTask)
                        .category(displayCategory)
                        .date(taskDate)
                        .done(false)
                        .build();
                task = providerAdd(task);
                dbHelper.insert(db, task);
            }
            Intent intentActivity = new Intent(this, BaseActivity.class);
            intentActivity.putExtra("selectedDate", taskDate);
            intentActivity.putExtra("activityType", activityType);
            startActivity(intentActivity);
        });
    }

    public Task providerAdd(Task task) {
        Cursor cursorId = db.rawQuery("select count(*) from ParameterTable", null);
        cursorId.moveToFirst();
        task.setId(cursorId.getInt(0) + 1);

        LocalDate date = task.getDate();
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), 11, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), 12, 30);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, task.getCategory());

        values.put(CalendarContract.Events.CALENDAR_ID, task.getId());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        task.setId(Integer.parseInt(uri.getLastPathSegment()));
        return task;
    }

    public void providerUpdate(Task task) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DESCRIPTION, task.getDescription());
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, task.getId());
        cr.update(updateUri, values, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("selectedDate", taskDate);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}