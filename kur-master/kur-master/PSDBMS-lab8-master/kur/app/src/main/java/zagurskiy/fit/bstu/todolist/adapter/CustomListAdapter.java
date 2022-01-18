package zagurskiy.fit.bstu.todolist.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zagurskiy.fit.bstu.todolist.R;
import zagurskiy.fit.bstu.todolist.activity.AddTaskActivity;
import zagurskiy.fit.bstu.todolist.models.Task;
import zagurskiy.fit.bstu.todolist.repository.DBHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomListAdapter extends BaseAdapter {

    private List<Task> tasks;
    private Context context;

    private boolean doneDate;
    private ActivityType type;

    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_item, null);

        TextView itemDescription = view.findViewById(R.id.itemDescription);
        ImageButton editItem = view.findViewById(R.id.btnEditItem);
        ImageButton deleteItem = view.findViewById(R.id.btnDeleteItem);

        CheckBox checkItem = view.findViewById(R.id.check);

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        editItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra("selectedDate", tasks.get(position).getDate());
            intent.putExtra("activityType", type);
            intent.putExtra("taskToEdit", tasks.get(position));
            context.startActivity(intent);
        });

        deleteItem.setOnClickListener(v -> {
            Task deleteTask = tasks.get(position);
            dbHelper.delete(db, deleteTask.getId().toString());

            context.getApplicationContext().getContentResolver();
            ContentResolver cr = context.getContentResolver();

            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, deleteTask.getId());
            cr.delete(deleteUri, null, null);
            tasks.remove(deleteTask);

            this.notifyDataSetChanged();
        });
        checkItem.setOnClickListener(v -> {
            Task task = tasks.get(position);
            task.setDone(checkItem.isChecked());

            dbHelper.update(db, task);
            this.notifyDataSetChanged();
        });
        checkItem.setChecked(tasks.get(position).isDone());
        itemDescription.setText(tasks.get(position).getDescription());
        return view;
    }
}
