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

import javafx.stage.* ;
import javafx.scene.* ;
import javafx.scene.layout.* ;
import javafx.scene.control.* ;
import javafx.geometry.* ;

public class ConfirmationBox
{
    static Stage primaryStage;
    static boolean btnYesClicked ;

    public boolean show(String message, String title, String textYes, String textNo)
    {
        btnYesClicked = false ;

        primaryStage = new Stage() ;
        primaryStage.setTitle(title) ;

        Label lbl = new Label() ;
        lbl.setText(message) ;
        lbl.setAlignment(Pos.CENTER);
        lbl.setMaxWidth(Double.MAX_VALUE) ;
        lbl.setPadding(new Insets(5)) ;

        Button yesBtn = new Button() ;
        yesBtn.setText(textYes) ;
        yesBtn.setMinWidth(50) ;
        yesBtn.setPrefHeight(20) ;
        yesBtn.setOnAction(e ->
        {
            btnYesClicked = true ;
            primaryStage.close() ;
        }) ;

        Button noBtn = new Button() ;
        noBtn.setText(textNo) ;
        noBtn.setMinWidth(50) ;
        noBtn.setPrefHeight(20) ;
        noBtn.setOnAction(e ->
        {
            btnYesClicked = false ;
            primaryStage.close() ;
        }) ;

        HBox hbox = new HBox(20) ;
        hbox.getChildren().addAll(yesBtn, noBtn) ;
        hbox.setAlignment(Pos.CENTER) ;

        VBox vbox = new VBox(20) ;
        vbox.setAlignment(Pos.CENTER) ;
        vbox.getChildren().addAll(lbl, hbox) ;
        vbox.setPadding(new Insets(5)) ;
        vbox.setMinWidth(300.0) ;

        Scene scene = new Scene(vbox) ;

        primaryStage.setScene(scene) ;
        primaryStage.initModality(Modality.APPLICATION_MODAL) ;
        primaryStage.setResizable(false) ;
        primaryStage.showAndWait() ;

        return btnYesClicked ;
    }
}