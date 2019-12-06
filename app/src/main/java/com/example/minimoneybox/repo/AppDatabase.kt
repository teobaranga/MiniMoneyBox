package com.example.minimoneybox.repo

import android.content.Context
import androidx.annotation.MainThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.minimoneybox.repo.model.LoginInfo
import com.example.minimoneybox.repo.model.Product
import com.example.minimoneybox.repo.model.ProductsData
import com.example.minimoneybox.repo.model.User

@Database(entities = [LoginInfo::class, User::class, ProductsData::class, Product::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private lateinit var sInstance: AppDatabase

        @MainThread
        fun get(applicationContext: Context): AppDatabase {
            sInstance = if (::sInstance.isInitialized) sInstance else Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "app-database"
            ).fallbackToDestructiveMigration().build()
            return sInstance
        }
    }

    abstract fun loginInfoDao(): LoginInfoDao

    abstract fun userDao(): UserDao

    abstract fun productsDao(): ProductsDao
}
