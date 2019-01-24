package com.example.pr_idi.mydatabaseexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class BookData extends AppCompatActivity{

    // Database fields
    private SQLiteDatabase database;

    // Helper to manipulate table
    private MySQLiteHelper dbHelper;

    private static String resultFromServer;
    static List<Book> getAllBooks = new ArrayList<>();

    // Here we only select Title and Author, must select the appropriate columns
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_QUANTITY,
            MySQLiteHelper.COLUMN_AUTHOR, MySQLiteHelper.COLUMN_CATEGORY,
            MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, MySQLiteHelper.COLUMN_PUBLISHER};

    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Book createBook(String title, String author, String publisher, int quantity,
                           String category, String eval) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + author);

        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_QUANTITY, quantity);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, eval);

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null,
                values);

        // Main activity calls this procedure to create a new book
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example)
        // to feed the view.

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);

        // Do not forget to close the cursor
        cursor.close();

        new SaveBook().execute(title, author, publisher, String.valueOf(quantity), category, eval, "save");

        // Return the book
        return newBook;
    }

    public Book findBookById(long id){

        Book b;
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID
                        + " =?", new String[] {String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        b = cursorToBook(cursor);
        cursor.close();

        return b;
    }

    public void deleteBook(Book book) {
        long id = book.getId();
        System.out.println("Book deleted with id: " + id);
        int afected = database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Book> findBookByTitle(String title){
        ArrayList<Book> books = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_TITLE
                        + " =?", new String[] {title}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }


    public void insertBook(Book book){
        long id = database.insert(MySQLiteHelper.TABLE_BOOKS,null,book.toContentValues());
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try {
           new GetAllBooks().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
//                allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Book book = cursorToBook(cursor);
//            books.add(book);
//            cursor.moveToNext();
//        }
//        // make sure to close the cursor
//        cursor.close();
        return getAllBooks;
    }

    public void updateBookStock(Book b, int newVlaue) {

        Long id = b.getId();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.COLUMN_QUANTITY,newVlaue);
        database.update(dbHelper.TABLE_BOOKS,cv,dbHelper.COLUMN_ID+"="+id,null);

        new SaveBook().execute( b.getTitle(), b.getAuthor(), b.getPublisher(), String.valueOf(newVlaue), b.getCategory(), b.getPersonal_evaluation(), "sell");
    }

    public void updateValoracion(Book b, String valoracion) {
        ContentValues cv = new ContentValues();
        long id = b.getId();
        int fetchedValue = Integer.parseInt(valoracion);
        int oldValue =  b.getYear();
        int updatedValue = fetchedValue + oldValue;
        cv.put(dbHelper.COLUMN_QUANTITY,updatedValue);
        database.update(dbHelper.TABLE_BOOKS,cv,dbHelper.COLUMN_ID+"="+id,null);
    }

    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();

        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_TITLE)));
        book.setAuthor(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_AUTHOR)));
        book.setPublisher(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_PUBLISHER)));
        book.setYear(cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_QUANTITY)));
        book.setCategory(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_CATEGORY)));
        book.setPersonal_evaluation(cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_PERSONAL_EVALUATION)));
        return book;
    }



    //saving to remote db
    @SuppressLint("StaticFieldLeak")
    class SaveBook extends AsyncTask<String , Void, String>{

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress=new ProgressDialog();
//            progress.setMessage("Collecting Details Please Wait...");
//            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progress.setIndeterminate(true);
//            progress.setProgress(0);
//            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String saveServerAddress = "http://www.puredroid.info/mrj/savebook.php";

            List<NameValuePair> dataToBind = new ArrayList<>();

            dataToBind.add(new BasicNameValuePair("title",strings[0]));
            dataToBind.add(new BasicNameValuePair("author",strings[1]));
            dataToBind.add(new BasicNameValuePair("publisher",strings[2]));
            dataToBind.add(new BasicNameValuePair("quantity",strings[3]));
            dataToBind.add(new BasicNameValuePair("category",strings[4]));
            dataToBind.add(new BasicNameValuePair("price",strings[5]));
            dataToBind.add(new BasicNameValuePair("function",strings[6]));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 15000);
            HttpConnectionParams.setSoTimeout(httpRequestParams,15000);

            try {
                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(saveServerAddress);
                post.setEntity(new UrlEncodedFormEntity(dataToBind));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity httpEntity = httpResponse.getEntity();
                resultFromServer = EntityUtils.toString(httpEntity);

                Log.e("Response From Server", " "+resultFromServer);

            }catch (Exception e){
                Log.e("Error is", e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }



    @SuppressLint("StaticFieldLeak")
    class GetAllBooks extends AsyncTask<String, Void, String>{


//        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress=new ProgressDialog(getApplication().getApplicationContext());
//            progress.setMessage("Loading Data Please Wait...");
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String serverAddress = "http://puredroid.info/mrj/getallbooks.php";


            String id, title, author, quantity, publisher, category, personal_evaluation;

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 15000);
            HttpConnectionParams.setSoTimeout(httpRequestParams,15000);
            getAllBooks.clear();

            try {
                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(serverAddress);
                HttpResponse httpResponse = client.execute(post);

                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);
                Log.e("Server Data", result);

                JSONArray jsonArray = new JSONArray(result);

                if (jsonArray.length() == 0) {
                    Log.e("Error is ", "Inside If json");
                } else {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        id = jsonObject.getString("id");
                        title = jsonObject.getString("title");
                        Log.e("Server Title", title);
                        author = jsonObject.getString("author");
                        quantity = jsonObject.getString("quantity");
                        publisher = jsonObject.getString("publisher");
                        category = jsonObject.getString("category");
                        personal_evaluation = jsonObject.getString("rate");

                        Book book = new Book();
                        book.setId(Long.parseLong(id));
                        book.setTitle(title);
                        book.setAuthor(author);
                        book.setYear(Integer.parseInt(quantity));
                        book.setPublisher(publisher);
                        book.setCategory(category);
                        book.setPersonal_evaluation(personal_evaluation);

                        getAllBooks.add(book);
                    }
                }
            }catch (Exception e){
                Log.e("Error is", e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            progress.dismiss();
        }

    }

}