package com.bignerdranch.android.phonebook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.phonebook.database.PhonebookDbSchema.PhonebookTable;

public class PhonebookBaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "phonebookBase.db";

    public PhonebookBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + PhonebookTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PhonebookTable.Cols.UUID + ", " +
                PhonebookTable.Cols.TITLE + ", " +
                PhonebookTable.Cols.DETAILS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
