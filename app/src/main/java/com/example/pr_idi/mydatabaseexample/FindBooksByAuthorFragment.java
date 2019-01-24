package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FindBooksByAuthorFragment extends Fragment {

    ArrayAdapter<String> adapter;
    private List<Book> searchResultBooks = new ArrayList<>();

    private ArrayList<Book> myBooks = new ArrayList<>();
    EditText editTextAutor;
    TextView textViewAutorNotFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_find_books_by_author,container,false);

        RecyclerView.LayoutManager myLayoutManager;
        getResources().getConfiguration();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            myLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        else{
            myLayoutManager = new LinearLayoutManager(inflater.getContext());
        }

        editTextAutor = v.findViewById(R.id.editTextAutor);
        textViewAutorNotFound = v.findViewById(R.id.textViewAutorNotFound);

        RecyclerView myRecyclerView = v.findViewById(R.id.recyclerViewByAuthor);
        myRecyclerView.setLayoutManager(myLayoutManager);
        final RecyclerView.Adapter myAdapter = new BookRecyclerViewAdapter(myBooks);
        myRecyclerView.setAdapter(myAdapter);
        final BookData myBookData = new BookData(getActivity().getApplicationContext());
        myBookData.open();
        myBooks.clear();
        myBooks.addAll(myBookData.getAllBooks());
        myBookData.close();
        sortByCategory(myBooks);
        myAdapter.notifyDataSetChanged();



        editTextAutor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                myBookData.open();

                searchResultBooks = myBookData.getAllBooks();
                myBooks.clear();

                Log.e("NewList", String.valueOf(searchResultBooks));

                String searchString = editTextAutor.getText().toString();
                int textLength = searchString.length();
                Log.e("NewList", String.valueOf(searchResultBooks));


                for(int i = 0; i<searchResultBooks.size(); i++)
                {
                    //Log.e("NewList Size", String.valueOf(searchResultBooks.size()));
                    String propertyname = searchResultBooks.get(i).getAuthor();
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

                String searchString = editTextAutor.getText().toString();
                int textLength = searchString.length();
                Log.e("NewList", String.valueOf(searchResultBooks));


                for(int i = 0; i<searchResultBooks.size(); i++)
                {
                    //Log.e("NewList Size", String.valueOf(searchResultBooks.size()));
                    String propertyname = searchResultBooks.get(i).getAuthor();
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
