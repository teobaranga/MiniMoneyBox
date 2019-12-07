package com.example.minimoneybox.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.minimoneybox.R
import com.example.minimoneybox.RxImmediateSchedulerRule
import com.example.minimoneybox.api.MoneyBoxApi
import com.example.minimoneybox.di.testModule
import com.example.minimoneybox.repo.UserDao
import com.example.minimoneybox.repo.model.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import retrofit2.HttpException

class LoginViewModelTest : KoinTest {

    private val module = module {
        factory { LoginViewModel(get(), get(), get(), get()) }
    }

    /**
     * Allows LiveData value changes in unit tests without the need for Android SDK classes.
     */
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    /**
     * Converts RxJava async operations into sync ones.
     */
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @MockK
    private lateinit var currentUser: LiveData<User?>

    @Before
    fun before() {
        MockKAnnotations.init(this)
        startKoin { modules(listOf(testModule, module)) }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `login - no email and no password - no API call`() {

        every { get<UserDao>().getCurrentUser() } returns currentUser

        val loginViewModel = get<LoginViewModel>()

        loginViewModel.login()

        verify(exactly = 0) { get<MoneyBoxApi>().login(any()) }
    }

    @Test
    fun `login - 401 Unauthorized - error message correct`() {

        every { get<UserDao>().getCurrentUser() } returns currentUser

        val httpException = mockk<HttpException>()
        every { httpException.code() } returns 401
        every { get<MoneyBoxApi>().login(any()) } returns Single.error(httpException)

        val loginViewModel = get<LoginViewModel>()
        loginViewModel.email.value = "test@test.com"
        loginViewModel.password.value = "1234"

        loginViewModel.login()

        verify(exactly = 1) { get<MoneyBoxApi>().login(any()) }

        assert(loginViewModel.errorMessage.value == R.string.error_401)
    }
}
