package com.alastor.mybook.books;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alastor.mybook.R;
import com.alastor.mybook.repository.api.model.Book;

import java.util.Collections;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {

    private List<Book> listOfBooks = Collections.emptyList();
    private BooksListener booksListener;

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_books, parent, false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        final Book book = listOfBooks.get(position);
        holder.titleTv.setText(book.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (booksListener != null) {
                booksListener.onBookClick(book.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfBooks.size();
    }

    public void setListOfBooks(List<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
        notifyDataSetChanged();
    }

    public String getBookIdAtPosition(int index) {
        return listOfBooks.get(index).getId();
    }

    public void removeItem(String id) {
        final int position = getPositionOf(id);
        if (position != -1) {
            listOfBooks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void refreshItem(String id) {
        final int position = getPositionOf(id);
        if (position != -1) {
            notifyItemChanged(position);
        }
    }

    public void setBooksListener(BooksListener booksListener) {
        this.booksListener = booksListener;
    }

    private int getPositionOf(String id) {
        for (int index = 0; index < listOfBooks.size(); index++) {
            if (listOfBooks.get(index).getId().equals(id)) {
                return index;
            }
        }
        return -1;
    }

    public interface BooksListener {
        void onBookClick(String id);
    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title);
        }
    }
}
