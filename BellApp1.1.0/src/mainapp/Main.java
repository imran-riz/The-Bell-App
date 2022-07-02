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


package mainapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import model.Bell;
import util.BellCollection;
import model.Page;
import util.PageNavigator;
import values.Status;
import controllers.HomePageController;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class Main extends Application implements Status {

    public static void main(String[] args) {
        Application.launch() ;
    }

    @Override
    public void start(Stage window) {
        window.setTitle("THE BELL APP v1.1.0") ;
        window.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/resources/images/app_icon.png"))));
        window.setOnCloseRequest(e -> {

            // if the user wishes to close the application first check if there are any bells scheduled to be rung and warn the user
            boolean bellsLeft = false ;
            int counter = 0 ;

            while(!bellsLeft && counter < BellCollection.getInstance().getListOfBells().size()) {
                if(BellCollection.getInstance().getListOfBells().get(counter).getStatus().equals(Status.RUNNING)) {
                    bellsLeft = true;
                }

                counter++ ;
            }

            if(!bellsLeft) {
                System.exit(0) ;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING) ;
                alert.setTitle("WARNING! Closing application.");
                alert.getDialogPane().setContent(new Label("There are bells to be rung!! Closing the application will result in the cancellation of them all. Close the application?")) ;
                alert.getDialogPane().getButtonTypes().clear();
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO) ;

                Optional<ButtonType> result = alert.showAndWait() ;

                if (result.isPresent() && result.get() == ButtonType.YES) {
                    HomePageController.stopTimelines() ;

                    // cancel all the Bells
                    for(Bell bell: BellCollection.getInstance().getListOfBells()) {
                        bell.cancel();
                    }

                    System.exit(0) ;
                }
                else {
                    e.consume();
                }
            }
        }) ;

        try {
            // load data from the file before opening the application
            BellCollection.getInstance().readDataFromFile() ;

            // if there are any bells then check if the path set to the audio files are still valid. If it's invalid, pause the Bell
            for(Bell bell: BellCollection.getInstance().getListOfBells()) {
                if(!bell.checkFile()) {
                    bell.pause() ;
                }
            }

            Scene scene = new Scene(new Pane()) ;

            this.initialisePageNavigator(scene) ;
            PageNavigator.activate("HomePage") ;

            window.setScene(scene) ;
            window.setResizable(false) ;
            window.show() ;
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR) ;
            alert.getDialogPane().setContent(new Label("The application failed to open. Try again later."));
            alert.showAndWait() ;

            System.exit(0) ;
        }
    }


    private void initialisePageNavigator(Scene mainScene) throws IOException {
        Pane homePane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/fxml/pages/HomePage.fxml"))) ;
        Pane addBellPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/fxml/pages/AddBellPage.fxml"))) ;
        Pane aboutPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/fxml/pages/AboutPage.fxml"))) ;

        Page homePage = new Page(homePane, "/resources/styles/HomeStyle.css") ;
        Page addBellPage = new Page(addBellPane, "/resources/styles/AddBellStyle.css") ;
        Page aboutPage = new Page(aboutPane, "/resources/styles/AboutStyle.css") ;

        PageNavigator.setMainScene(mainScene) ;
        PageNavigator.addPage("HomePage", homePage) ;
        PageNavigator.addPage("AddBellPage", addBellPage) ;
        PageNavigator.addPage("AboutPage", aboutPage) ;
    }
}
