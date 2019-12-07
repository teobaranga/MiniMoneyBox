package com.example.minimoneybox.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.minimoneybox.repo.model.LoginInfo
import io.reactivex.Completable

@Dao
interface LoginInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loginInfo: LoginInfo): Completable

    @Query("SELECT * FROM LOGININFO LIMIT 1")
    fun get(): LiveData<LoginInfo>

    @Query("SELECT * FROM LOGININFO LIMIT 1")
    fun getSync(): LoginInfo?
}
