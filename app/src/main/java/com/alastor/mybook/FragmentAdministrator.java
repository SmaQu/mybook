package com.alastor.mybook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentAdministrator {
    private static final String TAG = FragmentAdministrator.class.getSimpleName();

    public static boolean isFragmentPresent(final FragmentManager fragmentManager,
                                            final int fragmentContainer,
                                            final String simpleClassName) {
        Fragment currentFragment = fragmentManager
                .findFragmentById(fragmentContainer);
        if (currentFragment != null && currentFragment.getTag() != null) {
            return currentFragment.getTag().equals(simpleClassName);
        }
        return false;
    }

    public static void addFragment(final FragmentManager fragmentManager,
                                   final int fragmentContainer,
                                   final Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(fragmentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    public static void replaceFragment(final FragmentManager fragmentManager,
                                       final int fragmentContainer,
                                       final Fragment fragment) {
        replaceFragment(fragmentManager, fragmentContainer, fragment, false);

    }

    public static void replaceFragment(final FragmentManager fragmentManager,
                                       final int fragmentContainer,
                                       final Fragment fragment,
                                       final boolean allowPreserveNavigation) {
        final String TAG = fragment.getClass().getSimpleName();

        if (isFragmentPresent(fragmentManager, fragmentContainer, TAG)) {
            return;
        }

        if (!allowPreserveNavigation) popAllBackStackEntries(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(fragmentContainer, fragment, TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(TAG)
                .commit();
    }

    public static void popBackStack(final FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    public static void popAllBackStackEntries(final FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
