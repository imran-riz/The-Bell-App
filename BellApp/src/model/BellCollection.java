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


package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import util.DataExchange;
import values.Repeat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * An example of "Singleton design pattern"
 * Only one instance of this class will exist and can be accessed using the "getInstance()" method.
 * The class is responsible for keeping a collection of all the bells that exist in an "ObservableList" of "Bell"s
 */
public class BellCollection
{
    // this will be the ONLY instance of this class
    private final static BellCollection instance = new BellCollection() ;

    private final ObservableList<Bell> listOfBells = FXCollections.observableArrayList() ;
    private final String PATH_TO_DIRECTORY = System.getProperty("user.home") + "\\.bell-app" ;
    private final String PATH_TO_FILE = PATH_TO_DIRECTORY + "/bell_info.ser" ;


    // the constructor is made private so that this class cannot be instantiated
    private BellCollection() {}


    /**
     * Method to return the ONLY instance of this class
     */
    public static BellCollection getInstance()
    {
        return instance ;
    }

    /**
     * Method to create a new Bell. The data will be written to file if the repeat attribute is not set to NONE
     * @param bellID the unique number assigned to a Bell
     * @param startTime the starting time for a Bell
     * @param endTime the ending time for a Bell
     * @param periodInSeconds the period for the Bell in seconds
     * @param repeat the repeat value of the bell
     * @throws Exception when attributes are invalid (eg: endTime <= startTime)
     */
    public void addNewBell(String bellID, String startTime, String endTime, int periodInSeconds, String repeat, String path, String description) throws Exception
    {
        Bell newBell = new Bell(bellID, startTime, endTime, periodInSeconds, repeat, path, description) ;

        this.listOfBells.add(newBell) ;

        this.writeDataToFile() ;
    }


    public void removeBellFromList(Bell bell)
    {
        try
        {
            this.writeDataToFile() ;
            this.listOfBells.remove(bell) ;
            this.writeDataToFile() ;
        }
        catch (Exception e)
        {
            e.printStackTrace() ;
        }
    }


    public ObservableList<Bell> getListOfBells()
    {
        return this.listOfBells ;
    }


    /**
     * Used to save information of all the Bells created (except the ones that are not set to repeat) on disk
     * @throws Exception when the file is not found
     */
    public void writeDataToFile() throws Exception
    {
        ArrayList<Bell> list = new ArrayList<>() ;

        for (Bell bell : this.listOfBells)
        {
            if (!bell.getRepeat().equals(Repeat.NONE))
                list.add(bell) ;
        }

        DataExchange.writeToFile(PATH_TO_FILE, list) ;
    }


    /**
     * Used to get information of the bells that have been stored on disk and will reset them.
     * @throws Exception when the file is not found
     */
    public void readDataFromFile() throws Exception
    {
        Object readData ;

        try
        {
            readData = DataExchange.readFromFile(PATH_TO_FILE) ;

            ArrayList<Bell> list = (ArrayList<Bell>) readData ;
            this.listOfBells.addAll(list);

            // reset all the bells
            for(Bell b: this.listOfBells)
            {
                b.reset() ;
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException)
        {
            System.out.println("No saved data") ;
        }
        catch (FileNotFoundException fileNotFoundException)
        {
            System.out.println("\nBellCollection.readDataFromFile(): File not found! Creating directory...") ;

            File file = new File(PATH_TO_DIRECTORY) ;

            if(file.mkdirs())
            {
                System.out.println("BellCollection.readDataFromFile(): Directory created! Creating file in the directory...") ;

                File file2 = new File(PATH_TO_FILE) ;

                if(file2.createNewFile())
                {
                    System.out.println("BellCollection.readDataFromFile(): File created! Reading from file again...");
                    this.readDataFromFile() ;
                }
            }
        }
    }
}
