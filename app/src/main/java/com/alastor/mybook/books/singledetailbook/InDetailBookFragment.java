package com.alastor.mybook.books.singledetailbook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alastor.mybook.R;
import com.alastor.mybook.databinding.InDetailBookFragmentBinding;
import com.alastor.mybook.repository.api.model.Book;
import com.google.android.material.snackbar.Snackbar;

public class InDetailBookFragment extends Fragment {

    private static final String KEY_BOOK_ID = "key_book_id";

    private String bookId;
    private InDetailBookViewModel inDetailBookViewModel;
    private InDetailBookFragmentBinding inDetailBookFragmentBinding;

    public static InDetailBookFragment newInstance(String id) {
        final InDetailBookFragment inDetailBookFragment = new InDetailBookFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_BOOK_ID, id);
        inDetailBookFragment.setArguments(bundle);
        return inDetailBookFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        final Bundle bundle = getArguments();
        if (bundle == null || (bookId = bundle.getString(KEY_BOOK_ID, "")).isEmpty()) {
            throw new IllegalArgumentException("This fragment require book id provided in arguments. " +
                    "Use newInstance(String id) method to create fragment.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inDetailBookFragmentBinding = InDetailBookFragmentBinding.inflate(inflater, container, false);
        return inDetailBookFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inDetailBookViewModel = new ViewModelProvider(this).get(InDetailBookViewModel.class);

        inDetailBookViewModel.getBook(bookId).observe(getViewLifecycleOwner(), bookResponse -> {
            if (bookResponse == null) {
                return;
            }
            switch (bookResponse.status) {
                case LOADING:
                    inDetailBookFragmentBinding.bookFlipper.setDisplayedChild(0);
                    break;
                case SUCCESS:
                    inDetailBookFragmentBinding.bookFlipper.setDisplayedChild(1);
                    final Book data = bookResponse.data;
                    if (data != null) {
                        setUpViews(data);
                    }
                    break;
                case ERROR:
                    inDetailBookFragmentBinding.bookFlipper.setDisplayedChild(2);
                    Snackbar.make(requireView(), R.string.error_cannot_get_book, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_retry, v -> {
                                inDetailBookViewModel.requestBook(bookId);
                            })
                            .show();
                    break;
            }
        });

        inDetailBookViewModel.getCoverLiveData().observe(getViewLifecycleOwner(), drawableResponse -> {
            if (drawableResponse == null) {
                return;
            }
            switch (drawableResponse.status) {
                case LOADING:
                case ERROR:
                    inDetailBookFragmentBinding.cover.setImageDrawable(
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_banner_foreground));
                    break;
                case SUCCESS:
                    inDetailBookFragmentBinding.cover.setImageDrawable(drawableResponse.data);
                    break;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inDetailBookFragmentBinding = null;
    }

    private void setUpViews(Book book) {
        inDetailBookFragmentBinding.title.setText(book.getTitle());
        inDetailBookFragmentBinding.description.setText(book.getDescription());
    }

}