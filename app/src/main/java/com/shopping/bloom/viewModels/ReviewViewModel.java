package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.repository.ReviewRepository;
import com.shopping.bloom.model.review.ReviewModel;

public class ReviewViewModel extends AndroidViewModel {

    private Application application;
    private ReviewRepository repository;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.repository = new ReviewRepository();
    }

    public LiveData<ReviewModel> getReviews(String productId,
                                            String limit, String page) {
        return repository.getReviews(application, productId, limit, page);
    }

}
