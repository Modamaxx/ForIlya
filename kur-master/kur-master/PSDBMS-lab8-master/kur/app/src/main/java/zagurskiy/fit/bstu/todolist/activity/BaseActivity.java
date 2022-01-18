package zagurskiy.fit.bstu.todolist.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import zagurskiy.fit.bstu.todolist.adapter.CustomListAdapter;
import zagurskiy.fit.bstu.todolist.R;
import zagurskiy.fit.bstu.todolist.MainActivity;
import zagurskiy.fit.bstu.todolist.models.Task;
import zagurskiy.fit.bstu.todolist.repository.DBHelper;
import zagurskiy.fit.bstu.todolist.repository.XMLHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class BaseActivity extends AppCompatActivity {

    TextView displayName;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button addTaskButton;
    LocalDate selectedDate;
    ActivityType type;
    TextView itemDate;
    public static List<Task> tasks;
    private List<Task> filteredTasks;
    private CustomListAdapter customListAdapter;
    ListView tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addTaskButton = findViewById(R.id.btnAddTaskWork);
        tasksList = findViewById(R.id.tasksListWork);
        type = (ActivityType) getIntent().getExtras().get("activityType");
        selectedDate = (LocalDate) getIntent().getExtras().get("selectedDate");
        itemDate = findViewById(R.id.data);
        itemDate.setText(selectedDate.toString());

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        setListener();
        setData();
    }

    private void setListener() {
        addTaskButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("activityType", type);
            startActivity(intent);
        });
    }

    private void setData() {
        String title = XMLHelper.getTitleByCategory(this, type.toString());

        tasks = dbHelper.selectTasks(db, title, selectedDate.toString());
        displayName = findViewById(R.id.displayName);
        displayName.setText(title);

        setFilteredTasks(selectedDate);
        customListAdapter = CustomListAdapter.builder()
                .tasks(filteredTasks)
                .context(this)
                .doneDate(false)
                .type(type)
                .build();
        tasksList.setAdapter(customListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void setFilteredTasks(LocalDate selectedDate) {
        filteredTasks = tasks.stream()
                .filter(t -> t.isDisplayed(selectedDate))
                .collect(Collectors.toList());
    }
}
