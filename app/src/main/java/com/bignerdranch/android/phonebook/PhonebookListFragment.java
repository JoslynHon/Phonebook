package com.bignerdranch.android.phonebook;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PhonebookListFragment extends Fragment
{
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mPhonebookRecyclerView;
    private TextView mEmptyTextView;
    private Button mNewPhonebookButton;
    private PhonebookAdapter mAdapter;
    private boolean mSubtitleVisible;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_phonebook_list, container, false);

        mPhonebookRecyclerView = (RecyclerView) view.findViewById(R.id.phonebook_recycler_view);
        mPhonebookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPhonebookRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        Drawable mDivider = ContextCompat.getDrawable(getActivity(), R.drawable.divider);
        DividerItemDecoration vItemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        vItemDecoration.setDrawable(mDivider);

        mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);
        mNewPhonebookButton = (Button) view.findViewById(R.id.new_phonebook_button);
        mNewPhonebookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewPhonebook();
            }
        });

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fb_add);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewPhonebook();
            }
        });

        if (savedInstanceState != null)
        {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_phonebook_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else
        {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle()
    {
        PhonebookLab phonebookLab = PhonebookLab.get(getActivity());
        int phonebookCount = phonebookLab.getPhonebooks().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, phonebookCount,phonebookCount);

        if (!mSubtitleVisible)
        {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI()
    {
        PhonebookLab phonebookLab = PhonebookLab.get(getActivity());
        List<Phonebook> phonebooks = phonebookLab.getPhonebooks();

        if (phonebooks.size() == 0)
        {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mNewPhonebookButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mEmptyTextView.setVisibility(View.GONE);
            mNewPhonebookButton.setVisibility(View.GONE);
        }


        if (mAdapter == null)
        {
            mAdapter = new PhonebookAdapter(phonebooks);
            mPhonebookRecyclerView.setAdapter(mAdapter);

        }
        else
        {
            mAdapter.setPhonebooks(phonebooks);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private void createNewPhonebook()
    {
        Phonebook phonebook = new Phonebook();
        PhonebookLab.get(getActivity()).addPhonebook(phonebook);
        Intent intent = PhonebookPagerActivity.newIntent(getContext(), phonebook.getId());
        startActivity(intent);
    }

    private class PhonebookHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Phonebook mPhonebook;
        private TextView mTitleTextView;
        private TextView mPhoneNumberTextView;

        public PhonebookHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_phonebook, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.phonebook_title);
            mPhoneNumberTextView = (TextView) itemView.findViewById(R.id.phonebook_phone_number);
        }

        public void bind(Phonebook phonebook)
        {
            mPhonebook = phonebook;

            mTitleTextView.setText(mPhonebook.getTitle());
            mPhoneNumberTextView.setText(mPhonebook.getDetails());

        }

        @Override
        public void onClick(View view)
        {
            Intent intent = PhonebookPagerActivity.newIntent(getActivity(), mPhonebook.getId());
            startActivity(intent);
        }
    }

    private class PhonebookAdapter extends RecyclerView.Adapter<PhonebookHolder>
    {
        private List<Phonebook> mPhonebooks;

        public PhonebookAdapter(List<Phonebook> phonebooks)
        {
            mPhonebooks = phonebooks;
        }

        @Override
        public PhonebookHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PhonebookHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PhonebookHolder holder, int position)
        {
            Phonebook phonebook = mPhonebooks.get(position);
            holder.bind(phonebook);
        }

        @Override
        public int getItemCount()
        {
            return mPhonebooks.size();
        }

        public void setPhonebooks(List<Phonebook> phonebooks)
        {
            mPhonebooks = phonebooks;
        }
    }
}
