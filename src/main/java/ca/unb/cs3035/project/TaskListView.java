package ca.unb.cs3035.project;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class TaskListView extends VBox {
    public TaskListView() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("task-list-vbox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

        MainApplication.tasksModel.taskList.addListener(new ListChangeListener<TaskList>() {
            @Override
            public void onChanged(Change<? extends TaskList> change) {
                try {
                    draw();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        MainApplication.tasksModel.selectedTaskList.addListener(new ChangeListener<TaskList>() {
            @Override
            public void changed(ObservableValue<? extends TaskList> observableValue, TaskList taskList, TaskList t1) {
                try {
                    draw();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        draw();
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();
//        try {
//            draw();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
//
    public void draw() throws IOException {
        //AnchorPane parent = (AnchorPane) this.getParent();
        //this.setPrefSize(parent.getWidth(), parent.getHeight());
        this.getChildren().clear();

        VBox vbox = this;

        MainApplication.tasksModel.taskList.forEach(new Consumer<TaskList>() {
            @Override
            public void accept(TaskList taskList) {
                try {
                    HBox newBtn = createNewTaskListButton();
                    TextField field = getTaskListButtonTextField(newBtn);
                    field.setText(taskList.taskListName.get());

                    taskList.taskListName.addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                            field.setText(t1);
                        }
                    });


                    if (MainApplication.tasksModel.selectedTaskList.get() == taskList) {
                        newBtn.getStyleClass().add("task-list-button-selected");
                    }
                    newBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            MainApplication.tasksModel.selectedTaskList.set(taskList);
                        }
                    });

                    vbox.getChildren().add(newBtn);
                    if (taskList.taskListName.get().isEmpty()) { // If the string is empty, we know it's a new one
                        renameField(taskList, newBtn, field, true);
                    }

                    createContextMenuForTaskListButton(newBtn, field, taskList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //System.out.println(this.getChildren().size());
    }

    void renameField(TaskList taskList, HBox btn, TextField field, boolean deleteIfNameEmpty) {
        String previousString = field.getText();
        field.setEditable(true);
        field.setDisable(false);
        field.requestFocus();
        btn.getStyleClass().add("task-list-button-editing");

        ChangeListener onFieldChanged = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean inFocus) {
                if (!inFocus) {
                    btn.getStyleClass().remove("task-list-button-editing");
                    field.setEditable(false);
                    field.setDisable(true);
                    if (field.getText().length() == 0) {
                        field.setText(previousString);
                        if (deleteIfNameEmpty) {
                            MainApplication.tasksModel.taskList.remove(taskList);
                        }
                    } else {
                        field.focusedProperty().removeListener(this);
                        taskList.setName(field.getText());
                    }
                } else {
                }
            }
        };
        field.focusedProperty().addListener(onFieldChanged);
        field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    field.getParent().requestFocus();
                }
            }
        });
    }

    ContextMenu createContextMenuForTaskListButton(HBox button, TextField field, TaskList taskList) {
        ContextMenu contextMenu = new ContextMenu();

        if (taskList.canRename) {
            MenuItem renameMenuItem = new MenuItem("Rename");
            renameMenuItem.setOnAction(action -> {
                renameField(taskList, button, field, false);
            });
            contextMenu.getItems().add(renameMenuItem);
        }

        if (taskList.canDelete) {
            MenuItem deleteMenuItem = new MenuItem("Delete");
            deleteMenuItem.setOnAction(action -> {
                MainApplication.tasksModel.taskList.remove(taskList);
                if (MainApplication.tasksModel.selectedTaskList.get() == taskList) {
                    MainApplication.tasksModel.selectedTaskList.set(MainApplication.tasksModel.taskList.get(0));
                }
            });
            contextMenu.getItems().add(deleteMenuItem);
        }

        button.setOnContextMenuRequested(e -> {
            contextMenu.show(MainApplication.scene.getWindow(), e.getScreenX(), e.getScreenY());
        });

        return contextMenu;
    }

    TextField getTaskListButtonTextField(HBox taskListButton) {
        return (TextField) taskListButton.getChildren().get(0);
    }

    HBox createNewTaskListButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("task-list-button.fxml"));
        HBox taskListButton = fxmlLoader.load();
        //this.getChildren().add(scene);
        TextField field = (TextField) taskListButton.getChildren().get(0);
        field.setText("");
        field.setPromptText("New Task List");
        field.setDisable(true);
        field.setEditable(false);

        return taskListButton;
    }
}
