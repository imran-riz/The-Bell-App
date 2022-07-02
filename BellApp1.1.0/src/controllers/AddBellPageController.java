/*
 * Copyright (c) 2021 by Imran R.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import model.Bell;
import util.BellCollection;
import util.IDGenerator;
import util.PageNavigator;
import values.Repeat;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * The controller class to AddBellPage.fxml
 * On a Scene it will show a number of text fields and choice boxes requesting for necessary info to
 * schedule a Bell. Here all entries are validated.
 */
public class AddBellPageController implements Initializable {

    public AnchorPane root ;
    public VBox mainVBox ;
    public Label header, warningLbl ;
    public Button backBtn, addBtn, searchBtn ;
    public TextField periodField, descriptionField, audioFileField;
    public ChoiceBox<String> startChoiceBox, endChoiceBox, periodChoiceBox ;
    public ComboBox<String> startHr, startMin, endHr, endMin, repeatBox;
    public CheckBox checkBox ;
    public Rectangle rectangle ;

    private final ImageView backImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/back_arrow_icon.png")).toString())) ;
    private final ImageView searchImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/search_icon.png")).toString())) ;
    private final Alert alert = new Alert(Alert.AlertType.ERROR) ;

    private final String AUDIO_FILE_INSTRUCTION = "pick an audio file..." ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.rectangle.setFill(Color.valueOf("#5d6064"));

        this.searchImageView.setFitWidth(15.0) ;
        this.searchImageView.setFitHeight(15.0) ;

        this.backBtn.setShape(new Circle(5.0)) ;
        this.backBtn.setGraphic(backImageView) ;

        this.searchBtn.setGraphic(this.searchImageView) ;

        this.audioFileField.setText(this.AUDIO_FILE_INSTRUCTION);
        this.audioFileField.setOnMouseClicked(e -> this.openFileChooser());

        initialiseBoxes() ;

        this.checkBox.setOnAction(e -> {
            if(this.checkBox.isSelected()) {
                this.endHr.setDisable(true) ;
                this.endMin.setDisable(true) ;
                this.endChoiceBox.setDisable(true) ;
                this.periodField.setDisable(true) ;
                this.periodChoiceBox.setDisable(true) ;
            }
            else {
                this.endHr.setDisable(false) ;
                this.endMin.setDisable(false) ;
                this.endChoiceBox.setDisable(false) ;
                this.periodField.setDisable(false) ;
                this.periodChoiceBox.setDisable(false) ;
            }
        }) ;

        this.root.setId("root_style") ;
        this.header.setId("header_style") ;
        this.warningLbl.setId("warning_lbl_style");
        this.backBtn.setId("back_btn_style") ;
        this.addBtn.setId("add_btn_style") ;
    }


    /**
     * Method to initialise the ChoiceBoxes and ComboBoxes in the application to allow
     * user interaction.
     */
    private void initialiseBoxes() {

        ObservableList<String> list1 = FXCollections.observableArrayList() ;
        list1.addAll("AM", "PM") ;

        ObservableList<String> list2 = FXCollections.observableArrayList() ;
        list2.addAll("Minutes", "Hours") ;

        ObservableList<String> listOfHours = FXCollections.observableArrayList() ;
        listOfHours.addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12") ;

        ObservableList<String> listOfRepeatValues = FXCollections.observableArrayList() ;
        listOfRepeatValues.addAll(Repeat.NONE, Repeat.WEEKDAYS, Repeat.EVERY_MONDAY, Repeat.EVERY_TUESDAY, Repeat.EVERY_WEDNESDAY, Repeat.EVERY_THURSDAY, Repeat.EVERY_FRIDAY) ;

        ObservableList<String> listOfNum = FXCollections.observableArrayList() ;
        for(int counter = 0 ; counter < 60 ; counter++) {
            String temp ;

            if(counter < 10)
                temp = "0" + counter;
            else
                temp = Integer.toString(counter) ;

            listOfNum.add(temp) ;
        }

        // set the items
        this.startHr.setItems(listOfHours) ;
        this.startMin.setItems(listOfNum) ;
        this.endHr.setItems(listOfHours) ;
        this.endMin.setItems(listOfNum) ;
        this.startChoiceBox.setItems(list1);
        this.endChoiceBox.setItems(list1);
        this.periodChoiceBox.setItems(list2);
        this.repeatBox.setItems(listOfRepeatValues) ;

        this.startHr.setVisibleRowCount(10) ;
        this.startMin.setVisibleRowCount(10) ;
        this.endHr.setVisibleRowCount(10) ;
        this.endMin.setVisibleRowCount(10) ;
        this.repeatBox.setVisibleRowCount(3) ;

        this.startHr.setValue("Hr") ;
        this.startMin.setValue("Min") ;
        this.endHr.setValue("Hr") ;
        this.endMin.setValue("Min") ;
        this.startChoiceBox.setValue("AM") ;
        this.endChoiceBox.setValue("AM") ;
        this.periodChoiceBox.setValue("Minutes") ;
        this.repeatBox.setValue(Repeat.NONE);
    }


    /**
     * Method to get the starting time that's set
     * @return The starting time in the 24-hour format HH:MM
     */
    private String getStartTime() {

        String startTime ;
        int h = Integer.parseInt(this.startHr.getValue()) ;

        // set the starting time
        if(this.startChoiceBox.getValue().equals("PM")) {
            if(!this.startHr.getValue().equals("12")) {
                h+=12 ;
            }

            startTime = h + ":" + this.startMin.getValue() ;
        }
        else {
            if(this.startHr.getValue().equals("12")) {
                startTime = "00" + ":" + this.startMin.getValue();
            }
            else {
                startTime = this.startHr.getValue() + ":" + this.startMin.getValue();
            }
        }

        return  startTime ;
    }


    /**
     * Method to get the ending time that's set
     * @return The end time in the 24-hour format HH:MM
     */
    private String getEndTime() {

        String endTime ;
        int h = Integer.parseInt(this.endHr.getValue()) ;

        if(this.endChoiceBox.getValue().equals("PM")) {
            if(!this.endHr.getValue().equals("12")) {
                h+=12 ;
            }

            endTime = h + ":" + this.endMin.getValue() ;
        }
        else {
            if(this.endHr.getValue().equals("12")) {
                endTime = "00" + ":" + this.endMin.getValue() ;
            }
            else {
                endTime = this.endHr.getValue() + ":" + this.endMin.getValue();
            }
        }

        return endTime ;
    }


    /**
     * @param hours the hours in the 24-hour format HH:MM to be converted
     * @return the hours in seconds
     */
    private Integer hoursToSeconds(String hours) {

        int inSeconds = 0 ;

        inSeconds = inSeconds + (Integer.parseInt(hours.substring(0, 2)) * 60 * 60) ;       // convert the hour part
        inSeconds = inSeconds + (Integer.parseInt(hours.substring(3, 5)) * 60) ;            // convert the minute part

        return inSeconds ;
    }


    /**
     * Checks if the start time is valid given an ending time
     * @return true if valid else false
     */
    private Boolean validateTimes() {

        boolean valid = true ;
        int startTimeInSeconds = hoursToSeconds(getStartTime()) ;
        int endTimeInSeconds = hoursToSeconds(getEndTime()) ;

        if(endTimeInSeconds <= startTimeInSeconds) {
            valid = false ;
        }

        return valid ;
    }


    public void goBack() {
        this.resetEntries();
        PageNavigator.activate("HomePage") ;
    }


    /**
     * Method to start the timer or cancel it depending on the current state
     */
    public void add() {

        String repeatBoxValue = this.repeatBox.getValue() ;

        if (!this.checkBox.isSelected()) {

            if (this.descriptionField.getText().isBlank() || this.audioFileField.getText().equals(this.AUDIO_FILE_INSTRUCTION) || this.startHr.getValue().equals("Hr") || this.startMin.getValue().equals("Min") || this.endHr.getValue().equals("Hr") || this.endMin.getValue().equals("Min") || this.periodField.getText().isBlank()) {
                this.alert.getDialogPane().setContent(new Label("All the details must be filled, times must be set and an audio file must be chosen!"));
                this.alert.showAndWait();
            }
            else if (!validateMediaFile()) {
                this.alert.getDialogPane().setContent(new Label("Only .mp3 and .wav files are allowed! Pick another audio file."));
                this.alert.showAndWait();
            }
            else {
                try {
                    double period ;       // must be in seconds

                    period = Double.parseDouble(this.periodField.getText());

                    if (this.periodChoiceBox.getValue().equals("Minutes")) {
                        period = period * 60;
                    }
                    else {
                        period = (period * 60) * 60;
                    }

                    if (validateTimes()) {
                        Bell bell = new Bell(generateBellID(), getStartTime(), getEndTime(), period, repeatBoxValue, this.audioFileField.getText(), this.descriptionField.getText()) ;

                        BellCollection.getInstance().addNewBell(bell);

                        HomePageController.addBell(bell);

                        this.resetEntries() ;

                        PageNavigator.activate("HomePage") ;
                    }
                    else {
                        this.alert.getDialogPane().setContent(new Label("The times entered are not valid."));
                        this.alert.showAndWait();
                    }
                }
                catch (Exception e) {
                    this.alert.getDialogPane().setContent(new Label("Oops! Something went wrong. Please check the details you've entered and try again."));
                    this.alert.showAndWait();

                    e.printStackTrace();
                }
            }
        }
        else {
            if (this.descriptionField.getText().isBlank() || this.audioFileField.getText().equals(this.AUDIO_FILE_INSTRUCTION) || this.startHr.getValue().equals("Hr") || this.startMin.getValue().equals("Min")) {
                this.alert.getDialogPane().setContent(new Label("All the required details must be filled!"));
                this.alert.showAndWait();
            }
            else {
                if (validateMediaFile()) {
                    try {
                        Bell bell = new Bell(generateBellID(), getStartTime(), "", 0, repeatBoxValue, this.audioFileField.getText(), this.descriptionField.getText()) ;

                        BellCollection.getInstance().addNewBell(bell);

                        HomePageController.addBell(bell) ;

                        this.resetEntries() ;
                        PageNavigator.activate("HomePage");
                    }
                    catch (Exception e) {
                        this.alert.getDialogPane().setContent(new Label("Oops! Something went wrong. Please check the details you've entered and try again."));
                        this.alert.showAndWait();

                        e.printStackTrace();
                    }
                }
                else {
                    this.alert.getDialogPane().setContent(new Label("Only .mp3 and .wav files are allowed! Pick another audio file."));
                    this.alert.showAndWait();
                }
            }
        }

    }


    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser() ;
        fileChooser.setTitle("Browse media file") ;

        File mediaFile = fileChooser.showOpenDialog(this.root.getScene().getWindow()) ;
        String filePath = this.AUDIO_FILE_INSTRUCTION ;

        if(mediaFile != null)   filePath = mediaFile.getPath() ;

        this.audioFileField.setText(filePath) ;
    }


    private void resetEntries() {
        this.checkBox.setSelected(false);
        this.descriptionField.setText("");
        this.audioFileField.setText(this.AUDIO_FILE_INSTRUCTION);
        this.startHr.setValue("Hr");
        this.startMin.setValue("Min");
        this.endHr.setValue("Hr");
        this.endMin.setValue("Min");
        this.periodField.setText("");
        this.periodChoiceBox.setValue("Minutes");
        this.repeatBox.setValue(Repeat.NONE);
    }


    /**
     * Checks the file extension of the picked audio file and validates it
     * @return true if valid (.wav or .mp3), otherwise false
     */
    private boolean validateMediaFile() {

        boolean valid = false;
        String filePath = this.audioFileField.getText();
        String fileExtension = filePath.substring(filePath.lastIndexOf("."));

        if (fileExtension.equalsIgnoreCase(".mp3") || fileExtension.equalsIgnoreCase(".wav")) {
            valid = true;
        }

        return valid;
    }


    private String generateBellID() {

        String id;
        int counter = 0;
        boolean found;

        do {
            id = IDGenerator.generateID(2, 4);
            found = false;

            while (!found && counter < BellCollection.getInstance().getListOfBells().size()) {
                if (BellCollection.getInstance().getListOfBells().get(counter).getBellID().equals(id))
                    found = true;

                counter++;
            }
        }
        while (found);

        return id;
    }
}