package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.remote.backendApi
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository

class TeacherRepositoryImpl(
        private val backendApi: backendApi,
) : TeacherRepository {
    override suspend fun getAllTeachers(): Result<List<Teacher>> {
        return try {
            val personas = backendApi.getPersonas()
            val teachers =
                    personas.map { p ->
                        Teacher(
                                id = p.id,
                                name = p.name,
                                subject = p.theme,
                                level = "Général",
                                description = p.description ?: "",
                                avatar = null,
                                isOnline = false,
                                rating = 4.5f
                        )
                    }
            Result.success(teachers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTeacherById(id: String): Result<Teacher> {
        return try {
            val persona = backendApi.getPersonaById(id)
            val teacher =
                    Teacher(
                            id = persona.id,
                            name = persona.name,
                            subject = persona.theme,
                            level = "Général",
                            description = persona.description ?: "",
                            avatar = null,
                            isOnline = false,
                            rating = 4.5f
                    )
            Result.success(teacher)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchTeachers(query: String): Result<List<Teacher>> {
        return try {
            val personas = backendApi.getPersonas()
            val filtered =
                    personas
                            .filter { p ->
                                p.name.contains(query, ignoreCase = true) ||
                                        p.theme.contains(query, ignoreCase = true)
                            }
                            .map { p ->
                                Teacher(
                                        id = p.id,
                                        name = p.name,
                                        subject = p.theme,
                                        level = "Général",
                                        description = p.description ?: "",
                                        avatar = null,
                                        isOnline = false,
                                        rating = 4.5f
                                )
                            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
