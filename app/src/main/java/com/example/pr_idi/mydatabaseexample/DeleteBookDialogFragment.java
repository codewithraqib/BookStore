package com.example.pr_idi.mydatabaseexample;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class DeleteBookDialogFragment extends DialogFragment {

    private Book b;
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(Book b, int newVal);
    }

    // Use this instance of the interface to deliver action events
    private NoticeDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.delete_book_dialog_layout,null);

        if(savedInstanceState != null) {
            b = (Book) savedInstanceState.getSerializable("llibre_guardat");
        }

        TextView autor = view.findViewById(R.id.textView6);
        TextView titol = view.findViewById(R.id.textView2);
        TextView shelfNumber = view.findViewById(R.id.textView4);
        TextView any = view.findViewById(R.id.textView3);
        RatingBar valoracio = view.findViewById(R.id.ratingBar);
        TextView categoria = view.findViewById(R.id.textView5);

        autor.setText(b.getAuthor());
        titol.setText(b.getTitle());
        shelfNumber.setText(b.getPublisher());
        any.setText(String.valueOf(b.getYear()));
        categoria.setText(b.getCategory());

        switch (b.getPersonal_evaluation()){
            case "molt dolent":
                valoracio.setRating(1);
                break;
            case "dolent":
                valoracio.setRating(2);
                break;
            case "regular":
                valoracio.setRating(3);
                break;
            case "bo":
                valoracio.setRating(4);
                break;
            case "molt bo":
                valoracio.setRating(5);
                break;
        }

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setTitle("Are You Sure You Want To Delete This Book?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int numberOfBooks = b.getYear();
                mListener.onDialogPositiveClick(b, numberOfBooks);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    public void setParams(NoticeDialogListener mListener, Book b) {
        this.mListener = mListener;
        this.b = b;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("llibre_guardat",b);
    }
}
