package com.example.ericrpurvis.xplorer;

import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testLatLngLongitude() throws Exception {
        com.google.android.gms.maps.model.LatLng actual = new LatLng(1.0, 1.0);
        com.example.ericrpurvis.xplorer.LatLng test = new com.example.ericrpurvis.xplorer.LatLng(1.0, 1.0);

        assertEquals(actual.longitude, test.getLongitude(), 0.001);
    }

    @Test
    public void testLatLngLattitude() throws Exception {
        com.google.android.gms.maps.model.LatLng actual = new LatLng(1.0, 1.0);
        com.example.ericrpurvis.xplorer.LatLng test = new com.example.ericrpurvis.xplorer.LatLng(1.0, 1.0);

        assertEquals(actual.latitude, test.getLatitude(), 0.001);
    }
}