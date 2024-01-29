package com.financialcompanion.android.auth.domain.usecase

import com.financialcompanion.android.auth.data.prefs.AuthPrefs

class UserIsAuthorizationUseCase {

    fun setUserIsAuthorization(value: Boolean) {
        AuthPrefs.isAuthorization = value
    }

    fun getUserIsAuthorization(): Boolean {
        return AuthPrefs.isAuthorization
    }
}