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


package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import model.BellCollection;
import util.IDGenerator;
import values.Repeat;

/**
 * The controller class to AddBellView.fxml
 * On a Scene it will show a number of text fields and choice boxes requesting for necessary info to
 * schedule a Bell. Here all entries are validated.
 */
public class AddBellViewController implements Initializable
{
    public AnchorPane root ;
    public VBox mainVBox ;
    public Label header, warningLbl ;
    public Button addBtn, searchBtn ;
    public TextField periodField, descriptionField, audioFileField;
    public ChoiceBox<String> startChoiceBox, endChoiceBox, periodChoiceBox ;
    public ComboBox<String> startHr, startMin, endHr, endMin, repeatBox;
    public CheckBox checkBox ;

    private final MessageBox messageBox = new MessageBox() ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/search_icon.png")).toString()));
        imageView.setFitWidth(15.0) ;
        imageView.setFitHeight(15.0) ;

        searchBtn.setGraphic(imageView) ;

        initialiseBoxes() ;

        checkBox.setOnAction(e ->
        {
            if(checkBox.isSelected())
            {
                endHr.setDisable(true) ;
                endMin.setDisable(true) ;
                endChoiceBox.setDisable(true) ;
                periodField.setDisable(true) ;
                periodChoiceBox.setDisable(true) ;
            }
            else
            {
                endHr.setDisable(false) ;
                endMin.setDisable(false) ;
                endChoiceBox.setDisable(false) ;
                periodField.setDisable(false) ;
                periodChoiceBox.setDisable(false) ;
            }
        }) ;

        root.setId("root_style") ;
        header.setId("header_style") ;
        warningLbl.setId("warning_lbl_style");
    }


    /**
     * Method to initialise the ChoiceBoxes and ComboBoxes in the application to allow
     * user interaction.
     */
    private void initialiseBoxes()
    {
        ObservableList<String> list1 = FXCollections.observableArrayList() ;
        list1.addAll("AM", "PM") ;

        ObservableList<String> list2 = FXCollections.observableArrayList() ;
        list2.addAll("Minutes", "Hours") ;

        ObservableList<String> listOfHours = FXCollections.observableArrayList() ;
        listOfHours.addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12") ;

        ObservableList<String> listOfRepeatValues = FXCollections.observableArrayList() ;
        listOfRepeatValues.addAll(Repeat.NONE, Repeat.WEEKDAYS, Repeat.EVERY_MONDAY, Repeat.EVERY_TUESDAY, Repeat.EVERY_WEDNESDAY, Repeat.EVERY_THURSDAY, Repeat.EVERY_FRIDAY) ;

        ObservableList<String> listOfNum = FXCollections.observableArrayList() ;
        for(int counter = 0 ; counter < 60 ; counter++)
        {
            String temp ;

            if(counter < 10)
                temp = "0" + counter;
            else
                temp = Integer.toString(counter) ;

            listOfNum.add(temp) ;
        }

        // set the items
        startHr.setItems(listOfHours) ;
        startMin.setItems(listOfNum) ;
        endHr.setItems(listOfHours) ;
        endMin.setItems(listOfNum) ;
        startChoiceBox.setItems(list1);
        endChoiceBox.setItems(list1);
        periodChoiceBox.setItems(list2);
        repeatBox.setItems(listOfRepeatValues) ;

        startHr.setVisibleRowCount(10) ;
        startMin.setVisibleRowCount(10) ;
        endHr.setVisibleRowCount(10) ;
        endMin.setVisibleRowCount(10) ;
        repeatBox.setVisibleRowCount(3) ;

        startHr.setValue("Hr") ;
        startMin.setValue("Min") ;
        endHr.setValue("Hr") ;
        endMin.setValue("Min") ;
        startChoiceBox.setValue("AM") ;
        endChoiceBox.setValue("AM") ;
        periodChoiceBox.setValue("Minutes") ;
        repeatBox.setValue(Repeat.NONE);
    }


    /**
     * Method to get the starting time that's set
     * @return The starting time in the 24-hour format HH:MM
     */
    private String getStartTime()
    {
        String startTime ;
        int h = Integer.parseInt(startHr.getValue()) ;

        // set the starting time
        if(startChoiceBox.getValue().equals("PM"))
        {
            if(!startHr.getValue().equals("12"))
            {
                h+=12 ;
            }

            startTime = h + ":" + startMin.getValue() ;
        }
        else
        {
            if(startHr.getValue().equals("12"))
                startTime = "00" + ":" + startMin.getValue() ;
            else
                startTime = startHr.getValue() + ":" + startMin.getValue() ;
        }

        return  startTime ;
    }


    /**
     * Method to get the ending time that's set
     * @return The end time in the 24-hour format HH:MM
     */
    private String getEndTime()
    {
        String endTime ;
        int h = Integer.parseInt(endHr.getValue()) ;

        if(endChoiceBox.getValue().equals("PM"))
        {
            if(!endHr.getValue().equals("12"))
            {
                h+=12 ;
            }

            endTime = h + ":" + endMin.getValue() ;
        }
        else
        {
            if(endHr.getValue().equals("12"))
                endTime = "00" + ":" + endMin.getValue() ;
            else
                endTime = endHr.getValue() + ":" + endMin.getValue() ;
        }

        return  endTime ;
    }


    /**
     * @param hours the hours in the 24-hour format HH:MM to be converted
     * @return the hours in seconds
     */
    private Integer hoursToSeconds(String hours)
    {
        int inSeconds = 0 ;

        inSeconds = inSeconds + (Integer.parseInt(hours.substring(0, 2)) * 60 * 60) ;       // convert the hour part
        inSeconds = inSeconds + (Integer.parseInt(hours.substring(3, 5)) * 60) ;            // convert the minute part

        return inSeconds ;
    }


    /**
     * Checks if the start time is valid given an ending time
     * @return true if valid else false
     */
    private Boolean validateTimes()
    {
        boolean valid = true ;
        int startTimeInSeconds = hoursToSeconds(getStartTime()) ;
        int endTimeInSeconds = hoursToSeconds(getEndTime()) ;

        if(endTimeInSeconds <= startTimeInSeconds)   valid = false ;

        return valid ;
    }


    /**
     * Method to start the timer or cancel it depending on the current state
     */
    public void add()
    {
        String description = "-" ;
        String val = repeatBox.getValue() ;

        if(!this.descriptionField.getText().isBlank())
            description = descriptionField.getText() ;


        if(!checkBox.isSelected())
        {
            if (startHr.getValue().equals("Hr") || startMin.getValue().equals("Min") || endHr.getValue().equals("Hr") || endMin.getValue().equals("Min"))
            {
                this.messageBox.show("You must assign a start and an end time!", "ERROR");
            }
            else if(audioFileField.getText().isBlank())
            {
                this.messageBox.show("Please select an audio file.", "ERROR") ;
            }
            else
            {
                double period = 0.0 ;       // must be in seconds

                if(!this.periodField.getText().isBlank())
                {
                    period = Double.parseDouble(this.periodField.getText());

                    if(periodChoiceBox.getValue().equals("Minutes"))
                        period = period * 60;
                    else
                        period = (period * 60) * 60 ;
                }

                try
                {
                    if(validateTimes())
                    {
                        if(validateMediaFile())
                        {
                            BellCollection.getInstance().addNewBell(generateBellID(), getStartTime(), getEndTime(), period, val, this.audioFileField.getText(), description) ;

                            ((Stage) this.root.getScene().getWindow()).close();
                        }
                        else
                        {
                            messageBox.show("Only .mp3 and .wav files are allowed! Select another audio file.", "ERROR") ;
                        }
                    }
                    else
                    {
                        messageBox.show("The times entered are not valid.", "ERROR");
                    }
                }
                catch (Exception e)
                {
                    messageBox.show("Oops! Something went wrong. Please check the details you've entered and try again.", "ERROR");

                    e.printStackTrace();
                }
            }
        }
        else
        {
            if(startHr.getValue().equals("Hr") || startMin.getValue().equals("Min"))
            {
                messageBox.show("You must assign a start time!", "ERROR") ;
            }
            else
            {
                if(validateMediaFile())
                {
                    try
                    {
                        BellCollection.getInstance().addNewBell(generateBellID(), getStartTime(), "", 0, val, this.audioFileField.getText(), description);

                        ((Stage) this.root.getScene().getWindow()).close();
                    }
                    catch (Exception e)
                    {
                        messageBox.show("Oops! Something went wrong. Please check the details you've entered and try again.", "ERROR");

                        e.printStackTrace();
                    }
                }
                else
                {
                    messageBox.show("Only .mp3 and .wav files are allowed! Select another audio file.", "ERROR") ;
                }
            }
        }
    }


    public void openFileChooser()
    {
        FileChooser fileChooser = new FileChooser() ;
        fileChooser.setTitle("Browse media file") ;

        File mediaFile = fileChooser.showOpenDialog(this.root.getScene().getWindow()) ;
        String filePath = "" ;

        if(mediaFile != null)   filePath = mediaFile.getPath() ;

        this.audioFileField.setText(filePath) ;
    }


    /**
     * Checks the file extension of the picked audio file and validates it
     * @return true if valid (.wav or .mp3), otherwise false
     */
    private boolean validateMediaFile()
    {
        boolean valid = false ;
        String filePath = this.audioFileField.getText() ;
        String fileExtension = filePath.substring(filePath.lastIndexOf(".")) ;

        if(fileExtension.equalsIgnoreCase(".mp3") || fileExtension.equalsIgnoreCase(".wav"))
        {
            valid = true ;
        }

        return valid ;
    }


    private String generateBellID()
    {
        String id ;
        int counter = 0 ;
        boolean found ;

        do
        {
            id = IDGenerator.generateID(2, 4) ;
            found = false ;

            while(!found && counter < BellCollection.getInstance().getListOfBells().size())
            {
                if(BellCollection.getInstance().getListOfBells().get(counter).getBellID().equals(id))
                    found = true;

                counter++;
            }
        }
        while(found) ;

        return id ;
    }
}