# MiniMoneyBox

This is a mini version of the Moneybox app that allows existing users to login, check their account and add money to their moneybox.

The project contains:
 - a LoginActivity with 3 input fields for credentials and an optional name, a sign-in Button and a lovely animation of an owl that plays on the press of the button.
 - a MainActivity that displays the current user's optional name, total plan value, as well as all the owned accounts with their respective details.
 - an AccountActivity that displays the details for a selected account and allows adding £10 to its MoneyBox with the press of a button.

The application follows the MVVM architecture, using databinding coupled with Android ViewModels.

## Setup

After cloning the repository, there is only one step that needs to be done before building and running the application.
The file `local.properties` must be created if it doesn't exist and a property must be added to indicate the App Id to be used with the MoneyBox API:

```
moneybox.appId=<app_id>
```

## Usage

The application is straightforward to use. From the LoginActivity, the user must input the right credentials to login.
Special error messages are provided if credentials are incorrectly formatted or if they are invalid, while a generic error message is provided for any unexpected scenarios.

Once the sign in is successful, the user is brought to the main screen. The user can sign out with the X button on the top right or click on one of the accounts for more details.

On the account details screen, the user can add £10 to the selected account.

## Technologies used:
 - Room for persisting login information, user information, and product data across application launches and screen transitions.
 - Retrofit + RxJava for accessing the MoneyBox API asynchronously.
 - LiveData for easy coupling of the data with the various views / screens of the app through databinding.
 - Koin for dependency injection, allowing dependencies to be easily mocked for unit tests.
