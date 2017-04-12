package com.ipl.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.ipl.R;

public class Utils {
    /**
     * @param mActivity
     * @param newFragment
     * @param hideFragment
     * @purpose method for add fragment
     */
    public static void addFragment(final Activity mActivity, final Fragment newFragment, final Fragment hideFragment, final int containerId) {
        final FragmentManager fragmentManager = mActivity.getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(hideFragment);
        fragmentTransaction.add(containerId, newFragment, newFragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(hideFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * @param mActivity
     * @param newFragment
     * @purpose method for replace fragment
     */

    public static void replaceFragment(final Activity mActivity, final Fragment newFragment, final int containerId) {
        final FragmentManager fragmentManager = mActivity.getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, newFragment, newFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

}
