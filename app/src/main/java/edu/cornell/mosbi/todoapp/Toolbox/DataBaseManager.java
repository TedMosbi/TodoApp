package edu.cornell.mosbi.todoapp.Toolbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.cornell.mosbi.todoapp.Model.TodoItem;

import java.util.ArrayList;

public class DataBaseManager {

    SQLiteDatabase database;
    SQLiteManager manager;
    String dbTableName = "task";
    String dbColumnStatus = "status";
    String dbColumnName = "taskName";
    String color;

    public DataBaseManager(Context context, String dbTableName, String color) {
        this.dbTableName = dbTableName;
        this.color = color;

        SQLiteManager.dbTableName = dbTableName;
        manager = new SQLiteManager(context);
        database = manager.getWritableDatabase();
        String query = "Create Table IF NOT EXISTS " + dbTableName +
                " (" + dbColumnStatus + " Boolean, " + dbColumnName + " Text " + ");";
        database.execSQL(query);
    }

    public void addTask(boolean status, String taskName) {
        ContentValues value = new ContentValues();
        value.put(dbColumnStatus, status);
        value.put(dbColumnName, taskName);
        database.insert(dbTableName, null, value);
    }

    public void removeTask(String taskName) {
        database.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbColumnName + "=\"" + taskName + "\";");
    }

    public ArrayList<TodoItem> readFromDB() {
        ArrayList<TodoItem> tastksTemp = new ArrayList<>();
        String query = "SELECT * FROM " + dbTableName;
        Cursor c = database.query(dbTableName, new String[]{dbColumnName, dbColumnStatus}, null, null, null, null, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(dbColumnName)) != null) {
                TodoItem item = new TodoItem(c.getString(c.getColumnIndex(dbColumnName)),
                        Boolean.getBoolean(c.getString(c.getColumnIndex(dbColumnStatus))), color);
                tastksTemp.add(item);
            }
            c.moveToNext();
        }
        return tastksTemp;
    }
}
