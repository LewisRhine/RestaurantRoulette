package com.restaurantroulette.app.lewisrhine.restaurantroulette;

import android.app.Activity;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@Config(manifest = "build/intermediates/manifests/debug/AndroidManifest.xml", resourceDir = "../../../../build/intermediates/res/debug", emulateSdk = 18)
//@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    @Test
    public void shouldFail() {
        assertTrue(false);
    }
}
