package zagurskiy.fit.bstu.todolist.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Task implements Serializable {

    private Integer id;
//    private int calendarId;
    private String description;
    private String category;
    private LocalDate date;
    private boolean done;


    public boolean isDisplayed(LocalDate selectedDate) {
        if (selectedDate.equals(date)) return true;
        return selectedDate.equals(LocalDate.now());
    }
}
