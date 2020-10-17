package com.bignerdranch.android.phonebook;

import android.support.v4.app.Fragment;

public class PhonebookListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new PhonebookListFragment();
    }
}
