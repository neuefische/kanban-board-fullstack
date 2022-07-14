package de.neuefische.muc.kanban.task;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;

@Getter
public class CsvTask {

    @CsvBindByName
    private String task;
    @CsvBindByName
    private String description;
    @CsvBindByName
    private String status;

}
