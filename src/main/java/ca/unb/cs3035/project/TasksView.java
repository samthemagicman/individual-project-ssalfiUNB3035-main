package ca.unb.cs3035.project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

public class TasksView extends VBox {
    ChangeListener listener = new ChangeListener<ObservableList<Task>>() {
        @Override
        public void changed(ObservableValue<? extends ObservableList<Task>> observableValue, ObservableList<Task> tasks, ObservableList<Task> t1) {
            try {
                draw();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    HashMap<Task, ChangeListener> taskCompletedListeners = new HashMap<>();

    public void initialize() throws IOException {
        MainApplication.tasksModel.selectedTaskList.get().tasks.addListener(listener);
        draw();
    }

    public TasksView() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("tasks-vbox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

        MainApplication.tasksModel.selectedTaskList.addListener(new ChangeListener<TaskList>() {
            @Override
            public void changed(ObservableValue<? extends TaskList> observableValue, TaskList taskList, TaskList t1) {
                try {
                    if (taskList != null) {
                        taskList.tasks.removeListener(listener);
                    }
                    draw();
                    t1.tasks.addListener(listener);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void draw() throws IOException {
        this.getChildren().clear();

        if (MainApplication.tasksModel.selectedTaskList.get().specialAction == TaskList.SpecialAction.AllTasks) {
            for (TaskList taskList : MainApplication.tasksModel.taskList) {
                for (Task task : taskList.tasks) {
                    addTaskButton(task);
                }
            }
        } else {
            for (Task task : MainApplication.tasksModel.selectedTaskList.get().tasks) {
                addTaskButton(task);
            }
        }
    }

    void addTaskButton(Task task) throws IOException {
        TasksButton taskButton = new TasksButton(task);


        if (task.completed.get()) {
            this.getChildren().add(this.getChildren().size(), taskButton);
        } else {
            this.getChildren().add(0, taskButton);
        }

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                try {
                    draw();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        if (taskCompletedListeners.containsKey(task)) {
            task.completed.removeListener(taskCompletedListeners.get(task));
        }

        taskCompletedListeners.put(task, listener);
        task.completed.addListener(listener);


//        taskButton.checkbox.selectedProperty().addListener(a -> {
//            if (taskButton.checkbox.selectedProperty().get()) {
//                task.completed.set(true);
//            } else {
//                task.completed.set(false);
//            }
//            try {
//                draw();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }
}
