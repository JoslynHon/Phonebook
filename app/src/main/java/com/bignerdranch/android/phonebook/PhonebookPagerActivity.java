package com.bignerdranch.android.phonebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class PhonebookPagerActivity extends AppCompatActivity
{
    private static final String EXTRA_PHONEBOOK_ID = "com.bignerdranch.android.phonebook.phonebook_id";

    private ViewPager mViewPager;
    private List<Phonebook> mphonebooks;

    public static Intent newIntent(Context packageContext, UUID phonebookId)
    {
        Intent intent = new Intent(packageContext, PhonebookPagerActivity.class);
        intent.putExtra(EXTRA_PHONEBOOK_ID, phonebookId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_pager);

        UUID phonebookId = (UUID) getIntent().getSerializableExtra(EXTRA_PHONEBOOK_ID);

        mViewPager = (ViewPager) findViewById(R.id.phonebook_view_pager);

        mphonebooks = PhonebookLab.get(this).getPhonebooks();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        {
            @Override
            public Fragment getItem(int position)
            {
                Phonebook phonebook = mphonebooks.get(position);
                return PhonebookFragment.newInstance(phonebook.getId());
            }

            @Override
            public int getCount()
            {
                return mphonebooks.size();
            }
        });

        for (int i = 0; i < mphonebooks.size(); i++)
        {
            if (mphonebooks.get(i).getId().equals(phonebookId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

