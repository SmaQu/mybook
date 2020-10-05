package com.alastor.mybook.books.bookcreator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alastor.mybook.FragmentAdministrator;
import com.alastor.mybook.R;
import com.alastor.mybook.databinding.CreateBookFragmentBinding;
import com.alastor.mybook.repository.api.model.Book;
import com.google.android.material.textfield.TextInputLayout;

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
                    createBookFragmentBinding.createBookFlipper.setDisplayedChild(1);
                    break;
                case SUCCESS:
                    createBookFragmentBinding.createBookFlipper.setDisplayedChild(0);
                    FragmentAdministrator.popBackStack(getParentFragmentManager());
                    break;
                case ERROR:
                    break;
            }
        });
    }
}