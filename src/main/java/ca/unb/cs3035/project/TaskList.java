package ca.unb.cs3035.project;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TaskList {
    public SimpleStringProperty taskListName;
    public SimpleListProperty<Task> tasks;
    public final boolean canRename;
    public final boolean canDelete;

    public enum SpecialAction {
        None,
        AllTasks
    }

    public SpecialAction specialAction;

    public TaskList(String taskListName, boolean canRename, boolean canDelete, SpecialAction specialAction) {
        this.canRename = canRename;
        this.canDelete = canDelete;
        this.specialAction = specialAction;
        this.taskListName = new SimpleStringProperty(taskListName);

        ObservableList<Task> observableList = (ObservableList<Task>) FXCollections.observableArrayList(new ArrayList<Task>());
        this.tasks = new SimpleListProperty<Task>(observableList);
    }

    public void setName(String name) {
        taskListName.set(name);
    }

    public String getName() {
        return taskListName.get();
    }
}
