package wp.ds.datingsim.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wp.ds.datingsim.repository.SplashScreenRepository
import kotlin.random.Random

class SplashScreenViewModel(application: Application) :AndroidViewModel(application) {

    private val repository: SplashScreenRepository = SplashScreenRepository(application)

    private val checkLiveData = MutableLiveData<Boolean>()

    val check: LiveData<Boolean>
        get() = checkLiveData

    fun letsCheck(){
        viewModelScope.launch {
            delay(3000)
            checkLiveData.postValue(repository.checkIsAuth())
        }
    }

}