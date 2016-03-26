package cn.kejin.gitbook

import android.app.Application
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.runner.AndroidJUnit4
import android.test.ActivityInstrumentationTestCase2
import android.test.ApplicationTestCase
import android.test.AndroidTestCase
import android.test.suitebuilder.annotation.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [Testing Fundamentals](http://d.android.com/tools/testing/testing_android.html)
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest : ActivityInstrumentationTestCase2<MainActivity>(MainActivity::class.java) {

    @Before
    public override fun setUp() {
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    fun openDrawer() {
        activity // getActivity()
        Espresso.pressBack()
        Thread.sleep(1000)
        Espresso.pressBack()
    }

    @After
    public override fun tearDown(){
        super.tearDown();
    }
}
