package com.bignerdranch.android.phonebook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import java.util.UUID;

public class PhonebookFragment extends Fragment
{
    private static final String ARG_PHONEBOOK_ID = "phonebook_id";

    private Phonebook mPhonebook;
    private EditText mTitleField;
    private EditText mDetailsFeild;
    private Button mMessageButton;

    public static PhonebookFragment newInstance(UUID phonebookId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHONEBOOK_ID, phonebookId);

        PhonebookFragment fragment = new PhonebookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID phonebookId = (UUID) getArguments().getSerializable(ARG_PHONEBOOK_ID);
        mPhonebook = PhonebookLab.get(getActivity()).getPhonebook(phonebookId);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        PhonebookLab.get(getActivity())
                .updatePhonebook(mPhonebook);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_phonebook, menu);
        MenuItem deleteItem = menu.findItem(R.id.delete_button);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.delete_button:
                PhonebookLab.get(getActivity()).deletePhonebook(mPhonebook);
                getActivity().finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_phonebook, container, false);

        mTitleField = (EditText) v.findViewById(R.id.phonebook_title);
        mTitleField.setText(mPhonebook.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mPhonebook.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mDetailsFeild = (EditText) v.findViewById(R.id.phonebook_details);
        mDetailsFeild.setText(mPhonebook.getDetails());
        mDetailsFeild.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mPhonebook.setDetails(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mMessageButton = (Button) v.findViewById(R.id.phonebook_message);
        mMessageButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getPhonebookMessage());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.message_subject));
                i= Intent.createChooser(i, getString(R.string.send_phonebook));
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }
    }

    private String getPhonebookMessage()
    {
        @SuppressLint("StringFormatMatches") String message = getString(R.string.phonebook_message,
                mPhonebook.getTitle(), mPhonebook.getDetails());

        return message;
    }
}

