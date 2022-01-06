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

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.BellCollection;
import values.Status;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller class to BellInfo.fxml
 * Will display the information stored about a selected bell from the HomeView.
 * Options to pause and delete the Bell will also be provided.
 */
public class BellInfoViewController implements Initializable
{
    public AnchorPane root ;
    public Label header, warning ;
    public TextField startTimeField, endTimeField, periodField, statusField, repeatField, descriptionField, audioFileField ;
    public Button actionBtn, deleteBtn, searchBtn ;

    private Timeline timeline ;

    private final ConfirmationBox confirmationBox = new ConfirmationBox() ;
    private final MessageBox messageBox = new MessageBox() ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.header.setText(this.header.getText() + HomeViewController.selectedBell.getBellID()) ;

        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/search_icon.png")).toString()));
        imageView.setFitWidth(15.0) ;
        imageView.setFitHeight(15.0) ;

        this.searchBtn.setGraphic(imageView) ;

        // initialise all the text fields using the selected bell
        this.startTimeField.setText(HomeViewController.selectedBell.getStartTime()) ;
        this.endTimeField.setText(HomeViewController.selectedBell.getEndTime()) ;
        this.periodField.setText(HomeViewController.selectedBell.getPeriod()) ;
        this.statusField.setText(HomeViewController.selectedBell.getStatus()) ;
        this.repeatField.setText(HomeViewController.selectedBell.getRepeat()) ;
        this.descriptionField.setText(HomeViewController.selectedBell.getDescription()) ;
        this.audioFileField.setText(HomeViewController.selectedBell.getPathToMediaFile()) ;

        if(!HomeViewController.selectedBell.checkFile())
        {
            this.warning.setText("Audio file missing! Select another or restore the file to its previous location.");
            HomeViewController.selectedBell.interrupt() ;
        }

        if(HomeViewController.selectedBell.getStatus().equals(Status.STOPPED) || HomeViewController.selectedBell.getStatus().equals(Status.INTERRUPTED))
        {
            actionBtn.setDisable(true);
        }
        else if(HomeViewController.selectedBell.getStatus().equals(Status.PAUSED))
        {
            actionBtn.setText("Reset") ;
        }
        else
        {
            actionBtn.setText("Pause") ;
        }

        // do this every 1 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event ->
        {
            // if the selected bell is INTERRUPTED (occurs only when the audio file is missing)
            if(HomeViewController.selectedBell.getStatus().equals(Status.INTERRUPTED))
            {
                if(HomeViewController.selectedBell.checkFile())     // if the file exists
                {
                    this.warning.setText("");
                    HomeViewController.selectedBell.reset();
                }
            }
            else
            {
                if(!HomeViewController.selectedBell.checkFile())    // if the file does not exist
                {
                    this.warning.setText("Audio file missing! Select another or restore the file to its previous location.");
                    HomeViewController.selectedBell.interrupt() ;
                }
            }
        })) ;
        timeline.setCycleCount(Animation.INDEFINITE) ;
        timeline.play() ;

        this.root.setId("root_style") ;
        this.header.setId("header_style") ;
        this.warning.setId("warning_style") ;
    }


    public void openFileChooser() throws Exception
    {
        FileChooser fileChooser = new FileChooser() ;
        fileChooser.setTitle("Browse media file") ;

        File mediaFile = fileChooser.showOpenDialog(this.root.getScene().getWindow()) ;

        if(mediaFile != null)
        {
            // validate the selected media file before writing it to disk
            if(validateMediaFile())
            {
                this.warning.setText("") ;
                this.audioFileField.setText(mediaFile.getPath()) ;

                HomeViewController.selectedBell.setPathToMediaFile(this.audioFileField.getText()) ;
                BellCollection.getInstance().writeDataToFile() ;
            }
            else
            {
                this.messageBox.show("Only .mp3 and .wav files are allowed! Select another audio file.", "ERROR") ;
            }
        }
    }


    public void delete()
    {
        if(confirmationBox.show("All details about this bell will be erased. Delete?", "WARNING", "Yes", "No"))
        {
            ((Stage) deleteBtn.getScene().getWindow()).close() ;

            BellCollection.getInstance().removeBellFromList(HomeViewController.selectedBell) ;
        }
    }


    public void action()
    {
        System.out.println("ACTION: " + actionBtn.getText()) ;

        if(this.actionBtn.getText().equalsIgnoreCase("Pause"))
        {
            HomeViewController.selectedBell.pause() ;
            this.timeline.stop() ;

            ((Stage) this.root.getScene().getWindow()).close() ;
        }
        else if(this.actionBtn.getText().equalsIgnoreCase("Reset"))
        {
            HomeViewController.selectedBell.reset() ;
            this.timeline.stop() ;

            ((Stage) this.root.getScene().getWindow()).close() ;
        }
    }


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
}
