package com.example.minimoneybox.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.minimoneybox.repo.model.LoginInfo
import io.reactivex.Completable

/**
 * Interface that allows CRUD operation on [LoginInfo] data.
 */
@Dao
abstract class LoginInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(loginInfo: LoginInfo): Completable

    @Query("SELECT * FROM LOGININFO LIMIT 1")
    abstract fun get(): LiveData<LoginInfo>

    @Query("SELECT * FROM LOGININFO LIMIT 1")
    abstract fun getSync(): LoginInfo?

    @Query("DELETE FROM LOGININFO")
    abstract fun clear(): Completable
}
