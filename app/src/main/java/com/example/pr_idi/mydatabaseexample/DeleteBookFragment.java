package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class DeleteBookFragment extends Fragment implements DeleteBookDialogFragment.NoticeDialogListener, BookRecyclerViewAdapterWithOnClickListener.Interaction {

    private EditText myEditText;
    private BookData myBookData;
    private DeleteBookFragment me;
    private FragmentManager myFragmentManager;
    private TextView error_message;
    private RecyclerView.Adapter myAdapter;
    private ArrayList<Book> books = new ArrayList<>();
    private List<Book> searchResultBooks = new ArrayList<>();
    private Book llibreAEliminar;
    private int pos_previa;
    private DeleteBookDialogFragment deleteBookDialogFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        me = this;
        View v = inflater.inflate(R.layout.delete_book_layout, container, false);
        myEditText =  v.findViewById(R.id.editTextTitle);
//        Button delButton = v.findViewById(R.id.button);
        error_message =  v.findViewById(R.id.textView16);
        myBookData = new BookData(getActivity().getApplicationContext());

        getResources().getConfiguration();
        RecyclerView.LayoutManager myLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            myLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        else{
            myLayoutManager = new LinearLayoutManager(inflater.getContext());
        }

        RecyclerView recyclerView = v.findViewById(R.id.recview);
        recyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new BookRecyclerViewAdapterWithOnClickListener(books,me);
        recyclerView.setAdapter(myAdapter);

        if(savedInstanceState != null){
            myEditText.setText(savedInstanceState.getString("titol_buscat"));
            myBookData.open();
            books.clear();
            if(myEditText.getText().toString().length() == 0){
                books.addAll(myBookData.getAllBooks());
            }
            else{
                books.addAll(myBookData.findBookByTitle(String.valueOf(myEditText.getText())));
            }
            myBookData.close();
            myAdapter.notifyDataSetChanged();

            if (books.isEmpty()){
                error_message.setVisibility(View.VISIBLE);
            }
            deleteBookDialogFragment = (DeleteBookDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("fragment_delete_book_dialog");
            if (deleteBookDialogFragment != null){
                deleteBookDialogFragment.setParams(me, llibreAEliminar);
            }


        }
        else{
            //Si es el primer cop, inicialment els mostrem tots
            books.clear();
            myBookData.open();
            books.addAll(myBookData.getAllBooks());
            myBookData.close();
            myAdapter.notifyDataSetChanged();
        }

        myFragmentManager = getActivity().getSupportFragmentManager();


        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                myBookData.open();

                searchResultBooks = myBookData.getAllBooks();
                books.clear();

                Log.e("NewList", String.valueOf(searchResultBooks));

                String searchString = myEditText.getText().toString();
                int textLength = searchString.length();
                Log.e("NewList", String.valueOf(searchResultBooks));


                for(int i = 0; i<searchResultBooks.size(); i++)
                {
                    //Log.e("NewList Size", String.valueOf(searchResultBooks.size()));
                    String propertyname = searchResultBooks.get(i).getTitle();
                    if(textLength <= propertyname.length()){

                        //compare the String in EditText with Names in the ArrayList
                        if(propertyname.toLowerCase().contains(searchString.toLowerCase()))
                            books.add(searchResultBooks.get(i));
                        myAdapter.notifyDataSetChanged();
                        myBookData.close();
                        Log.e("UpdatedList", String.valueOf(books));
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
                books.clear();

                Log.e("NewList", String.valueOf(searchResultBooks));

                String searchString = myEditText.getText().toString();
                int textLength = searchString.length();
                Log.e("NewList", String.valueOf(searchResultBooks));


                for(int i = 0; i<searchResultBooks.size(); i++)
                {
                    //Log.e("NewList Size", String.valueOf(searchResultBooks.size()));
                    String propertyname = searchResultBooks.get(i).getTitle();
                    if(textLength <= propertyname.length()){

                        //compare the String in EditText with Names in the ArrayList
                        if(propertyname.toLowerCase().contains(searchString.toLowerCase()))
                            books.add(searchResultBooks.get(i));
                        myAdapter.notifyDataSetChanged();
                        myBookData.close();
                        Log.e("UpdatedList", String.valueOf(books));
                    }
                }

            }
        });


//        delButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Per amagar el teclat
//                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                boolean trobat = false;
//                myBookData.open();
//                books.clear();
//                books.addAll(myBookData.findBookByTitle(String.valueOf(myEditText.getText())));
//                myBookData.close();
//                myAdapter.notifyDataSetChanged();
//
//                if(books.size() == 0) error_message.setVisibility(View.VISIBLE);
//                else error_message.setVisibility(View.INVISIBLE);
//            }
//        });

        return v;
    }

    @Override
    public void onDialogPositiveClick(final Book b, final int numberOfBooks) {
        myBookData.open();


        int temp = 0;
        boolean trobat = false;

        if(numberOfBooks > 1){
            myBookData.updateBookStock(b,numberOfBooks - 1);
            myAdapter.notifyItemChanged(pos_previa);
            Log.e("Inside", "if");
            updateData();

            llibreAEliminar = b;

            Snackbar snackbar = Snackbar
                    .make(getView(), "Book Sold", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myBookData.open();

                    myBookData.updateBookStock(b,numberOfBooks);
                    myAdapter.notifyItemChanged(pos_previa);
                    Log.e("Inside", "if");
                    updateData();
                }
            });
            snackbar.show();

        }else if(numberOfBooks == 1){
            myBookData.deleteBook(b);
            myBookData.close();
            while (!trobat){
                Book bb = books.get(temp);
                if (bb.getId() == b.getId())trobat = true;
                else ++temp;
            }
            pos_previa = temp;
            books.remove(pos_previa);
            myAdapter.notifyItemRemoved(pos_previa);
            Log.e("Inside", "else");

            llibreAEliminar = b;

            Snackbar snackbar = Snackbar
                    .make(getView(), "Book Deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myBookData.open();

                    Book restaurar = myBookData.createBook(llibreAEliminar.getTitle(),
                            llibreAEliminar.getAuthor(),llibreAEliminar.getPublisher(),
                            llibreAEliminar.getYear(),llibreAEliminar.getCategory(),
                            llibreAEliminar.getPersonal_evaluation());


                    myBookData.close();
                    llibreAEliminar = restaurar;

                    books.add(pos_previa,llibreAEliminar);
                    myAdapter.notifyItemInserted(pos_previa);
                }
            });
            snackbar.show();
        }



    }

    private void updateData() {
        books.clear();
        myBookData.open();
        books.addAll(myBookData.getAllBooks());
        myBookData.close();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("titol_buscat", String.valueOf(myEditText.getText()));
    }

    @Override
    public void onItemClick(Book b) {
        if (deleteBookDialogFragment == null){
            deleteBookDialogFragment = new DeleteBookDialogFragment();
        }
        deleteBookDialogFragment.setParams(me, b);
        deleteBookDialogFragment.show(myFragmentManager, "fragment_delete_book_dialog");
        error_message.setVisibility(View.INVISIBLE);
        llibreAEliminar = b;
    }
}
