package org.example;

import org.example.EpicClass.AnnotationsAreAwesome;
import static org.example.EpicClass.GIBBERISH;
import static org.example.EpicClass.PI;

import java.util.HashMap;
import java.util.Map;
import java.awt.color.ColorSpace;
import java.security.cert.Certificate;
import java.applet.*;


public class ImportTester {
    @AnnotationsAreAwesome
    public void wow() {
        System.out.println("Hello there!");
    }

    public int stuff() {
        Map<Integer, String> map = new HashMap<>();
        return map.size();
    }

    public double pi() {
        return PI;
    }
}
