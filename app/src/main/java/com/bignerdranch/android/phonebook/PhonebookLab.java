package com.bignerdranch.android.phonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.phonebook.database.PhonebookBaseHelper;
import com.bignerdranch.android.phonebook.database.PhonebookCursorWrapper;
import com.bignerdranch.android.phonebook.database.PhonebookDbSchema.PhonebookTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhonebookLab
{
    private static PhonebookLab sPhonebookLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PhonebookLab get(Context context)
    {
        if (sPhonebookLab == null)
        {
            sPhonebookLab = new PhonebookLab(context);
        }
        return sPhonebookLab;
    }

    private PhonebookLab(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new PhonebookBaseHelper(context)
                .getWritableDatabase();
    }

    public void addPhonebook(Phonebook p)
    {
        ContentValues values = getContentValues(p);

        mDatabase.insert(PhonebookTable.NAME, null, values);
    }

    public void deletePhonebook(Phonebook p)
    {
        mDatabase.delete(PhonebookTable.NAME, PhonebookTable.Cols.UUID + " =?",
                new String[] {p.getId().toString()});
    }

    public List<Phonebook> getPhonebooks()
    {
        List<Phonebook> phonebooks = new ArrayList<>();

        try (PhonebookCursorWrapper cursor = queryPhonebooks(null, null))
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                phonebooks.add(cursor.getPhonebook());
                cursor.moveToNext();
            }
        }

        return phonebooks;
    }

    public Phonebook getPhonebook(UUID id)
    {

        try (PhonebookCursorWrapper cursor = queryPhonebooks(
                PhonebookTable.Cols.UUID + " =?",
                new String[] { id.toString() }
        ))
        {
            if (cursor.getCount() == 0)
            {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPhonebook();
        }
    }

    public void updatePhonebook(Phonebook phonebook)
    {
        String uuidString = phonebook.getId().toString();
        ContentValues values = getContentValues(phonebook);

        mDatabase.update(PhonebookTable.NAME, values,
                PhonebookTable.Cols.UUID + " =?",
                new String[] { uuidString });
    }

    private PhonebookCursorWrapper queryPhonebooks(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                PhonebookTable.NAME,
                null, // null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PhonebookCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Phonebook phonebook)
    {
        ContentValues values = new ContentValues();
        values.put(PhonebookTable.Cols.UUID, phonebook.getId().toString());
        values.put(PhonebookTable.Cols.TITLE, phonebook.getTitle());
        values.put(PhonebookTable.Cols.DETAILS, phonebook.getDetails());
        return values;
    }
}

