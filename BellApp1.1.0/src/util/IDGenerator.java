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


package util;

import java.util.Random;

public class IDGenerator {

    public static String generateID(int letterCount, int digitCount) {
        String val;
        String ltrs = "", digits = "";
        int n;

        Random random = new Random();

        for (int counter = 0; counter < letterCount; counter++) {
            n = (int) (Math.random() * (90 - 65) + 65);            // generate a random upper case letter's ASCII value

            ltrs = ltrs.concat(Character.toString((char) n));
        }

        for (int counter = 0; counter < digitCount; counter++) {
            n = random.nextInt(10);

            digits = digits.concat(Integer.toString(n));
        }

        val = ltrs.concat(digits);

        return val;
    }
}