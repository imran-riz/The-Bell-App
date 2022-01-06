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


package util;

import model.Bell;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to read and write data locally on the computer
 */
public class DataExchange
{
    public static void writeToFile(String pathToFile, List<Bell> list) throws Exception
    {
        FileOutputStream fileOutputStream = new FileOutputStream(pathToFile);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        for(Bell b : list)
        {
            objectOutputStream.writeObject(b.getBellID());
            objectOutputStream.writeObject(b.getStartTime());
            objectOutputStream.writeObject(b.getEndTime());
            objectOutputStream.writeObject(b.getPeriod());
            objectOutputStream.writeObject(b.getPeriodInSeconds());
            objectOutputStream.writeObject(b.getRepeat());
            objectOutputStream.writeObject(b.getDescription());
            objectOutputStream.writeObject(b.getPathToMediaFile());
        }

        objectOutputStream.close();
        fileOutputStream.close();
    }


    public static List<Bell> readFromFile(String pathToFile) throws Exception
    {
        List<Bell> list = new ArrayList<>() ;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(pathToFile) ;

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream) ;

            while (true)
            {
                Bell bell = new Bell() ;

                bell.setBellID((String) objectInputStream.readObject()) ;
                bell.setStartTime((String) objectInputStream.readObject()) ;
                bell.setEndTime((String) objectInputStream.readObject()) ;
                bell.setPeriod((String) objectInputStream.readObject()) ;
                bell.setPeriodInSeconds((double) objectInputStream.readObject()) ;
                bell.setRepeat((String) objectInputStream.readObject()) ;
                bell.setDescription((String) objectInputStream.readObject()) ;
                bell.setPathToMediaFile((String) objectInputStream.readObject()) ;

                list.add(bell) ;
            }
        }
        catch (EOFException eofException)
        {
            System.out.println("\nDataExchange.readFromFile: End of file reached!\n") ;
        }
        catch (ClassNotFoundException ce)
        {
            ce.printStackTrace();
        }

        return list ;
    }
}
