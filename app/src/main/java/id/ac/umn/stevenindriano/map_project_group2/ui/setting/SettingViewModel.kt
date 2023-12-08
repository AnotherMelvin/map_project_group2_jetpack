package id.ac.umn.stevenindriano.map_project_group2.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class SettingViewModel(private val settingWrapper: SettingWrapper): ViewModel() {
    fun saveValue(num: Int) {
        settingWrapper.saveValue(num)
    }

    fun getValue(): LiveData<Int> {
        return settingWrapper.getValue()
    }
}