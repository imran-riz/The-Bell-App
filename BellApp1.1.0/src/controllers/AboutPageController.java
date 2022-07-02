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

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.shape.Rectangle;
import util.PageNavigator;


/**
 * The controller class to AboutPage.fxml
 * Will show a copy of the licence, a short description of the app and a hyperlink to the GitHub repository.
 */
public class AboutPageController implements Initializable
{
    public AnchorPane root ;
    public Label header ;
    public TextArea textArea_1, textArea_2 ;
    public Hyperlink hyperlink ;
    public Button backBtn ;
    public Rectangle rectangle ;

    private final ImageView backImageView = new ImageView(new Image(Objects.requireNonNull(this.getClass().getResource("/resources/images/back_arrow_icon.png")).toString())) ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.hyperlink.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/imran-2003/The-Bell-App/tree/version1.1.0#the-bell-app-v110")) ;
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }) ;

        this.rectangle.setFill(Color.valueOf("#5d6064")) ;

        this.backBtn.setShape(new Circle(5.0)) ;
        this.backBtn.setGraphic(this.backImageView) ;

        this.root.setId("root_style") ;
        this.textArea_1.setId("info_style") ;
    }


    public void goBack() {
        PageNavigator.activate("HomePage") ;
    }
}
