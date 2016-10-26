package com.nodry.nodry.Presentacion;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.nodry.nodry.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntegrationTest1 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void integrationTest1() {
        ViewInteraction imageView = onView(
                allOf(withId(R.id.image), withContentDescription("Property Image"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.customListView),
                                        0),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        onData(anything())
                .inAdapterView(withId(R.id.customListView))
                .atPosition(0)
                .onChildView(withId(R.id.rotulo))
                .check(matches(isDisplayed()));

        onData(anything())
                .inAdapterView(withId(R.id.customListView))
                .atPosition(0)
                .onChildView(withId(R.id.direccion))
                .check(matches(isDisplayed()));


        onData(anything())
                .inAdapterView(withId(R.id.customListView))
                .atPosition(0)
                .onChildView(withId(R.id.diesel))
                .check(matches(isDisplayed()));


        onData(anything())
                .inAdapterView(withId(R.id.customListView))
                .atPosition(0)
                .onChildView(withId(R.id.lbldiesel))
                .check(matches(withText(startsWith("Diesel:"))));


        onData(anything())
                .inAdapterView(withId(R.id.customListView))
                .atPosition(0)
                .onChildView(withId(R.id.lblgasolina))
                .check(matches(withText(startsWith("Gasolina:"))));

        onData(anything())
                .inAdapterView(withId(R.id.customListView))
                .atPosition(0)
                .onChildView(withId(R.id.gasolina))
                .check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
