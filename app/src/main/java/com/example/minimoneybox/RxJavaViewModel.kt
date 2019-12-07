package com.example.minimoneybox

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * [ViewModel] that allows disposing of any subscriptions created in subclasses when the ViewModel is cleared.
 * This aims to prevent memory leaks.
 */
abstract class RxJavaViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
    }
}
