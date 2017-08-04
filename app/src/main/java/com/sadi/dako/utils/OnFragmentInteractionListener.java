package com.sadi.dako.utils;

import android.support.v4.app.Fragment;


public interface OnFragmentInteractionListener {

    public void setContentFragment(Fragment fragment, boolean addToBackStack, String title);
    public void addContentFragment(Fragment fragment, boolean addToBackStack, String title);
}
