package com.shopping.bloom.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shopping.bloom.model.NotificationModel;
import com.shopping.bloom.restService.response.NotificationResponse;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM notification")
    List<NotificationResponse> getNotification();

    @Insert
    void insertNotification(NotificationModel notificationmodel);

}
