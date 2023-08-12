package ca.unb.cs3035.project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController {
    @FXML
    VBox tasksList;
    @FXML
    AnchorPane taskListAnchorPane;
    @FXML
    AnchorPane tasksAnchorPane;
    @FXML
    Label selectedTasksListHeader;
    @FXML
    Button addTaskButton;
    @FXML
    TextField addTaskField;
    @FXML
    MenuItem aboutMenuItem;
    @FXML
    MenuItem helpMenuItem;
    @FXML
    MenuItem closeMenuItem;
    @FXML
            MenuItem markAllTasksCompleteMenuItem;
    @FXML
            MenuItem deleteAllTasksMenuItem;

    ChangeListener listener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            selectedTasksListHeader.setText(t1);
        }
    };

    public void initialize() throws IOException {
        // This is the pre-made data according to the requirements
        TaskList tList = new TaskList("Grocery List", true, true, TaskList.SpecialAction.None);
        MainApplication.tasksModel.taskList.add(tList);
        tList.tasks.add(new Task("Milk", tList));
        tList.tasks.add(new Task("Eggs", tList));
        tList.tasks.add(new Task("Eggos", tList));
        tList.tasks.add(new Task("Lobster", tList));

        TaskList tList2 = new TaskList("School Assignments", true, true, TaskList.SpecialAction.None);
        MainApplication.tasksModel.taskList.add(tList2);
        tList2.tasks.add(new Task("CS3997 Assignment 2", tList2));
        Task t1 = new Task("CS3035 Individual Project", tList2);
        t1.completed.set(true);
        tList2.tasks.add(t1);


        TaskListView taskListView = new TaskListView();
        taskListAnchorPane.getChildren().clear();
        taskListAnchorPane.getChildren().add(taskListView);

        TasksView taskView = new TasksView();
        tasksAnchorPane.getChildren().clear();
        tasksAnchorPane.getChildren().add(taskView);

        selectedTasksListHeader.setText("All Tasks");

        MainApplication.tasksModel.selectedTaskList.addListener(new ChangeListener<TaskList>() {
            @Override
            public void changed(ObservableValue<? extends TaskList> observableValue, TaskList taskList, TaskList currentTaskList) {
                selectedTasksListHeader.setText(currentTaskList.getName());

                taskList.taskListName.removeListener(listener);

                currentTaskList.taskListName.addListener(listener);
            }
        });

        addTaskButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createNewTask();
            }
        });

        addTaskField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    createNewTask();
                }
            }
        });

        closeMenuItem.setOnAction(a -> {
            Stage st = (Stage) MainApplication.scene.getWindow();
            st.close();
        });

        helpMenuItem.setOnAction(a -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("help-scene.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(MainApplication.scene.getWindow());
                stage.setResizable(false);
                stage.setTitle("Help");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        aboutMenuItem.setOnAction(a -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about-scene.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(MainApplication.scene.getWindow());
                stage.setResizable(false);
                stage.setTitle("About");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        deleteAllTasksMenuItem.setOnAction(a -> {
            if (MainApplication.tasksModel.selectedTaskList.get().specialAction == TaskList.SpecialAction.AllTasks) {
                for (TaskList tk : MainApplication.tasksModel.taskList) {
                    if (tk != MainApplication.tasksModel.selectedTaskList.get()) {

                        tk.tasks.clear();
                    }
                }
                MainApplication.tasksModel.selectedTaskList.get().tasks.clear();
            } else {
                MainApplication.tasksModel.selectedTaskList.get().tasks.clear();
            }
        });

        markAllTasksCompleteMenuItem.setOnAction(a -> {
            if (MainApplication.tasksModel.selectedTaskList.get().specialAction == TaskList.SpecialAction.AllTasks) {
                for (TaskList tk : MainApplication.tasksModel.taskList) {
                    for (Task task : tk.tasks) {
                        task.completed.set(true);
                    }
                }
            } else {
                for (Task task : MainApplication.tasksModel.selectedTaskList.get().tasks) {
                    task.completed.set(true);
                }
            }
        });

    }

    private void createNewTask() {
        String text = addTaskField.getText();
        if (!text.isEmpty()) {
            addTaskField.setText("");
            Task task = new Task(text, MainApplication.tasksModel.selectedTaskList.get());
            MainApplication.tasksModel.selectedTaskList.get().tasks.add(task);
        }
    }

    @FXML
    protected void onNewTaskListButtonClicked() {
        MainApplication.tasksModel.addNewTaskList();
    }


}