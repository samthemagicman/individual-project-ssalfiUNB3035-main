package ca.unb.cs3035.project;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class TasksModel {
    //public SimpleMapProperty<Integer, TaskList> taskList;
    public SimpleListProperty<TaskList> taskList;
    public SimpleObjectProperty<TaskList> selectedTaskList;

    public TasksModel() {
        ObservableList<TaskList> observableList = (ObservableList<TaskList>) FXCollections.observableArrayList(new ArrayList<TaskList>());

        taskList = new SimpleListProperty<TaskList>(observableList);

        TaskList allTasks = new TaskList("All Tasks", false, false, TaskList.SpecialAction.AllTasks);
        taskList.add(allTasks);

        selectedTaskList = new SimpleObjectProperty<>();
        selectedTaskList.set(allTasks);
    }

    public void addNewTaskList() {
        TaskList tList = new TaskList("", true, true, TaskList.SpecialAction.None);
        taskList.add(tList);
    }
}
