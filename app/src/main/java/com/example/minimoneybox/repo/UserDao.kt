package com.example.minimoneybox.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.minimoneybox.repo.model.User
import io.reactivex.Completable

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: User): Completable

    @Query("UPDATE User SET signedIn = 1")
    abstract fun signIn(): Completable

    @Query("SELECT * FROM USER LIMIT 1")
    abstract fun getCurrentUser(): LiveData<User?>

    @Query("SELECT * FROM USER LIMIT 1")
    abstract fun getCurrentUserSync(): User?

    @Query("UPDATE USER SET BearerToken = :bearer")
    abstract fun updateBearer(bearer: String)

    @Query("DELETE FROM USER")
    abstract fun clear(): Completable
}
