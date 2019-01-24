package com.example.pr_idi.mydatabaseexample;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;


public class ChangeValorationDialogFragment extends DialogFragment {


    private TextView autor;
    private TextView titol;
    private TextView shelfNumber;
    private TextView quantity;
    private TextView categoria;
    private TextView valoracio;
    private Book b;

    //private RatingBar ratingBar;
    private EditText changeStockET;
    //private TextView ratingBarTextView;
    private String t = "poor";
    private int r = 1;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(Book b, String newVal);
    }
    // Use this instance of the interface to deliver action events
    private ChangeValorationDialogFragment.NoticeDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_change_valoration_dialog,null);

        if(savedInstanceState != null) {
            b = (Book) savedInstanceState.getSerializable("llibre_guardat");
        }

        autor =  view.findViewById(R.id.textView6);
        titol =  view.findViewById(R.id.textView2);
        shelfNumber =  view.findViewById(R.id.textView4);
        quantity =  view.findViewById(R.id.textView3);
        valoracio =  view.findViewById(R.id.ratingBar);
        categoria =  view.findViewById(R.id.textView5);


        autor.setText(b.getAuthor());
        titol.setText(b.getTitle());
        shelfNumber.setText(b.getPublisher());
        quantity.setText(String.valueOf(b.getYear()));
        categoria.setText(b.getCategory());
        valoracio.setText(b.getPersonal_evaluation());


//        switch (b.getPersonal_evaluation()){
//            case "molt dolent":
//                valoracio.setRating(1);
//                break;
//            case "dolent":
//                valoracio.setRating(2);
//                break;
//            case "regular":
//                valoracio.setRating(3);
//                break;
//            case "bo":
//                valoracio.setRating(4);
//                break;
//            case "molt bo":
//                valoracio.setRating(5);
//                break;
//        }
        changeStockET = view.findViewById(R.id.changeStockET);
        //ratingBar =  view.findViewById(R.id.ratingBarDialog);
        //ratingBarTextView =  view.findViewById(R.id.stateRatingBar);

//        if (savedInstanceState == null) {
//            ratingBarTextView.setText(R.string.rating_one);
//            ratingBar.setRating(1);
//        }
//        else {
//            ratingBarTextView.setText(savedInstanceState.getCharSequence("state_rating"));
//        }


//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                if(fromUser){
//                    if (rating < 1) rating = 1;
//                    switch ((int) rating){
//                        case 1:
//                            t = "poor";
//                            break;
//                        case 2:
//                            t = "average";
//                            break;
//                        case 3:
//                            t = "regular";
//                            break;
//                        case 4:
//                            t = "good";
//                            break;
//                        case 5:
//                            t = "best";
//                            break;
//                    }
//                    r = (int) rating;
//                    //ratingBarTextView.setText(t);
//                    ratingBar.setRating(rating);
//                }
//            }
//        });

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setTitle("Add New Stock For The Given Unit");
        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // int ratingNum = (int) ratingBar.getRating();
                String newStock = changeStockET.getText().toString();
//                String tt = "molt dolent";
//                switch (ratingNum){
//                    case 1:
//                        tt = "molt dolent";
//                        break;
//                    case 2:
//                        tt = "dolent";
//                        break;
//                    case 3:
//                        tt = "regular";
//                        break;
//                    case 4:
//                        tt = "bo";
//                        break;
//                    case 5:
//                        tt = "molt bo";
//                        break;
//                }
                mListener.onDialogPositiveClick(b,newStock);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog d = alertDialogBuilder.create();

        return d;
    }

    public void setParams(NoticeDialogListener mListener, Book b) {
        this.mListener = mListener;
        this.b = b;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("llibre_guardat",b);
        //outState.putCharSequence("state_rating",ratingBarTextView.getText().toString());
    }

    public void setListener (NoticeDialogListener mListener) {
        this.mListener = mListener;
    }

}
