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


package values;


/**
 * Each bell has only 4 statuses:
 *      RUNNING - when the current time is within the range of the set start and end times
 *      STOPPED - when the current time is not within the range of the set start and end times
 *      PAUSED - when the user has chosen to pause a running bell
 *      INTERRUPTED - when the audio file of the scheduled bell is missing
 */
public interface Status
{
    String RUNNING = "RUNNING" ;
    String PAUSED = "PAUSED" ;
    String STOPPED = "STOPPED" ;
    String INTERRUPTED = "INTERRUPTED" ;
}
