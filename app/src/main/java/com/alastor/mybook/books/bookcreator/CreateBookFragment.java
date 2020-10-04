package com.alastor.mybook.books.bookcreator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alastor.mybook.databinding.CreateBookFragmentBinding;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createBookFragmentBinding = null;
    }
}