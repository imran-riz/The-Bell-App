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

import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutViewController implements Initializable
{
    public AnchorPane root ;
    public Label header ;
    public TextArea textArea_1, textArea_2 ;
    public Hyperlink hyperlink ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        hyperlink.setOnAction(e ->
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://github.com/space-ninja-x/The-Bell-App")) ;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }) ;

        root.setId("root_style") ;
        textArea_1.setId("info_style") ;
    }
}
