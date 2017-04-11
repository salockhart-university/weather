package ca.alexlockhart.a3.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationService extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationService";
    private static final String TABLE_CONTACTS = "location";
    private static final String KEY_ID = "id";
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_CITY = "city";

    public LocationService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY, %s TEXT, %s TEXT )", TABLE_CONTACTS, KEY_ID, KEY_PROVINCE, KEY_CITY);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_CONTACTS));
        onCreate(db);
    }

    public void wipe() {
        onUpgrade(this.getWritableDatabase(), 1, 1);
    }

    public void setLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROVINCE, location.getProvince());
        values.put(KEY_CITY, location.getCity());

        if (getLocation() == null) {
            db.insert(TABLE_CONTACTS, null, values);
        } else {
            db.update(TABLE_CONTACTS, values, "id=1", null);
        }

        db.close();
    }

    public Location getLocation() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_PROVINCE, KEY_CITY},
                KEY_ID + "=?", new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Location location = contactFromCursor(cursor);
            return location;
        }
        return null;
    }

    private Location contactFromCursor(Cursor cursor) {
        return new Location(cursor.getString(1), cursor.getString(2));
    }
}
