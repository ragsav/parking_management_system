package com.example.se1.MallListViewModel;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MallViewModelFactory implements ViewModelProvider.Factory {
    ProgressBar progressBar;

    public MallViewModelFactory(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MallDataViewModel(progressBar);
    }
}
