package zagurskiy.fit.bstu.todolist.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityType {

    STUDY("Учеба"),
    WORK("Работа"),
    SLEEP("Сон"),
    MEETING("Встречи"),
    SPORT("Спорт"),
    LEISURE("Досуг");

    private String displayName;
}
