package id.ac.umn.stevenindriano.map_project_group2.ui.setting

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingWrapper(private val sharedPreferences: SharedPreferences) {
    companion object{
        const val KEY_TEXT = "reminderDuration"
    }

    private val valueLiveData = MutableLiveData<Int>()

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener {
                _, key ->
            when(key) {
                KEY_TEXT -> {
                    valueLiveData.postValue(
                        sharedPreferences.getInt(KEY_TEXT, 3)
                    )
                }
            }
        }
    }

    fun saveValue(num: Int) {
        sharedPreferences.edit()
            .putInt(KEY_TEXT, num)
            .apply()
    }

    fun getValue(): LiveData<Int> {
        valueLiveData.postValue(sharedPreferences.getInt(KEY_TEXT, 3))
        return valueLiveData
    }
}