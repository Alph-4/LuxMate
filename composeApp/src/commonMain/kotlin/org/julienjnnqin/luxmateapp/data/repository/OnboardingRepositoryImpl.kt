package org.julienjnnqin.luxmateapp.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.julienjnnqin.luxmateapp.domain.entity.OnboardingState
import org.julienjnnqin.luxmateapp.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(private val settings: Settings) : OnboardingRepository {
    private var isCompleted = false

    companion object {
        private const val KEY_ONBOARDING = "pref_onboarding_done"
    }

    override fun getOnboardingState(): OnboardingState {
        // Retourne false par défaut si la clé n'existe pas
        val value = settings.getBoolean(KEY_ONBOARDING, defaultValue = false)
        return OnboardingState(isCompleted = value)
    }

    override fun setOnboardingCompleted(): Result<Unit> {
        return try {
            settings[KEY_ONBOARDING] = true
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

