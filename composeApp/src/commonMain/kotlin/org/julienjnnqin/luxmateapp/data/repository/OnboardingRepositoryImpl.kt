package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.domain.entity.OnboardingState
import org.julienjnnqin.luxmateapp.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl : OnboardingRepository {
    private var isCompleted = false

    override suspend fun getOnboardingState(): OnboardingState {
        return OnboardingState(isCompleted = isCompleted)
    }

    override suspend fun setOnboardingCompleted(): Result<Unit> {
        return try {
            isCompleted = true
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

