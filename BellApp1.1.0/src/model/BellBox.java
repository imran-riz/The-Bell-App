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


package model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle ;
import javafx.util.Duration;

import controllers.HomePageController;
import util.BellCollection;
import values.Status;

import java.util.Objects;
import java.util.Optional;


/**
 * A BellBox represents a ListItem that will be placed in the ListView in the HomePage. The BellBox will contain the
 * details of a Bell and allow the user pause/reset or delete it depending on the Bell's status.
 */
public class BellBox {

    private final Bell bell ;
    private int index ;

    private final ImageView pauseImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/pause_icon.png")).toString())) ;
    private final ImageView resetImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/reset_icon.png")).toString())) ;
    private final ImageView deleteImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/delete_icon.png")).toString())) ;

    private final StackPane root = new StackPane() ;
    private final AnchorPane anchorPane = new AnchorPane() ;

    private final Rectangle blockRectangle = new Rectangle() ;
    private final Rectangle indexRectangle = new Rectangle() ;

    private final Label descriptionLbl = new Label();
    private final Label indexLbl = new Label();
    private final Label startTimeLbl = new Label();
    private final Label endTimeLbl = new Label();
    private final Label periodLbl = new Label();
    private final Label repeatLbl = new Label();
    private final Label statusLbl = new Label();
    private final Label audioFileLbl = new Label();
    private final Button actionBtn = new Button();
    private final Button deleteBtn = new Button();

    private Timeline timeline ;


    public BellBox(Bell bell, int index) {
        this.bell = bell;
        this.index = index;

        this.draw() ;
        this.startTimeline() ;
    }


    public void setIndex(int index) {
        this.index = index ;
        this.indexLbl.setText("#" + (this.index + 1));
    }

    public Pane getRoot() {
        return this.root ;
    }

    public void stopTimeline() {
        this.bell.cancel() ;
        this.timeline.stop() ;
    }

    private void draw() {
        this.blockRectangle.setWidth(740.0);
        this.blockRectangle.setHeight(105.0);
        this.blockRectangle.setFill(Color.BLACK) ;
        this.blockRectangle.setOpacity(0.6) ;

        this.indexRectangle.setWidth(55.0) ;
        this.indexRectangle.setHeight(100.0) ;
        this.indexRectangle.setFill(Color.WHITE);

        this.initialiseLabels() ;
        this.initialiseButtons() ;

        AnchorPane.setLeftAnchor(this.indexRectangle, 5.0) ;
        AnchorPane.setTopAnchor(this.indexRectangle, 0.0) ;
        AnchorPane.setBottomAnchor(this.indexRectangle, 0.0) ;

        AnchorPane.setLeftAnchor(this.descriptionLbl, 70.0);
        AnchorPane.setTopAnchor(this.descriptionLbl, 5.0);

        AnchorPane.setLeftAnchor(this.indexLbl, 10.0);
        AnchorPane.setTopAnchor(this.indexLbl, 35.0);

        AnchorPane.setLeftAnchor(this.startTimeLbl, 70.0);
        AnchorPane.setBottomAnchor(this.startTimeLbl, 35.0);

        AnchorPane.setLeftAnchor(this.endTimeLbl, 70.0);
        AnchorPane.setBottomAnchor(this.endTimeLbl, 10.0);

        AnchorPane.setLeftAnchor(this.periodLbl, 230.0);
        AnchorPane.setTopAnchor(this.periodLbl, 35.0);

        AnchorPane.setLeftAnchor(this.repeatLbl, 230.0);
        AnchorPane.setBottomAnchor(this.repeatLbl, 10.0);

        AnchorPane.setLeftAnchor(this.statusLbl, 410.0);
        AnchorPane.setTopAnchor(this.statusLbl, 35.0);

        AnchorPane.setLeftAnchor(this.audioFileLbl, 410.0);
        AnchorPane.setBottomAnchor(this.audioFileLbl, 10.0);

        AnchorPane.setTopAnchor(this.actionBtn, 5.0);
        AnchorPane.setRightAnchor(this.actionBtn, 50.0);

        AnchorPane.setTopAnchor(this.deleteBtn, 5.0);
        AnchorPane.setRightAnchor(this.deleteBtn, 10.0);

        this.root.setPrefWidth(740.0);
        this.root.setPrefHeight(105.0);
        this.root.setMinWidth(Region.USE_PREF_SIZE);
        this.root.setMinHeight(Region.USE_PREF_SIZE);
        this.root.setMaxWidth(Region.USE_PREF_SIZE);
        this.root.setMaxHeight(Region.USE_PREF_SIZE);

        this.anchorPane.setPrefWidth(735.0);
        this.anchorPane.setPrefHeight(100.0);
        this.anchorPane.setMinWidth(Region.USE_PREF_SIZE);
        this.anchorPane.setMinHeight(Region.USE_PREF_SIZE);
        this.anchorPane.setMaxWidth(Region.USE_PREF_SIZE);
        this.anchorPane.setMaxHeight(Region.USE_PREF_SIZE);

        this.anchorPane.getChildren().addAll(this.indexRectangle, this.descriptionLbl, this.indexLbl, this.startTimeLbl, this.endTimeLbl, this.periodLbl, this.repeatLbl, this.statusLbl, this.audioFileLbl, this.actionBtn, this.deleteBtn) ;
        this.root.getChildren().addAll(this.anchorPane, this.blockRectangle) ;

        this.blockRectangle.setVisible(false) ;

        this.anchorPane.setId("bell_box_anchor_pane_style");
        this.descriptionLbl.setId("description_style");
        this.indexLbl.setId("index_lbl_style");
        this.deleteBtn.setId("delete_btn_style");
        this.actionBtn.setId("action_btn_style");
    }

    private void initialiseLabels() {
        this.descriptionLbl.setText(this.bell.getDescription());
        this.descriptionLbl.setPrefWidth(550.0);
        this.descriptionLbl.setPrefHeight(30.0);

        this.indexLbl.setText("#" + (index + 1));
        this.indexLbl.setAlignment(Pos.CENTER) ;
        this.indexLbl.setPrefWidth(40.0);
        this.indexLbl.setPrefHeight(30.0);

        this.startTimeLbl.setText("Start time: " + this.bell.getStartTime());
        this.startTimeLbl.setPrefWidth(150.0);
        this.startTimeLbl.setPrefHeight(30.0);

        this.endTimeLbl.setText("End time: " + this.bell.getEndTime());
        this.endTimeLbl.setPrefWidth(150.0);
        this.endTimeLbl.setPrefHeight(30.0);

        this.periodLbl.setText("Period: " + this.bell.getPeriod());
        this.periodLbl.setPrefWidth(150.0);
        this.periodLbl.setPrefHeight(30.0);

        this.repeatLbl.setText("Repeat: " + this.bell.getRepeat());
        this.repeatLbl.setPrefWidth(150.0);
        this.repeatLbl.setPrefHeight(30.0);

        this.statusLbl.setText("Status: " + this.bell.getStatus());
        this.statusLbl.setPrefWidth(150.0);
        this.statusLbl.setPrefHeight(30.0);

        this.audioFileLbl.setText("Audio file: " + this.bell.getPathToMediaFile());
        this.audioFileLbl.setPrefWidth(315.0);
        this.audioFileLbl.setPrefHeight(30.0);

    }

    private void initialiseButtons() {
        this.pauseImageView.setFitWidth(20.0);
        this.pauseImageView.setFitHeight(20.0);

        this.resetImageView.setFitWidth(20.0);
        this.resetImageView.setFitHeight(20.0);

        this.deleteImageView.setFitWidth(20.0);
        this.deleteImageView.setFitHeight(20.0);

        this.deleteBtn.setGraphic(this.deleteImageView) ;
        this.deleteBtn.setPrefWidth(35.0);
        this.deleteBtn.setPrefHeight(35.0);
        this.deleteBtn.setShape(new Circle(5.0)) ;
        this.deleteBtn.setOnAction(e -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION) ;
            alert.setTitle("Delete Scheduled Bell");
            alert.getDialogPane().setContent(new Label("Are you absolutely sure that you wish to delete this scheduled bell? Once deleted, it can't be undone."));
            alert.getDialogPane().getButtonTypes().clear();
            alert.getDialogPane().getButtonTypes().addAll(ButtonType.NO, ButtonType.YES) ;

            Optional<ButtonType> result = alert.showAndWait() ;

            if (result.isPresent() && result.get() == ButtonType.YES) {
                this.bell.cancel() ;
                BellCollection.getInstance().removeBellFromList(this.bell) ;
                HomePageController.removeBellBox(this) ;
            }

        });

        this.actionBtn.setPrefWidth(35.0);
        this.actionBtn.setPrefHeight(35.0);
        this.actionBtn.setShape(new Circle(5.0));
        this.actionBtn.setOnAction(e -> {

            // pause of reset the bell depending on its status....
            if(this.bell.getStatus().equals(Status.RUNNING)) {      // if it is running, then pause it
                this.bell.pause() ;
                this.actionBtn.setGraphic(this.resetImageView) ;
            }
            else if(this.bell.getStatus().equals(Status.PAUSED)) {
                this.bell.reset() ;
                this.actionBtn.setGraphic(this.pauseImageView) ;
            }

            this.statusLbl.setText("Status: " + this.bell.getStatus());
        });


        if(this.bell.getStatus().equals(Status.RUNNING)) {
            this.actionBtn.setGraphic(this.pauseImageView) ;
        }
        else {
            this.actionBtn.setGraphic(this.resetImageView) ;
        }

        if(this.bell.getStatus().equals(Status.STOPPED)) {
            this.actionBtn.setDisable(true) ;
        }
    }


    private void startTimeline() {
        // do this every 1 second
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

            // if the bell is INTERRUPTED (occurs only when the audio file is missing)
            if(this.bell.getStatus().equals(Status.INTERRUPTED)) {

                if(this.bell.checkFile()) {             // check if the file exists...
                    this.blockRectangle.setVisible(false) ;
                    this.bell.reset();
                    this.actionBtn.setDisable(false) ;
                    this.deleteBtn.setDisable(false) ;


                    // update the action button's graphic accordingly
                    if(this.bell.getStatus().equals(Status.RUNNING)) {
                        this.actionBtn.setGraphic(this.pauseImageView) ;
                    }
                    else {
                        this.actionBtn.setGraphic(this.resetImageView) ;
                    }

                    if(this.bell.getStatus().equals(Status.STOPPED)) {
                        this.actionBtn.setDisable(true) ;
                    }

                    this.statusLbl.setText("Status: " + this.bell.getStatus());
                    HomePageController.hideWarning();
                }
            }
            else {
                if(!this.bell.checkFile()) {            // if the file does not exist
                    this.bell.interrupt() ;
                    this.actionBtn.setDisable(true) ;
                    this.deleteBtn.setDisable(true) ;
                    this.blockRectangle.setVisible(true) ;

                    this.statusLbl.setText("Status: " + this.bell.getStatus());
                    HomePageController.showWarning();
                }

                if(this.bell.getStatus().equals(Status.STOPPED) && !this.actionBtn.isDisable()) {
                    this.actionBtn.setDisable(true);
                    this.actionBtn.setGraphic(this.resetImageView);
                    this.statusLbl.setText("Status: " + this.bell.getStatus());
                }
            }
        })) ;

        this.timeline.setCycleCount(Animation.INDEFINITE) ;
        this.timeline.play() ;
    }
}
