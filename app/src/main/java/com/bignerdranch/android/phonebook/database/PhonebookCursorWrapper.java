package com.bignerdranch.android.phonebook.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.phonebook.Phonebook;
import com.bignerdranch.android.phonebook.database.PhonebookDbSchema.PhonebookTable;

import java.util.UUID;

public class PhonebookCursorWrapper extends CursorWrapper
{
    public PhonebookCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Phonebook getPhonebook()
    {
        String uuidString = getString(getColumnIndex(PhonebookTable.Cols.UUID));
        String title = getString(getColumnIndex(PhonebookTable.Cols.TITLE));
        String details = getString(getColumnIndex(PhonebookTable.Cols.DETAILS));

        Phonebook phonebook = new Phonebook(UUID.fromString(uuidString));
        phonebook.setTitle(title);
        phonebook.setDetails(details);

        return phonebook;
    }
}
