package com.example.pr_idi.mydatabaseexample;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BooksSortedByTitleFragment extends Fragment implements BookRecyclerViewAdapterWithOnClickListener.Interaction {

    private ArrayList<Book> myBooksAL = new ArrayList<>();
    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BooksSortedByTitleFragment me = this;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_sorted_by_title, container, false);

        RecyclerView.LayoutManager myLayoutManager;
        getResources().getConfiguration();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            myLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        else{
            myLayoutManager = new LinearLayoutManager(inflater.getContext());
        }
        RecyclerView myRecyclerView = v.findViewById(R.id.books_sorted_by_title_RV);
        myRecyclerView.setLayoutManager(myLayoutManager);
        RecyclerView.Adapter myAdapter = new BookRecyclerViewAdapterWithOnClickListener(myBooksAL, me);
        myRecyclerView.setAdapter(myAdapter);
        BookData myBookData = new BookData(getActivity().getApplicationContext());
        myBookData.open();
        myBooksAL.clear();
        myBooksAL.addAll(myBookData.getAllBooks());
        myBookData.close();
        myAdapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onItemClick(Book b) {

    }
}
