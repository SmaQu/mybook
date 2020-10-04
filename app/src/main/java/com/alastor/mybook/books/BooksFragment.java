package com.alastor.mybook.books;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alastor.mybook.FragmentAdministrator;
import com.alastor.mybook.R;
import com.alastor.mybook.books.bookcreator.CreateBookFragment;
import com.alastor.mybook.books.singledetailbook.InDetailBookFragment;
import com.alastor.mybook.databinding.BooksFragmentBinding;

public class BooksFragment extends Fragment {

    private BooksViewModel booksViewModel;
    private BooksFragmentBinding booksFragmentBinding;
    private BooksAdapter booksAdapter;

    public static BooksFragment newInstance() {
        return new BooksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        booksFragmentBinding = BooksFragmentBinding.inflate(inflater, container, false);
        booksAdapter = new BooksAdapter();
        booksAdapter.setBooksListener(getBooksListener());
        booksFragmentBinding.booksRecycleView.setAdapter(booksAdapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getSwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(booksFragmentBinding.booksRecycleView);
        return booksFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);

        booksViewModel.getBooks().observe(getViewLifecycleOwner(), listResponse -> {
            if (listResponse == null) {
                return;
            }
            switch (listResponse.status) {
                case LOADING:
                    booksFragmentBinding.booksFlipper.setDisplayedChild(0);
                    break;
                case SUCCESS:
                    booksAdapter.setListOfBooks(listResponse.data);
                    booksFragmentBinding.booksFlipper.setDisplayedChild(1);
                    break;
                case ERROR:
                    break;
            }
        });

        booksFragmentBinding.fab.setOnClickListener(v ->
                FragmentAdministrator.replaceFragment(getParentFragmentManager(),
                        R.id.fragment_container,
                        CreateBookFragment.newInstance()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        booksFragmentBinding = null;
    }

    private SwipeToDeleteCallback getSwipeToDeleteCallback() {
        return new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final String id = booksAdapter.getBookIdAtPosition(viewHolder.getAdapterPosition());
                booksViewModel.removeBook(id).observe(getViewLifecycleOwner(), stringResponse -> {
                    if (stringResponse == null) {
                        return;
                    }
                    switch (stringResponse.status) {
                        case LOADING:
                            break;
                        case SUCCESS:
                            booksAdapter.removeItem(id);
                            break;
                        case ERROR:
                            break;
                    }
                });
            }
        };
    }

    private BooksAdapter.BooksListener getBooksListener() {
        return id -> {
            FragmentAdministrator.replaceFragment(getParentFragmentManager(),
                    R.id.fragment_container,
                    InDetailBookFragment.newInstance(id));
        };
    }

}