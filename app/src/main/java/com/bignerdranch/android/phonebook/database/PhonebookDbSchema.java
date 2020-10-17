package com.bignerdranch.android.phonebook.database;

public class PhonebookDbSchema
{
    public static final class PhonebookTable
    {
        public static final String NAME = "phonebooks";

        public static final class Cols
        {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DETAILS = "details";
        }
    }
}
