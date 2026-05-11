package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.dao.ToDoListDAO;
import org.example.entity.ToDoList;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.service.ToDoService;
import org.example.utils.ChangeFXML;
import org.example.view.ToDoCell;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ToDoController implements Initializable {
    private final ToDoService todoService = new ToDoService();
    private final ObservableList<ToDoList> allTodos = FXCollections.observableArrayList();
    private String currentFilter = "ALL";

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label lblProgress;

    @FXML
    private TextField txtTitle;

    @FXML
    private ListView<ToDoList> listTodo;

    @FXML
    private VBox startDayBox;
    @FXML
    private VBox mainTodoBox;

    private User currentUser = UserSession.getCurrentUser();

    @FXML
    public void comeBackTodolist(MouseEvent event){
        ChangeFXML.changeFXML(event, "/view/home.fxml");
    }

    @FXML
    private void handleAdd() {
        String title = txtTitle.getText();
        if (title == null || title.isEmpty()) return;

        ToDoList todo = todoService.addTodo(title, currentUser);
        if (todo != null) {
            allTodos.add(todo);
            txtTitle.clear();
            updateProgress();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Không thể thêm công việc!");
            alert.show();
        }
    }

    @FXML
    private void filterAll() {
        currentFilter = "ALL";
        listTodo.setItems(allTodos);
    }

    @FXML
    private void filterDone() {
        currentFilter = "DONE";
        listTodo.setItems(allTodos.filtered(ToDoList::isCompleted));
    }

    @FXML
    private void filterUndone() {
        currentFilter = "UNDONE";
        listTodo.setItems(allTodos.filtered(t -> !t.isCompleted()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listTodo.setCellFactory(list -> new ToDoCell(todoService, () -> currentFilter, this::updateProgress));
        allTodos.clear();
        allTodos.addAll(ToDoListDAO.findByUserAndDate(currentUser, LocalDate.now()));
        listTodo.setItems(allTodos);
    }

    @FXML
    private void handleStartDay() {
        ToDoListDAO.carryOverPendingTasks( currentUser,LocalDate.now());
        startDayBox.setVisible(false);
        startDayBox.setManaged(false);
        mainTodoBox.setVisible(true);
        mainTodoBox.setManaged(true);

        allTodos.clear();
        allTodos.addAll(ToDoListDAO.findByUserAndDate(currentUser,LocalDate.now()));

        listTodo.setItems(allTodos);
        updateProgress();
    }

    private void updateProgress() {
        if (allTodos.isEmpty()) {
            progressBar.setProgress(0);
            lblProgress.setText("0%");
            return;
        }

        long doneCount = allTodos.stream()
                .filter(ToDoList::isCompleted)
                .count();

        double progress = (double) doneCount / allTodos.size();
        progressBar.setProgress(progress);

        int percent = (int) (progress * 100);
        lblProgress.setText(percent + "% (" + doneCount + "/" + allTodos.size() + ")");
    }
    @FXML
    private void handleEndDay() {
        long totalTasks = allTodos.size();
        List<ToDoList> pendingTasks = allTodos.stream()
                .filter(t -> !t.isCompleted())
                .toList();
        long completedCount = totalTasks - pendingTasks.size();

        double completionRate = (totalTasks == 0) ? 0 : (double) completedCount / totalTasks;
        int percent = (int) (completionRate * 100);


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tổng kết ngày làm việc");


        alert.setHeaderText("Hôm nay bạn đã hoàn thành " + percent + "% công việc.");

        if (pendingTasks.isEmpty() && totalTasks > 0) {
            alert.setContentText("Tuyệt vời! Bạn đã dọn dẹp sạch sẽ danh sách việc cần làm.");
        } else if (totalTasks == 0) {
            alert.setContentText("Hôm nay bạn chưa có công việc nào để tổng kết.");
        } else {
            alert.setContentText("Bạn vẫn còn " + pendingTasks.size() + " việc chưa xong. Bạn muốn làm gì?");
        }

        ButtonType btnTomorrow = new ButtonType("Chuyển sang mai");
        ButtonType btnKeep = new ButtonType("Để lại hôm nay");
        ButtonType btnCancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnTomorrow, btnKeep, btnCancel);


        alert.showAndWait().ifPresent(response -> {
            if (response == btnTomorrow) {
                for (ToDoList t : pendingTasks) {
                    t.setCreatedAt(LocalDate.now().plusDays(1));
                    todoService.update(t);
                }
                allTodos.removeAll(pendingTasks);
                updateProgress();

                showMotivationAlert();
            }
        });
    }

    // Hàm hiện thông báo khích lệ so sánh với hôm qua
    private void showMotivationAlert() {
        Alert motivation = new Alert(Alert.AlertType.INFORMATION);
        motivation.setTitle("CodeMate Coach");
        motivation.setHeaderText("🚀 Lời nhắn nhủ");
        motivation.setContentText("Hệ thống ghi nhận bạn đã rất nỗ lực. Hãy nghỉ ngơi và sẵn sàng cho ngày mai nhé!");
        motivation.show();
    }

}
