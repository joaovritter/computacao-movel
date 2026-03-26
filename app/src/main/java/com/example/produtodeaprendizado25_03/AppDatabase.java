package com.example.produtodeaprendizado25_03;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Equipamentos.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EquipamentosDao equipamentosDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "banco-de-dados"
                    )
                    .allowMainThreadQueries() // igual ao exemplo do professor
                    .build();
        }
        return INSTANCE;
    }


}
