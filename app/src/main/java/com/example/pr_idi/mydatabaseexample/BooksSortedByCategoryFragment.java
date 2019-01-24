package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BooksSortedByCategoryFragment extends Fragment{

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private List<Book> searchResultBooks = new ArrayList<>();
    private BookData myBookData;
    private ArrayList<Book> myBooks = new ArrayList<>();
    private static final String TAG = "BooksSortedByCategory..";
    EditText editTextCategoryBooks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.books_sorted_by_category_layout,container,false);

        if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE){
            myLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        else{
            myLayoutManager = new LinearLayoutManager(inflater.getContext());
        }

        myRecyclerView =  v.findViewById(R.id.book_recycler_view);
        myRecyclerView.setLayoutManager(myLayoutManager);
        editTextCategoryBooks = v.findViewById(R.id.editTextCategoryBooks);

        myAdapter = new BookRecyclerViewAdapter(myBooks);
        myRecyclerView.setAdapter(myAdapter);
        myBookData = new BookData(getActivity().getApplicationContext());
        myBookData.open();
        myBooks.clear();
        myBooks.addAll(myBookData.getAllBooks());
        myBookData.close();
        sortByCategory(myBooks);
        myAdapter.notifyDataSetChanged();



        editTextCategoryBooks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                myBookData.open();

                searchResultBooks = myBookData.getAllBooks();
                myBooks.clear();

                Log.e("NewList", String.valueOf(searchResultBooks));

                String searchString = editTextCategoryBooks.getText().toString();
                int textLength = searchString.length();
                Log.e("NewList", String.valueOf(searchResultBooks));


                for(int i = 0; i<searchResultBooks.size(); i++)
                {
                    //Log.e("NewList Size", String.valueOf(searchResultBooks.size()));
                    String propertyname = searchResultBooks.get(i).getTitle();
                    if(textLength <= propertyname.length()){

                        //compare the String in EditText with Names in the ArrayList
                        if(propertyname.toLowerCase().contains(searchString.toLowerCase()))
                            myBooks.add(searchResultBooks.get(i));
                        myAdapter.notifyDataSetChanged();
                        myBookData.close();
                        Log.e("UpdatedList", String.valueOf(myBooks));
                    }
                }



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }

            @Override
            public void afterTextChanged(Editable s) {
                myBookData.open();

                searchResultBooks = myBookData.getAllBooks();
                myBooks.clear();

                Log.e("NewList", String.valueOf(searchResultBooks));

                String searchString = editTextCategoryBooks.getText().toString();
                int textLength = searchString.length();
                Log.e("NewList", String.valueOf(searchResultBooks));


                for(int i = 0; i<searchResultBooks.size(); i++)
                {
                    //Log.e("NewList Size", String.valueOf(searchResultBooks.size()));
                    String propertyname = searchResultBooks.get(i).getTitle();
                    if(textLength <= propertyname.length()){

                        //compare the String in EditText with Names in the ArrayList
                        if(propertyname.toLowerCase().contains(searchString.toLowerCase()))
                            myBooks.add(searchResultBooks.get(i));
                        myAdapter.notifyDataSetChanged();
                        myBookData.close();
                        Log.e("UpdatedList", String.valueOf(myBooks));
                    }
                }

            }
        });









        return v;
    }

    private void sortByCategory(ArrayList<Book> books){

        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2)
            {
                return  b1.getCategory().compareTo(b2.getCategory());
            }
        });
    }
}
