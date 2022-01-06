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

public class MessageBox
{
    public void show(String message, String title)
    {
        Stage primaryStage = new Stage() ;
        primaryStage.setTitle(title) ;

        Label lbl = new Label() ;
        lbl.setText(message) ;
        lbl.setPadding(new Insets(5)) ;

        Button btnOK = new Button() ;
        btnOK.setText("OK") ;
        btnOK.setOnAction(e -> primaryStage.close()) ;

        VBox vbox = new VBox(20) ;
        vbox.getChildren().addAll(lbl, btnOK) ;
        vbox.setAlignment(Pos.CENTER) ;
        vbox.setPadding(new Insets(15, 5, 15, 5)) ;

        Scene scene = new Scene(vbox) ;

        primaryStage.setScene(scene) ;
        primaryStage.setResizable(false) ;
        primaryStage.initModality(Modality.APPLICATION_MODAL) ;
        primaryStage.showAndWait() ;
    }
}