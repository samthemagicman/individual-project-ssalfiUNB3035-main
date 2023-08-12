package ca.unb.cs3035.project;

import javafx.beans.property.SimpleBooleanProperty;

public class Task {
    private String taskName;
    public TaskList taskList;
    public SimpleBooleanProperty completed;

    public Task(String taskName, TaskList taskList) {
        this.taskList = taskList;
        this.taskName = taskName;
        this.completed = new SimpleBooleanProperty(false);
    }

    public String getTaskName() {
        return taskName;
    }
}
