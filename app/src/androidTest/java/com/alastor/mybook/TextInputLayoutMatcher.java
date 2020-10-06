package com.alastor.mybook;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TextInputLayoutMatcher {

    public static Matcher<View> isErrorShown(String expectedError) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item instanceof TextInputLayout) {
                    final TextInputLayout textInputLayout = (TextInputLayout) item;
                    final CharSequence error = textInputLayout.getError();
                    if (error != null) {
                        return error.toString().equals(expectedError);
                    }

                }
                return false;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}
