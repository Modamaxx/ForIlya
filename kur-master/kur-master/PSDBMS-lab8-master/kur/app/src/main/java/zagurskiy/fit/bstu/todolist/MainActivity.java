package zagurskiy.fit.bstu.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import zagurskiy.fit.bstu.todolist.activity.ApiActivity;
import zagurskiy.fit.bstu.todolist.activity.BaseActivity;
import zagurskiy.fit.bstu.todolist.repository.XMLHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class MainActivity extends AppCompatActivity {
    //View
    Button selectedDateButton;
    TextView selectedDate;
    LocalDate localDate;
    Calendar calendar;

    Button studyButton;
    Button workButton;
    Button sleepButton;
    Button meetingsButton;
    Button sportsButton;
    Button leisureButton;
    Button apiButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding();
        XMLHelper.writeXML(this);
        setListeners();
        setData();
    }

    private void binding() {
        selectedDateButton = findViewById(R.id.btnSelectDate);
        selectedDate = findViewById(R.id.txtSelectDate);
        calendar = Calendar.getInstance();

        studyButton = findViewById(R.id.studyButton);
        workButton = findViewById(R.id.workButton);
        sleepButton = findViewById(R.id.sleepButton);
        meetingsButton = findViewById(R.id.meetingsButton);
        sportsButton = findViewById(R.id.sportsButton);
        leisureButton = findViewById(R.id.leisureButton);
        apiButton = findViewById(R.id.apiButton);
    }

    private void setData() {
        try {
            localDate = LocalDate.parse(getIntent().getExtras().get("selectedDate").toString());

            calendar.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
            calendar.set(Calendar.MONTH, localDate.getMonthValue() - 1);
            calendar.set(Calendar.YEAR, localDate.getYear());
        } catch (Exception e) {
            localDate = Calendar.getInstance().getTime()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } finally {
            selectedDate.setText(localDate.toString());
        }
    }

    private void setListeners() {

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            localDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            selectedDate.setText(localDate.toString());
        };

        selectedDateButton.setOnClickListener(view ->
                new DatePickerDialog(MainActivity.this, d,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show());

        studyButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.STUDY);
            startActivity(intent);
        });
        workButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.WORK);
            startActivity(intent);
        });
        sleepButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.SLEEP);
            startActivity(intent);
        });
        meetingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.MEETING);
            startActivity(intent);
        });
        sportsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.SPORT);
            startActivity(intent);
        });
        leisureButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.LEISURE);
            startActivity(intent);
        });
        apiButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ApiActivity.class);
//            intent.putExtra("selectedDate", localDate);
//            intent.putExtra("activityType", ActivityType.LEISURE);
            startActivity(intent);
        });
    }
}
