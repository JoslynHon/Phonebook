package com.bignerdranch.android.phonebook;

import java.util.UUID;

public class Phonebook
{
    private UUID mId;
    private String mTitle;

    private String mDetails;

    public Phonebook()
    {
        this(UUID.randomUUID());
    }

    public Phonebook(UUID id)
    {
        mId = id;
    }

    public UUID getId()
    {
        return mId;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

}