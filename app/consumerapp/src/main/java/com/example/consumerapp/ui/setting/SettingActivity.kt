package com.example.consumerapp.ui.setting

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private var binding :ActivitySettingBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver
    private  lateinit var remainderPreference : SharedPreferences

    companion object {
        private const val DAILY_REMAINDER_TAG = "DAILY REMAINDER"
        const val PREFERENCE_NAME = "SETTING PREFERENCE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        alarmReceiver = AlarmReceiver()
        remainderPreference = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        setSwitch()
        binding!!.swDailyRemainder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setRepeatingAlarm(
                    this,
                    AlarmReceiver.TYPE_REPEATING,
                    getString(R.string.daily_remainder_alarm)
                )
            } else {
                alarmReceiver.cancelAlarm(this)
            }
            saveChange(isChecked)
        }
    }

    private fun setSwitch() {
        binding?.swDailyRemainder?.isChecked = remainderPreference.getBoolean(DAILY_REMAINDER_TAG, false)
    }

    private fun saveChange(value: Boolean) {
        val editor = remainderPreference.edit()
        editor.putBoolean(DAILY_REMAINDER_TAG, value)
        editor.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}