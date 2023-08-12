package ca.unb.cs3035.project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TasksButton extends HBox {
    @FXML
    Label taskLabel;
    @FXML
    CheckBox checkbox;

    Task task;

    public TasksButton(Task task) throws IOException {
        this.task = task;
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("tasks-button.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

        taskLabel.setText(task.getTaskName());

        checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1 == true) {
                    task.completed.set(true);
                } else {
                    task.completed.set(false);
                }

                determineIfCompleted();
            }
        });

//        task.completed.addListener(a -> {
//            determineIfCompleted();
//        });


        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteButton = new MenuItem("Delete");

        contextMenu.getItems().add(deleteButton);

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Deleting task");
                task.taskList.tasks.remove(task);
            }
        });

        setOnContextMenuRequested(e -> {
            contextMenu.show(MainApplication.scene.getWindow(), e.getScreenX(), e.getScreenY());
        });

        determineIfCompleted();
    }

    void determineIfCompleted() {
        if (task.completed.get()) {
            getStyleClass().add("completed-task");
            checkbox.selectedProperty().set(true);
        } else {
            getStyleClass().remove("completed-task");
            checkbox.selectedProperty().set(false);
        }

    }
}
