package com.skyblue.shop.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skyblue.shop.Products;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "shoppingdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "products";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our thumnail url column
    private static final String THUMBNAIL_COL = "thumbnail";

    // below variable is for our product name column
    private static final String PRODUCT_NAME_COL = "product_name";

    // below variable is for our product description column
    private static final String PRODUCT_DESCRIPTION_COL = "product_description";

    // below variable is for our sale price column
    private static final String SALE_PRICE_COL = "sale_price";

    // below variable is for our discount price column
    private static final String DISCOUNT_PRICE_COL = "discount_price";

    // below variable is for our RATING column
    private static final String RATING_COL = "rating";

    // below variable is for our FEATURE 1 column
    private static final String FEATURE_1_COL = "feature_1";

    // below variable is for our FEATURE 2 column
    private static final String FEATURE_2_COL = "feature_2";

    // below variable is for our FEATURE 3 column
    private static final String FEATURE_3_COL = "feature_3";

    // below variable is for our FEATURE 4 column
    private static final String FEATURE_4_COL = "feature_4";

    // below variable is for our FEATURE 5 column
    private static final String FEATURE_5_COL = "feature_5";



    Context context;

    // creating a constructor for our database handler.
    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + THUMBNAIL_COL + " TEXT,"
                + PRODUCT_NAME_COL + " TEXT,"
                + PRODUCT_DESCRIPTION_COL + " TEXT,"
                + SALE_PRICE_COL + " TEXT,"
                + DISCOUNT_PRICE_COL + " TEXT,"
                + RATING_COL + " TEXT,"
                + FEATURE_1_COL + " TEXT,"
                + FEATURE_2_COL + " TEXT,"
                + FEATURE_3_COL + " TEXT,"
                + FEATURE_4_COL + " TEXT,"
                + FEATURE_5_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
//    public void addNewDownload(String fileName, int fileSize, String timeDate, int downloadStatus, String fileUrl , String filePath) {
//
//        // on below line we are creating a variable for
//        // our sqlite database and calling writable method
//        // as we are writing data in our database.
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // on below line we are creating a
//        // variable for content values.
//        ContentValues values = new ContentValues();
//
//        // on below line we are passing all values
//        // along with its key and value pair.
//        values.put(FILE_NAME_COL, fileName);
//        values.put(FILE_SIZE_COL, fileSize);
//        values.put(FILE_DOWNLOADED_DATE_COL, timeDate);
//        values.put(FILE_DOWNLOADED_STATUS_COL, downloadStatus);
//        values.put(FILE_DOWNLOAD_URL_COL, fileUrl);
//        values.put(FILE_DOWNLOAD_PATH_COL, filePath);
//
//        // after adding all values we are passing
//        // content values to our table.
//        db.insert(TABLE_NAME, null, values);
//
//
//        // at last we are closing our
//        // database after adding database.
//        db.close();
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //get the all notes
    public ArrayList<Products> getHomeProducts() {
        ArrayList<Products> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products products = new Products();
                products.setId(cursor.getString(0));
                products.setProduct_name(cursor.getString(1));
                products.setTitle(cursor.getString(2));
                products.setThumbnail(cursor.getString(3));
                products.setSale_price(cursor.getString(7));
                products.setDiscount_price(cursor.getString(7));
                products.setRating(cursor.getString(7));
                products.setFeature_1(cursor.getString(7));
                products.setFeature_2(cursor.getString(7));
                products.setFeature_3(cursor.getString(7));
                products.setFeature_4(cursor.getString(7));
                products.setFeature_5(cursor.getString(7));
                arrayList.add(products);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

}
