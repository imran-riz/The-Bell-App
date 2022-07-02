/*
 * Copyright (c) 2022 by Imran R.
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

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import model.Bell;
import model.BellBox;
import util.BellCollection;
import util.PageNavigator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * The controller class to HomePage.fxml
 */
public class HomePageController implements Initializable
{
    public AnchorPane root ;
    public Label header ;
    public Button menuBtn ;
    public Circle circle ;

    private static final ListView<Pane> listView = new ListView<>() ;
    private VBox menuVBox ;

    private final ImageView menuImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/menu_icon.png")).toString())) ;
    private final ImageView backImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/back_arrow_icon.png")).toString())) ;

    private static final List<BellBox> bellBoxList = new ArrayList<>() ;
    private static final Label warningLbl = new Label("The audio file is missing for some bells. Restore the file for the bells to work!");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AnchorPane.setTopAnchor(listView, 245.0) ;
        AnchorPane.setLeftAnchor(listView, 65.0) ;
        AnchorPane.setRightAnchor(listView, 65.0) ;
        AnchorPane.setBottomAnchor(listView, 40.0) ;

        listView.setOnMouseClicked(e -> {
            if(this.menuVBox.isVisible()) {
                this.menuBtn.setVisible(true);
                this.menuVBox.setVisible(false) ;
            }
        });

        AnchorPane.setLeftAnchor(warningLbl, 0.0);
        AnchorPane.setRightAnchor(warningLbl, 0.0);
        AnchorPane.setBottomAnchor(warningLbl, 3.0);

        warningLbl.setAlignment(Pos.CENTER) ;
        warningLbl.setVisible(false);

        this.root.getChildren().addAll(listView, warningLbl) ;

        this.root.setOnMouseClicked(e -> {
            if(this.menuVBox.isVisible()) {
                this.menuBtn.setVisible(true);
                this.menuVBox.setVisible(false) ;
            }
        });


        // load all the bells and insert them into the ListView
        for(int i = 0 ; i < BellCollection.getInstance().getListOfBells().size() ; i++) {
            BellBox bellBox = new BellBox(BellCollection.getInstance().getListOfBells().get(i), i) ;
            listView.getItems().add(bellBox.getRoot()) ;

            bellBoxList.add(bellBox) ;
        }

        this.menuImageView.setFitWidth(35) ;
        this.menuImageView.setFitHeight(40) ;

        this.menuBtn.setGraphic(this.menuImageView) ;

        this.createMenu() ;

        this.header.setId("header_style");
        this.menuBtn.setId("menu_btn_style") ;
        warningLbl.setId("warning_lbl_style") ;
    }


    private void createMenu() {
        double BUTTON_WIDTH = 200.0 ;
        double BUTTON_HEIGHT = 40.0 ;

        // create the menuVBox and anchor it
        this.menuVBox = new VBox() ;
        this.menuVBox.setPrefWidth(BUTTON_WIDTH) ;
        AnchorPane.setTopAnchor(this.menuVBox, 0.0) ;
        AnchorPane.setBottomAnchor(this.menuVBox, 0.0) ;

        // create and add all buttons needed in the menu
        Button backBtn = new Button() ;
        backBtn.setGraphic(this.backImageView) ;
        backBtn.setPrefWidth(BUTTON_WIDTH) ;
        backBtn.setPrefHeight(BUTTON_HEIGHT) ;
        backBtn.setOnAction(e -> {
            this.menuBtn.setVisible(true);
            this.menuVBox.setVisible(false);
        }) ;
        backBtn.setId("menu_option_btn_style") ;

        Button addBtn = new Button("Add A New Bell") ;
        addBtn.setPrefWidth(BUTTON_WIDTH) ;
        addBtn.setPrefHeight(BUTTON_HEIGHT) ;
        addBtn.setId("menu_option_btn_style") ;
        addBtn.setOnAction(e -> {
            this.menuBtn.setVisible(true);
            this.menuVBox.setVisible(false);
            PageNavigator.activate("AddBellPage") ;
        });

        Button aboutBtn = new Button("About") ;
        aboutBtn.setPrefWidth(BUTTON_WIDTH) ;
        aboutBtn.setPrefHeight(BUTTON_HEIGHT) ;
        aboutBtn.setId("menu_option_btn_style") ;
        aboutBtn.setOnAction(e -> {
            this.menuBtn.setVisible(true);
            this.menuVBox.setVisible(false);
            PageNavigator.activate("AboutPage") ;
        }) ;

        this.menuVBox.getChildren().addAll(backBtn, addBtn, aboutBtn) ;
        this.menuVBox.setId("menu_vbox_style") ;

        this.root.getChildren().add(this.menuVBox) ;
        this.menuVBox.setVisible(false) ;
    }


    public static void stopTimelines() {
        for(BellBox bellBox: bellBoxList) {
            bellBox.stopTimeline();
        }
    }

    public void showMenu() {
        this.menuVBox.setVisible(true) ;
        this.menuBtn.setVisible(false) ;
    }

    public static void addBell(Bell newBell) {
        BellBox bellBox = new BellBox(newBell, bellBoxList.size()) ;
        bellBoxList.add(bellBox) ;
        listView.getItems().add(bellBox.getRoot()) ;
    }

    public static void removeBellBox(BellBox bellBox) {
        listView.getItems().remove(bellBox.getRoot()) ;
        bellBoxList.remove(bellBox) ;

        for (int i = 0 ; i < bellBoxList.size() ; i++) {
            bellBoxList.get(i).setIndex(i) ;
        }
    }

    public static void showWarning() {
        if (!warningLbl.isVisible()) {
            warningLbl.setVisible(true);
        }
    }

    public static void hideWarning() {
        if (warningLbl.isVisible()) {
            warningLbl.setVisible(false);
        }
    }
}