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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import model.Bell;
import model.BellCollection;
import values.Status;


/**
 * The controller class to HomeView.fxml
 * It will display a
 */
public class HomeViewController implements Initializable
{
    public AnchorPane root ;
    public Label header ;
    public HBox hbox ;
    public VBox vbox ;
    public Button addBtn ;
    public TableView<Bell> tableView ;
    public TableColumn<Bell, String> bellIDCol, descriptionCol, startTimeCol, endTimeCol, periodCol, statusCol, repeatCol;

    private final MessageBox messageBox = new MessageBox() ;
    private Timeline timeline ;

    public static Bell selectedBell ;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/add_icon.png")).toString()));
        imageView.setFitWidth(20.0) ;
        imageView.setFitHeight(20.0) ;

        this.addBtn.setGraphic(imageView);

        initialiseTable() ;

        // set a TimeLine that will execute every 1 second to refresh the content in the TableView
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event ->
        {
            // do this for every bell that exists
            for(Bell b: BellCollection.getInstance().getListOfBells())
            {
                if(b.getStatus().equals(Status.INTERRUPTED))    // if it's interrupted
                {
                    if(b.checkFile())
                        b.reset();
                }
                else if(!b.checkFile())
                {
                    b.interrupt() ;
                }
            }

            this.tableView.refresh() ;
        })) ;
        timeline.setCycleCount(Animation.INDEFINITE) ;
        timeline.play() ;

        this.root.setId("root_style");
        this.header.setId("header_style");
        this.hbox.setId("hbox_style");
        this.vbox.setId("vbox_style");
    }


    private void initialiseTable()
    {
        // initialise all the TableColumns to be used
        this.bellIDCol.setCellValueFactory(new PropertyValueFactory<>("bellID")) ;
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description")) ;
        this.startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime")) ;
        this.endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime")) ;
        this.periodCol.setCellValueFactory(new PropertyValueFactory<>("period")) ;
        this.statusCol.setCellValueFactory(new PropertyValueFactory<>("status")) ;
        this.repeatCol.setCellValueFactory(new PropertyValueFactory<>("repeat")) ;

        this.tableView.setItems(BellCollection.getInstance().getListOfBells()) ;

        this.tableView.setOnMouseClicked(event ->
        {
            if(event.getClickCount() >= 2)
            {
                selectedBell = this.tableView.getSelectionModel().getSelectedItem() ;

                if(selectedBell != null)
                {
                    try
                    {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/views/BellInfoView.fxml")));

                        Scene scene = new Scene(root);
                        scene.getStylesheets().add("/resources/styles/BellInfoStyle.css");

                        Stage newStage = new Stage();
                        newStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/resources/images/app_icon.png"))));
                        newStage.setTitle("BELL INFO");
                        newStage.setResizable(false);
                        newStage.setScene(scene);
                        newStage.initModality(Modality.APPLICATION_MODAL);
                        newStage.showAndWait();

                    }
                    catch (Exception e)
                    {
                        messageBox.show("Failed to open up the bell info window", "ERROR");
                        e.printStackTrace();
                    }
                }
            }
        }) ;
    }


    /**
     * Method to create a new bell
     */
    public void addBell()
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/views/AddBellView.fxml"))) ;

            Scene scene = new Scene(root) ;
            scene.getStylesheets().add("/resources/styles/CreateBellStyle.css") ;

            Stage newStage = new Stage() ;
            newStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/resources/images/app_icon.png"))));
            newStage.setTitle("NEW BELL") ;
            newStage.setResizable(false) ;
            newStage.setScene(scene) ;
            newStage.initModality(Modality.APPLICATION_MODAL) ;
            newStage.show() ;
        }
        catch (Exception e)
        {
            messageBox.show("An error occurred when creating a new alarm.", "ERROR") ;
            e.printStackTrace() ;
        }
    }


    public void about()
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/views/AboutView.fxml"))) ;

            Scene scene = new Scene(root) ;
            scene.getStylesheets().add("/resources/styles/AboutStyle.css") ;

            Stage newStage = new Stage() ;
            newStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/resources/images/app_icon.png"))));
            newStage.setTitle("ABOUT THE APP") ;
            newStage.setResizable(false) ;
            newStage.setScene(scene) ;
            newStage.initModality(Modality.APPLICATION_MODAL) ;
            newStage.showAndWait() ;
        }
        catch (Exception e)
        {
            messageBox.show("An error occurred.", "ERROR") ;
            e.printStackTrace() ;
        }
    }
}