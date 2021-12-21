/*
 * Copyright (c) 2021 by Imran Rizwan
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

import javafx.application.Application ;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import model.Bell;
import model.BellCollection;

import values.Status;
import views.ConfirmationBox;
import views.MessageBox;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application implements Status
{
    private final MessageBox messageBox = new MessageBox() ;
    private Stage primaryStage ;


    public static void main(String[] args)
    {
        Application.launch(args) ;
    }


    @Override
    public void start(Stage window)
    {
        primaryStage = window ;

        primaryStage.setTitle("THE BELL APP") ;
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/resources/images/app_icon.png"))));
        primaryStage.setOnCloseRequest( e ->
        {
            // if the user wishes to close the application first check if there are any bells scheduled to be rung and warn the user
            boolean bellsLeft = false ;
            int counter = 0 ;

            while(!bellsLeft && counter < BellCollection.getInstance().getListOfBells().size())
            {
                if(BellCollection.getInstance().getListOfBells().get(counter).getStatus().equals(Status.RUNNING))
                    bellsLeft = true ;

                counter++ ;
            }

            if(!bellsLeft)
            {
                System.exit(0) ;
            }
            else
            {
                ConfirmationBox confirmationBox = new ConfirmationBox() ;

                if(confirmationBox.show("There are bells to be rung. Closing the application will result in the cancellation of them. Close the application?", "ATTENTION", "Yes", "No"))
                {
                    // cancel all the Bells before terminating the application
                    for(model.Bell bell: BellCollection.getInstance().getListOfBells())
                        bell.cancel() ;

                    System.exit(0) ;
                }
                else
                {
                    e.consume();
                }
            }
        }) ;

        try
        {
            // load data from the file before opening the application
            BellCollection.getInstance().readDataFromFile() ;

            // if there are any bells then check if the path set to the audio files are still valid. If it's invalid, pause the Bell
            for(Bell bell: BellCollection.getInstance().getListOfBells())
            {
                if(!bell.checkFile())
                {
                    bell.pause() ;
                }
            }

            this.openHomeView() ;
        }
        catch (Exception e)
        {
            messageBox.show("The application failed to open. Try again later. " + e.getLocalizedMessage(), "ERROR") ;
            e.printStackTrace() ;
            System.exit(0) ;
        }
    }

    private void openHomeView() throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/views/HomeView.fxml")));

        Scene scene = new Scene(root) ;
        scene.getStylesheets().add("/resources/styles/HomeStyle.css") ;

        primaryStage.setScene(scene) ;
        primaryStage.setResizable(false) ;
        primaryStage.show() ;
    }
}
