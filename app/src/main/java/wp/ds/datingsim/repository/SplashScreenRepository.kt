package wp.ds.datingsim.repository

import android.content.Context

class SplashScreenRepository(context: Context) {

    suspend fun checkIsAuth(): Boolean{
        return true
    }
}