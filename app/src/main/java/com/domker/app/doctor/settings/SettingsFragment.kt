package com.domker.app.doctor.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import com.domker.app.doctor.R
import com.domker.app.doctor.store.AppSettings
import com.domker.base.sp.settingValueOf
import kotlinx.coroutines.launch

const val KEY_INCLUDE_SYSTEM_APP = "include_system_app"
const val VALUE_INCLUDE_SYSTEM_APP_DEFAULT = false
const val KEY_SKIN_MODE = "skin_mode"
const val VALUE_SKIN_MODE_DEFAULT = "close"
const val KEY_APP_LIST_MODE = "app_list_mode"
const val VALUE_APP_LIST_MODE_DEFAULT = "list"

/**
 * 设置界面
 */
class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        initCallback(requireContext())
    }

    private fun initCallback(context: Context) {
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener { sp, key ->
            val v = when (key) {
                // Boolean类型的值
                KEY_INCLUDE_SYSTEM_APP -> sp.settingValueOf(key, VALUE_INCLUDE_SYSTEM_APP_DEFAULT)
                KEY_SKIN_MODE -> sp.settingValueOf(key, VALUE_SKIN_MODE_DEFAULT)
                KEY_APP_LIST_MODE -> sp.settingValueOf(key, VALUE_APP_LIST_MODE_DEFAULT).also { s ->
                    // 提前获取Context，避免协程异步获取不到
                    lifecycleScope.launch {
                        AppSettings.setAppListStyle(context, s.value)
                    }
                }
                else -> sp.settingValueOf(key, "")
            }
            println("[SettingsFragment] changed: $key ${v.value}")
            settingsViewModel.onChange(v)
        }
    }

}