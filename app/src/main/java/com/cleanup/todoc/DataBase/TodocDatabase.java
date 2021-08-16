package com.cleanup.todoc.DataBase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;


    @Database(entities = {Task.class, Project.class},version = 1, exportSchema = false)
    public abstract class TodocDatabase extends RoomDatabase {
        // --- SINGLETON ---
        private static volatile TodocDatabase INSTANCE;
        // --- DAO ---
        public abstract TaskDao taskDao();
        public abstract ProjectDao projectDao();
        // --- INSTANCE ---
        public static TodocDatabase getInstance (Context context) {
            if (INSTANCE == null) {
                synchronized (TodocDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TodocDatabase.class, "MyDatabase.db")
                                .addCallback(prepopulateDatabase())
                                .build();
                    }
                }
            }
            return INSTANCE;
    }

    private static Callback prepopulateDatabase () {
            return new Callback() {

                @Override
                public void onCreate (@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id",1L);
                    contentValues.put ("name", "Project Tartampion  ");
                    contentValues.put ("color", 0xFFEADAD1);

                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("id", 2L);
                    contentValues1.put("name", "Project Lucidia ");
                    contentValues1.put("color", 0xFFB4CDBA);

                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("id", 3L);
                    contentValues2.put("name", "Project Circus");
                    contentValues2.put("color", 0xFFA3CED2);

                    db.insert ("Project", OnConflictStrategy.IGNORE, contentValues);
                    db.insert("Project", OnConflictStrategy.IGNORE, contentValues1);
                    db.insert ("Project", OnConflictStrategy.IGNORE,contentValues2 );


                }
            };
    }
}
