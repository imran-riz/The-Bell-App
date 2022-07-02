package util;

import javafx.scene.Scene;
import model.Page;

import java.util.HashMap;


public class PageNavigator {

    private static final HashMap<String, Page> pageMap = new HashMap<>() ;
    private static Scene mainScene = null ;

    public static void setMainScene(Scene scene) {
        mainScene = scene ;
    }


    public static void addPage(String name, Page page) {
        pageMap.put(name, page) ;
    }

    public static void removePage(String name) {
        pageMap.remove(name) ;
    }


    public static void activate(String name) {
        Page page = pageMap.get(name) ;

        mainScene.setRoot(page.pane) ;
        mainScene.getStylesheets().clear() ;
        mainScene.getStylesheets().add(page.pathToStyleSheet) ;
    }
}
