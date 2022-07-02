package model;

import javafx.scene.layout.Pane;

public class Page {
    public Pane pane ;
    public String pathToStyleSheet ;

    public Page(Pane p, String s) {
        this.pane = p ;
        this.pathToStyleSheet = s ;
    }
}