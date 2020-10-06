package com.alastor.mybook;

import android.widget.ViewFlipper;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.alastor.mybook.books.bookcreator.CreateBookFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateBookFragmentTest {

    private static final String TITLE_TEXT = "title";
    private static final String DESCRIPTION_TEXT = "description";
    private static final String COVER_TEXT = "cover_url";

    private FragmentScenario<CreateBookFragment> fragmentFragmentScenario;

    @Before
    public void init() {
        fragmentFragmentScenario = FragmentScenario.launchInContainer(CreateBookFragment.class, null, R.style.AppTheme, null);
    }

    @Test
    public void check_if_flipper_show_buttons() {
        AtomicInteger flipperDisplayedChild = new AtomicInteger();
        fragmentFragmentScenario.onFragment(fragment -> {
            ViewFlipper viewFlipper = fragment.getView().findViewById(R.id.create_book_flipper);
            flipperDisplayedChild.set(viewFlipper.getDisplayedChild());
        });
        assertThat(flipperDisplayedChild.get(), is(equalTo(0)));
    }

    @Test
    public void are_required_field_visible() {
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.title_text)).check(matches(isDisplayed()));
        onView(withId(R.id.description)).check(matches(isDisplayed()));
        onView(withId(R.id.description_text)).check(matches(isDisplayed()));
    }

    @Test
    public void check_possibility_to_write() {
        onView(withId(R.id.title_text)).perform(ViewActions.typeText(TITLE_TEXT));
        onView(withId(R.id.title_text)).check(matches(withText(TITLE_TEXT)));

        onView(withId(R.id.description_text)).perform(ViewActions.typeText(DESCRIPTION_TEXT));
        onView(withId(R.id.description_text)).check(matches(withText(DESCRIPTION_TEXT)));

        onView(withId(R.id.cover_url_text)).perform(ViewActions.typeText(COVER_TEXT));
        onView(withId(R.id.cover_url_text)).check(matches(withText(COVER_TEXT)));
    }

    @Test
    public void check_are_recreated_with_values() {
        onView(withId(R.id.title_text)).perform(ViewActions.typeText(TITLE_TEXT));
        onView(withId(R.id.description_text)).perform(ViewActions.typeText(DESCRIPTION_TEXT));
        onView(withId(R.id.cover_url_text)).perform(ViewActions.typeText(COVER_TEXT));

        fragmentFragmentScenario.recreate();

        onView(withId(R.id.title_text)).check(matches(withText(TITLE_TEXT)));
        onView(withId(R.id.description_text)).check(matches(withText(DESCRIPTION_TEXT)));
        onView(withId(R.id.cover_url_text)).check(matches(withText(COVER_TEXT)));
    }

    @Test
    public void check_are_error_shown() {
        AtomicReference<String> expectedError = new AtomicReference<>();
        fragmentFragmentScenario.onFragment(fragment -> {
            expectedError.set(fragment.getString(R.string.label_required));
        });
        onView(withId(R.id.save)).perform(click());

        onView(withId(R.id.title)).check(matches(TextInputLayoutMatcher.isErrorShown(expectedError.get())));
        onView(withId(R.id.description)).check(matches(TextInputLayoutMatcher.isErrorShown(expectedError.get())));
    }
}
