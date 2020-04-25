package JavaFX;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.IconView;
import javax.swing.text.View;
import java.util.*;
import java.util.function.Predicate;


public class List_View_Example extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        final String[] namen = new String[]{"Sarah", "Elias", "Lila", "Jan","Saskia","Hubert","Heinz","Herbert","Elb","Sandra","Eva","Julia"};
        final ObservableList<String> eintraege = FXCollections.observableArrayList(namen);
        final FilteredList<String>filteredList = new FilteredList<>(eintraege);
        final ListView<String> listView = new ListView<>(filteredList);
        final Button add = new Button("add");
        final TextField textField = new TextField();
        final TextField filterTxt = new TextField();
        final Button remove = new Button("remove");
        final ToolBar toolBar = new ToolBar(add, new Separator(), textField, new Separator(), remove, new Separator(), filterTxt);
        final String path = "ressource/pics/searchIcon.jpg";
        
        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.getChildren().addAll(toolBar, listView);

        // Funct 1: Dynamischer Suchfilter für Listeneinträge über Predicate<T> mit removeIf(true)

        filterTxt.setPromptText("Search for...");
        filterTxt.textProperty().addListener(((observable, oldValue, newValue) ->
        {
            final List<String> filteredEntries = new ArrayList<>(eintraege);
            final Predicate<String> caseInsensitivContains = entry -> {

                return entry.toUpperCase().contains(newValue.toUpperCase());
            };

            /* Funct 1: zur dynamischen Filterung über removeIf(Predicate<T>)

            filteredEntries.removeIf(caseInsensitivContains.negate());
            listView.setItems(FXCollections.observableArrayList(filteredEntries));
        */
            //Dynamischer Filter über FilteredList<T>

            filteredList.setPredicate(caseInsensitivContains);
        }
        ));

        // Action-Handling

        final SelectionModel<String> selectionModel = listView.getSelectionModel();
        add.setOnAction(event -> eintraege.add(textField.getText()));
        add.setOnAction(event -> {
            eintraege.add(textField.getText());
            listView.scrollTo(eintraege.size());
        });
        remove.setOnAction(event -> eintraege.remove(selectionModel.getSelectedItem()));

        // Usability steigern

        add.disableProperty().bind(textField.textProperty().isEmpty());
        remove.disableProperty().bind(Bindings.isNull(selectionModel.selectedItemProperty()));

        primaryStage.setScene(new Scene(vBox, 500, 200));
        primaryStage.setTitle(this.getClass().getSimpleName());
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
