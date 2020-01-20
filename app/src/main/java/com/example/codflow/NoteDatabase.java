package com.example.codflow;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {

    /**
     * Singleton
     * <p>
     * comment by mohamed_allam
     * this line means that no more than ONE object created in same time only one object can
     * connect with Database ==> that is the MAGIC of ROOMING
     */
    private static NoteDatabase instance;

    /**
     * connecting Dao
     * now we create Abstract method ,we don't need to write method body here
     */
    public abstract NoteDao noteDao();

    /**
     * == here create our only Instance and call from outside a method to create an instance
     * == synchronized --> means only one thread at time
     * == check if instance created or not if not generate new one else didn't create new one
     * and use current instance
     */

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    /** how to populate ROOM Database using Callback
     * issue --> app starts with table empty or have elements
     * SQLiteOpenHelper class do this on_create method because it called when create Database
     * but in any time else we call callback method and in it call on_create again
     * */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;
        public PopulateDBAsyncTask(NoteDatabase db){
            noteDao =db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1","description 1"));
            noteDao.insert(new Note("Title 2","description 2"));
            noteDao.insert(new Note("Title 3","description 3"));
            return null;
        }
    }


}
