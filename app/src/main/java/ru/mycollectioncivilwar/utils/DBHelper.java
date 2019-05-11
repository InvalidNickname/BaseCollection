package ru.mycollectioncivilwar.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static ru.mycollectioncivilwar.App.USES_DB_VERSION;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_BANKNOTES = "banknotes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_REVERSE = "reverse";
    public static final String COLUMN_OBVERSE = "obverse";
    public static final String COLUMN_CIRCULATION = "circulation";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PARENT = "parent";
    public static final String COLUMN_IMAGE = "image";
    public static final String TABLE_CATEGORIES = "categories";
    private static final String DATABASE_NAME = "main";
    private static DBHelper instance = null;
    private static SQLiteDatabase database;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, USES_DB_VERSION);
    }

    @Nullable
    public static synchronized DBHelper getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
            database = instance.getReadableDatabase();
            try {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(database.getPath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //instance = new DBHelper(context.getApplicationContext());
            //database = instance.getWritableDatabase();
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
