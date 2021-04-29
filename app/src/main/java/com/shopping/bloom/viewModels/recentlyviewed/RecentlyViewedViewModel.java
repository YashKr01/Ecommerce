package com.shopping.bloom.viewModels.recentlyviewed;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.repository.RecentlyViewedRepository;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;

import java.util.List;

public class RecentlyViewedViewModel extends AndroidViewModel {

    private RecentlyViewedRepository repository;
    private Application application;

    public RecentlyViewedViewModel(@NonNull Application application) {
        super(application);
        this.repository = new RecentlyViewedRepository();
        this.application = application;
    }

    public LiveData<List<RecentlyViewedItem>> getRecentlyViewedList(
            String pageNo,
            String limit
    ) {
        return repository.getRecentlyViewedList(application, pageNo, limit);
    }

}
