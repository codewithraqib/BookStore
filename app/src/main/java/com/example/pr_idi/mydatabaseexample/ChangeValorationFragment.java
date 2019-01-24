package com.example.pr_idi.mydatabaseexample;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class ChangeValorationFragment extends Fragment implements BookRecyclerViewAdapterWithOnClickListener.Interaction,
                                                                    ChangeValorationDialogFragment.NoticeDialogListener {


    private RecyclerView.Adapter myAdapter;
    private BookData myBookData;
    private ChangeValorationFragment me;
    private ArrayList<Book> myBooks = new ArrayList<>();
    private List<Book> searchResultBooks = new ArrayList<>();
    private ChangeValorationDialogFragment changeValorationDialogFragment;
    private FragmentManager myFragmentManager;
    EditText editTextTitleChangeStock;
//    Button buttonChangeStockSearch;
    TextView errorTextViewInUpdateStock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        me = this;
        View v = inflater.inflate(R.layout.fragment_change_valoration, container, false);

        getResources().getConfiguration();
        RecyclerView.LayoutManager myLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            myLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        else{
            myLayoutManager = new LinearLayoutManager(inflater.getContext());
        }
        RecyclerView myRecyclerView =  v.findViewById(R.id.book_recycler_view_valoration);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new BookRecyclerViewAdapterWithOnClickListener(myBooks, me);
        myRecyclerView.setAdapter(myAdapter);
        myBookData = new BookData(getActivity().getApplicationContext());
        myBookData.open();
        myBooks.clear();
        myBooks.addAll(myBookData.getAllBooks());
        myBookData.close();
        myAdapter.notifyDataSetChanged();
        editTextTitleChangeStock = v.findViewById(R.id.editTextTitleChangeStock);
//        buttonChangeStockSearch = v.findViewById(R.id.buttonChangeStockSearch);
        errorTextViewInUpdateStock = v.findViewById(R.id.errorTextViewInUpdateStock);

        myFragmentManager = getActivity().getSupportFragmentManager();


        if (savedInstanceState != null) {
            changeValorationDialogFragment = (ChangeValorationDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("fragment_change_valoration_dialog");
            if (changeValorationDialogFragment != null){
                changeValorationDialogFragment.setListener(me);
            }
        }


        editTextTitleChangeStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                myBookData.open();

                searchResultBooks = myBookData.getAllBooks();
                myBooks.clear();

                Log.e("NewList", String.valueOf(searchResultBooks));

                String searchString = editTextTitleChangeStock.getText().toString();
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

                String searchString = editTextTitleChangeStock.getText().toString();
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


//        buttonChangeStockSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                myBookData.open();
//                myBooks.clear();
//                myBooks.addAll(myBookData.findBookByTitle(String.valueOf(editTextTitleChangeStock.getText())));
//                myBookData.close();
//                myAdapter.notifyDataSetChanged();
//
//                if(myBooks.size() == 0) errorTextViewInUpdateStock.setVisibility(View.VISIBLE);
//                else errorTextViewInUpdateStock.setVisibility(View.INVISIBLE);
//            }
//        });


        return v;
    }

    @Override
    public void onItemClick(Book b) {
        if (changeValorationDialogFragment == null) changeValorationDialogFragment = new ChangeValorationDialogFragment();
        changeValorationDialogFragment.setParams(me,b);
        changeValorationDialogFragment.show(myFragmentManager, "fragment_change_valoration_dialog");
    }

    private void updateData() {
        myBooks.clear();
        myBookData.open();
        myBooks.addAll(myBookData.getAllBooks());
        myBookData.close();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClick(Book b, String newVal) {
        myBookData.open();
        myBookData.updateValoracion(b,newVal);
        myBookData.close();
        updateData();
    }
}
