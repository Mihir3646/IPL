package com.ipl.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;

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

    /**
     * @param mActivity
     * @param message
     * @param isCancelable
     * @return
     * @purpose show progress dialog
     */
    public static ProgressDialog showProgressDialog(final Activity mActivity, final String message, boolean isCancelable) {

        if (mActivity != null) {
            final ProgressDialog mDialog = new ProgressDialog(mActivity);
            mDialog.setCancelable(isCancelable);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(message);
            mDialog.show();
            return mDialog;
        }
        return null;
    }

    /**
     * @purpose dismiss progress dialog
     */
    public static void dismissProgressDialog(ProgressDialog mDialog) {
        if ((mDialog != null) && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


}
