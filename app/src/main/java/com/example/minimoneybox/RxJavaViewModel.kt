package com.example.minimoneybox

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class RxJavaViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
    }
}
