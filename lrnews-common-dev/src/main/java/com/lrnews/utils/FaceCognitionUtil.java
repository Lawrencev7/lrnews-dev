package com.lrnews.utils;

import com.lrnews.values.CommonTestStrings;

public class FaceCognitionUtil {
    public static boolean verifyFace(String faceImg64) {
        return faceImg64.equalsIgnoreCase(CommonTestStrings.FACE_IMG64);
    }
}
