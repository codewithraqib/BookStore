package com.example.pr_idi.mydatabaseexample;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BookRecyclerViewAdapterWithOnClickListener extends RecyclerView.Adapter<BookRecyclerViewAdapterWithOnClickListener.BookViewHolder> {

    private ArrayList<Book> myBooks;
    private Interaction myInteraction;

    public interface Interaction{
        public void onItemClick(Book b);
    }

    public BookRecyclerViewAdapterWithOnClickListener(ArrayList<Book> myBooks, Interaction i) {
        this.myBooks = myBooks;
        this.myInteraction = i;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item_layout_v2,parent,false);
        BookViewHolder bookViewHolder = new BookViewHolder(view);
        return bookViewHolder;

    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {

        Book book = myBooks.get(position);

        holder.author.setText(book.getAuthor());
        holder.title.setText(book.getTitle());
        holder.quantity.setText(String.valueOf(book.getYear()));
        holder.publisher.setText(book.getPublisher());
        holder.category.setText(book.getCategory());
        holder.personal_evaluation.setText(book.getPersonal_evaluation());

    }

//    private int toStarts(String s){
//        switch (s) {
//            case "molt bo":
//                return 5;
//            case "bo":
//                return 4;
//            case "regular":
//                return 3;
//            case "dolent":
//                return 2;
//            case "molt dolent":
//                return 1;
//        }
//        return 0;
//    }


    @Override
    public int getItemCount() {
        return myBooks.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        private CardView view;
        private TextView author;
        private TextView title;
        private TextView quantity;
        private TextView publisher;
        private TextView category;
        private TextView personal_evaluation;


        BookViewHolder(View itemView) {
            super(itemView);

            view =  itemView.findViewById(R.id.card_view);
            author =  view.findViewById(R.id.textView6);
            title =  view.findViewById(R.id.textView2);
            quantity =  view.findViewById(R.id.textView3);
            publisher =  view.findViewById(R.id.textView4);
            category =  view.findViewById(R.id.textView5);
            personal_evaluation =  view.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myInteraction.onItemClick(myBooks.get(getAdapterPosition()));
                }
            });

        }
    }
}
