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


package model;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import values.Repeat;
import values.Status;


/**
 * An object containing details about when the bell should be rung.
 */
public class Bell implements Serializable
{
    private String bellID, startTime, endTime, period, description ;
    private String pathToMediaFile, repeat, status ;

    private int delayInSeconds ;
    private double periodInSeconds ;

    private Timer timer ;
    private TimerTask timerTask ;


    public Bell() {}


    /**
     * @param id the unique bell ID
     * @param startTime the time the alarm must start at. 24-hour format HH:MM
     * @param endTime the time the alarm must stop at. 24-hour format HH:MM
     * @param periodInSeconds the period, in seconds, of the alarm.
     * @param repeat how often has the bell got to repeat. Must be a value from the interface values.Repeat
     * @param path the file path to the audio file.
     * @param description a description about the purpose of the bell (optional).
     */
    public Bell(String id, String startTime, String endTime, double periodInSeconds, String repeat, String path, String description)
    {
        this.bellID = id ;
        this.startTime = startTime;
        this.endTime = endTime;
        this.periodInSeconds = periodInSeconds;
        this.repeat = repeat ;
        this.pathToMediaFile = path ;
        this.description = description ;

        if(endTime.length() < 5) this.endTime = "-" ;

        // determine the format of the period for display
        if(periodInSeconds == 0)
        {
            this.period = "-" ;
        }
        else if(periodInSeconds >= 60)
        {
            this.period = Double.toString(periodInSeconds / 60) ;

            if(this.period.equals("1"))
                this.period = period.concat(" minute") ;
            else
                this.period = period.concat(" minutes") ;
        }
        else
        {
            this.period = Double.toString(periodInSeconds) ;

            if(this.period.equals("1"))
                this.period = this.period.concat(" second") ;
            else
                this.period = this.period.concat(" seconds") ;
        }


        this.status = Status.STOPPED ;

        if(this.repeat.equals(Repeat.NONE))
        {
            if(this.setDelay())
            {
                this.setTheTimer() ;
                this.status = Status.RUNNING ;
            }
        }
        else
        {
            if (this.playToday())
            {
                if(this.setDelay())
                {
                    this.setTheTimer();
                    this.status = Status.RUNNING;
                }
            }
        }
    }


    /**
     * Determines the delay to execute the timer task using the present time and the start time that's set
     * @return true if success, else false. As extra measure, the "delayInSeconds" will be set to -1
     */
    private boolean setDelay()
    {
        boolean val = false ;

        LocalTime localTime = LocalTime.now() ;

        int currentTimeInSeconds = (localTime.getHour() * 60 * 60) + (localTime.getMinute() * 60) + localTime.getSecond() ;
        int startTimeInSeconds = (Integer.parseInt(this.startTime.substring(0, 2)) * 60 * 60) + (Integer.parseInt(this.startTime.substring(3, 5)) * 60) ;
        int endTimeInSeconds = 0 ;

        // check if an endTime is set before converting it to seconds
        if(this.endTime.length() == 5)
        {
            endTimeInSeconds = (Integer.parseInt(this.endTime.substring(0, 2)) * 60 * 60) + (Integer.parseInt(this.endTime.substring(3, 5)) * 60) ;
        }


        if(currentTimeInSeconds < startTimeInSeconds)
        {
            this.delayInSeconds = startTimeInSeconds - currentTimeInSeconds ;
            val = true ;
        }
        else if(endTimeInSeconds > currentTimeInSeconds)
        {
            // then determine the delay using the period that is set
            boolean timeFound = false ;
            int temp = startTimeInSeconds ;

            while(!timeFound)
            {
                temp+=this.periodInSeconds ;

                if(temp > currentTimeInSeconds) timeFound = true ;
            }

            this.delayInSeconds = temp - currentTimeInSeconds ;
            val = true ;
        }
        else
        {
            this.delayInSeconds = -1 ;
        }


        return val ;
    }


    /**
     * Using the startTime, determine the delay for which the Timer must execute at.
     * The associated TimerTask will be cancelled when the system clock's time matches the endTime (if set).
     * @Exception when the delay is negative
     */
    private void setTheTimer()
    {
        timer = new Timer() ;

        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                ring() ;

                String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                int currentTimeInSeconds = (LocalTime.now().getHour() * 60 * 60) + (LocalTime.now().getMinute() * 60) + LocalTime.now().getSecond() ;
                int endTimeInSeconds = 0 ;

                // check if an end time is set before converting it to seconds
                if(endTime.length() == 5)
                {
                    endTimeInSeconds = (Integer.parseInt(endTime.substring(0, 2)) * 60 * 60) + (Integer.parseInt(endTime.substring(3, 5)) * 60);
                }

                if(endTimeInSeconds == 0)     // if the endTime is not set then the TimerTask executes only once
                {
                    cancel() ;
                    status = Status.END ;
                    System.out.println("END") ;
                }
                else if(currentTime.equals(endTime) || currentTimeInSeconds > endTimeInSeconds)        // if the current time matches or has passed the end time that's set
                {
                    cancel() ;
                    status = Status.END ;
                    System.out.println("END") ;
                }
            }
        } ;

        // schedule the Timer
        if(this.periodInSeconds > 0)
            this.timer.schedule(timerTask, (this.delayInSeconds * 1000L), (long) (this.periodInSeconds * 1000L)) ;
        else
            this.timer.schedule(timerTask, (this.delayInSeconds * 1000L)) ;
    }


    /**
     * Method to reset the bell. Must be called when changes to the Bell is made
     */
    public void reset()
    {
        this.status = Status.STOPPED ;

        if(this.repeat.equals(Repeat.NONE))         // this only occurs when the user has wished to reset a bell that was paused
        {
            if(setDelay())
            {
                this.setTheTimer();

                // before setting its status validate the path to the media file
                if(this.checkFile())
                {
                    this.status = Status.RUNNING;
                }
                else
                {
                    this.pause() ;
                }
            }
        }
        else
        {
            if(playToday())
            {
                if(setDelay())
                {
                    this.setTheTimer();

                    // before setting its status validate the path to the media file
                    if(this.checkFile())
                    {
                        this.status = Status.RUNNING;
                    }
                    else
                    {
                        this.pause() ;
                    }
                }
            }
        }
    }


    /**
     * Method to cancel the Timer and associated TimerTask for this Bell. This is to be used just before the application
     * closes.
     */
    public void cancel()
    {
        if(this.timerTask != null)
        {
            this.timerTask.cancel() ;
            this.timer.cancel() ;
        }
    }


    /**
     * Cancels the Timer and TimerTask and updates the status of the bell as PAUSED
     */
    public void pause()
    {
        this.cancel() ;
        this.status = Status.PAUSED ;
    }


    /**
     * Cancels the Timer and TimerTask and updates the status of the bell as INTERRUPTED.
     * Must be used ONLY when there's an error (eg: when the audio file is not found)
     */
    public void interrupt()
    {
        this.cancel() ;
        this.status = Status.INTERRUPTED ;
    }


    /**
     * Method to play the audio file
     */
    private void ring()
    {
        Media media = new Media(new File(this.pathToMediaFile).toURI().toString()) ;

        AudioClip audioClip = new AudioClip(media.getSource()) ;
        audioClip.play() ;
    }


    /**
     * Checks if the bell is supposed to play today depending on its "repeat" attribute
     * @return true if it's the right day, otherwise false
     */
    private Boolean playToday()
    {
        boolean play = false ;
        LocalDate localDate = LocalDate.now() ;
        DayOfWeek dayOfWeek =localDate.getDayOfWeek() ;
        String dayToRepeatOn = this.repeat.substring(6) ;           // get only the day name of the repeat attribute (eg: SATURDAY when repeat = EVERY SATURDAY)

        if(dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)
        {
            if(this.repeat.equals(Repeat.WEEKDAYS))
            {
                play = true ;
            }
            else
            {
                if(dayOfWeek.toString().equalsIgnoreCase(dayToRepeatOn))
                    play = true ;
            }
        }

        return play ;
    }


    /**
     * Checks if the assigned media file exists
     * @return true if found, otherwise false
     */
    public Boolean checkFile()
    {
        File file ;

        try
        {
            file = new File(this.pathToMediaFile);
        }
        catch (NullPointerException e)
        {
            return false ;
        }

        return file.exists() ;
    }


    public String getBellID() {
        return bellID;
    }

    public void setBellID(String bellID) {
        this.bellID = bellID;
    }


    public String getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }


    public String getEndTime()
    {
        return this.endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime ;
    }


    public double getPeriodInSeconds()
    {
        return this.periodInSeconds;
    }

    public void setPeriodInSeconds(double periodInSeconds)
    {
        this.periodInSeconds = periodInSeconds;
    }


    public String getPeriod()
    {
        return period;
    }

    public void setPeriod(String period)
    {
        this.period = period;
    }


    public String getStatus()
    {
        return this.status ;
    }


    public String getRepeat()
    {
        return this.repeat ;
    }

    public void setRepeat(String repeat)
    {
        this.repeat = repeat ;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPathToMediaFile()
    {
        return pathToMediaFile;
    }

    public void setPathToMediaFile(String pathToMediaFile)
    {
        this.pathToMediaFile = pathToMediaFile;
    }
}
