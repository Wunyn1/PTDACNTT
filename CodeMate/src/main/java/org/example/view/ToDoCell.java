package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import org.example.entity.ToDoList;
import org.example.service.ToDoService;

import java.util.function.Supplier;

public class ToDoCell  extends ListCell<ToDoList> {
    private final CheckBox checkBox = new CheckBox();
    private final Label lblTitle = new Label();
    private final TextField txtEdit = new TextField();
    private final Button btnEdit = createIconButton("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75z");
    private final Button btnDelete = createIconButton("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
    private final HBox container = new HBox(12);
    private final ToDoService todoService;
    private final Supplier<String> currentFilter;
    private final Runnable onDataChanged;

    public ToDoCell(ToDoService todoService, Supplier<String> currentFilter, Runnable onDataChanged) {
        this.todoService = todoService;
        this.currentFilter = currentFilter;
        this.onDataChanged = onDataChanged;
        initLayout();
        initEvents();
    }

    private void initLayout() {
        txtEdit.setVisible(false);
        txtEdit.setManaged(false);
        txtEdit.setStyle("-fx-background-color: #0f172a; -fx-text-fill: white; -fx-border-color: #3b82f6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actionBox = new HBox(8, btnEdit, btnDelete);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        container.getChildren().addAll(checkBox, lblTitle, txtEdit, spacer, actionBox);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-padding: 10 15; -fx-background-color: #1e293b; -fx-background-radius: 10; -fx-border-color: #334155; -fx-border-radius: 10;");
    }

    private void initEvents() {
        checkBox.setOnAction(e -> {
            if (getItem() != null) {
                todoService.setCompleted(getItem(), checkBox.isSelected());
                onDataChanged.run();
            }
        });
        btnDelete.setOnAction(e -> {
            if (getItem() != null) {
                todoService.delete(getItem());
                getListView().getItems().remove(getItem());
                onDataChanged.run();
            }
        });
        btnEdit.setOnAction(e -> {
            lblTitle.setManaged(false);
            lblTitle.setVisible(false);
            txtEdit.setManaged(true);
            txtEdit.setVisible(true);
            txtEdit.setText(getItem().getTitle());
            txtEdit.requestFocus();
        });
        txtEdit.setOnAction(e -> {
            getItem().setTitle(txtEdit.getText());
            todoService.update(getItem());
            lblTitle.setText(txtEdit.getText());
            txtEdit.setManaged(false);
            txtEdit.setVisible(false);
            lblTitle.setManaged(true);
            lblTitle.setVisible(true);
        });
    }

    @Override
    protected void updateItem(ToDoList item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setStyle("-fx-background-color: transparent;");
        } else {
            lblTitle.setText(item.getTitle());
            checkBox.setSelected(item.isCompleted());
            if (item.isCompleted()) {
                lblTitle.setStyle("-fx-text-fill: #64748b; -fx-strikethrough: true; -fx-font-size: 14px;");
            } else {
                lblTitle.setStyle("-fx-text-fill: white; -fx-strikethrough: false; -fx-font-size: 14px;");
            }
            setGraphic(container);
            setStyle("-fx-background-color: transparent; -fx-padding: 5 0;");
        }
    }

    private Button createIconButton(String svg) {
        SVGPath path = new SVGPath();
        path.setContent(svg);
        path.setStyle("-fx-fill: #94a3b8;");
        path.setScaleX(1.2);
        path.setScaleY(1.2);
        Button b = new Button();
        b.setGraphic(path);
        b.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        return b;
    }
}
