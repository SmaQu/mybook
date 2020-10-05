package com.alastor.mybook.books.bookcreator;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alastor.mybook.FragmentAdministrator;
import com.alastor.mybook.R;
import com.alastor.mybook.databinding.CreateBookFragmentBinding;
import com.alastor.mybook.repository.api.model.Book;
import com.google.android.material.snackbar.Snackbar;

public class CreateBookFragment extends Fragment {

    private CreateBookViewModel createBookViewModel;
    private CreateBookFragmentBinding createBookFragmentBinding;

    public static CreateBookFragment newInstance() {
        return new CreateBookFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        createBookFragmentBinding = CreateBookFragmentBinding.inflate(inflater, container, false);
        return createBookFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createBookViewModel = new ViewModelProvider(this).get(CreateBookViewModel.class);
        createBookFragmentBinding.save.setOnClickListener(v -> {
            final Book book = createBookIfValidated();
            if (book != null) {
                makeRequest(book);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createBookFragmentBinding = null;
    }

    private Book createBookIfValidated() {
        if (validateViews()) {
            final String title = createBookFragmentBinding.titleText
                    .getText().toString();
            final String description = createBookFragmentBinding.descriptionText
                    .getText().toString();
            final String coverUrlText = createBookFragmentBinding.coverUrlText
                    .getText().toString();
            return new Book(title, description, coverUrlText);
        }

        return null;
    }

    private boolean validateViews() {
        boolean valid = true;
        if (createBookFragmentBinding.titleText.getText().toString().isEmpty()) {
            createBookFragmentBinding.title.setError(getString(R.string.label_required));
            valid = false;
        } else {
            createBookFragmentBinding.title.setError(null);
        }

        if (createBookFragmentBinding.descriptionText.getText().toString().isEmpty()) {
            createBookFragmentBinding.description.setError(getString(R.string.label_required));
            valid = false;
        } else {
            createBookFragmentBinding.description.setError(null);
        }
        return valid;
    }

    private void makeRequest(Book book) {
        createBookViewModel.createBoot(book).observe(getViewLifecycleOwner(), stringResponse -> {
            if (stringResponse == null) {
                return;
            }
            switch (stringResponse.status) {
                case LOADING:
                    hideKeyboard(requireActivity());
                    createBookFragmentBinding.createBookFlipper.setDisplayedChild(1);
                    break;
                case SUCCESS:
                    createBookFragmentBinding.createBookFlipper.setDisplayedChild(0);
                    FragmentAdministrator.popBackStack(getParentFragmentManager());
                    break;
                case ERROR:
                    createBookFragmentBinding.createBookFlipper.setDisplayedChild(0);
                    Snackbar.make(requireView(), R.string.error_cannot_create_book, Snackbar.LENGTH_LONG)
                            .show();
                    break;
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}