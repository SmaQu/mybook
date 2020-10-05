package com.alastor.mybook.books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alastor.mybook.FragmentAdministrator;
import com.alastor.mybook.R;
import com.alastor.mybook.books.bookcreator.CreateBookFragment;
import com.alastor.mybook.books.singledetailbook.InDetailBookFragment;
import com.alastor.mybook.databinding.BooksFragmentBinding;
import com.google.android.material.snackbar.Snackbar;

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
        booksFragmentBinding.booksRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return booksFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        requestBooks();

        booksFragmentBinding.fab.setOnClickListener(v ->
                FragmentAdministrator.replaceFragment(getParentFragmentManager(),
                        R.id.fragment_container,
                        CreateBookFragment.newInstance()));

        booksFragmentBinding.swipeRefresh.setOnRefreshListener(this::requestBooks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        booksFragmentBinding = null;
    }

    private void requestBooks() {
        booksViewModel.getBooks().observe(getViewLifecycleOwner(), listResponse -> {
            if (listResponse == null) {
                return;
            }
            switch (listResponse.status) {
                case LOADING:
                    if (!booksFragmentBinding.swipeRefresh.isRefreshing()) {
                        booksFragmentBinding.swipeRefresh.setRefreshing(true);
                    }
                    booksFragmentBinding.booksRecycleView.setVisibility(View.VISIBLE);
                    booksFragmentBinding.recyclerPlaceHolder.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    booksAdapter.setListOfBooks(listResponse.data);
                    booksFragmentBinding.swipeRefresh.setRefreshing(false);
                    break;
                case ERROR:
                    booksFragmentBinding.booksRecycleView.setVisibility(View.GONE);
                    booksFragmentBinding.recyclerPlaceHolder.setVisibility(View.VISIBLE);
                    booksFragmentBinding.swipeRefresh.setRefreshing(false);
                    Snackbar.make(requireView(), R.string.error_cannot_get_books, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_retry, v -> {
                                booksViewModel.requestBooks();
                            })
                            .show();
                    break;
            }
        });
    }

    private SwipeToDeleteGridViewCallback getSwipeToDeleteCallback() {
        return new SwipeToDeleteGridViewCallback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final String id = booksAdapter.getBookIdAtPosition(viewHolder.getAdapterPosition());
                booksViewModel.removeBook(id).observe(getViewLifecycleOwner(), stringResponse -> {
                    if (stringResponse == null) {
                        return;
                    }
                    switch (stringResponse.status) {
                        case LOADING:
                            booksFragmentBinding.swipeRefresh.setRefreshing(true);
                            break;
                        case SUCCESS:
                            booksAdapter.removeItem(id);
                            booksFragmentBinding.swipeRefresh.setRefreshing(false);
                            break;
                        case ERROR:
                            booksAdapter.refreshItem(id);
                            booksFragmentBinding.swipeRefresh.setRefreshing(false);
                            Snackbar.make(requireView(), R.string.error_cannot_delete_book, Snackbar.LENGTH_LONG)
                                    .show();
                            break;
                    }
                });
            }
        };
    }

    private BooksAdapter.BooksListener getBooksListener() {
        return id -> FragmentAdministrator.replaceFragment(getParentFragmentManager(),
                R.id.fragment_container,
                InDetailBookFragment.newInstance(id));
    }
}