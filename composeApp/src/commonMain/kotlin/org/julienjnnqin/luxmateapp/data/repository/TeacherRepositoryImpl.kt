package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.core.utils.TeacherLevel
import org.julienjnnqin.luxmateapp.core.utils.TeacherTheme
import org.julienjnnqin.luxmateapp.data.remote.BackendApi
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository

class TeacherRepositoryImpl(
    private val backendApi: BackendApi,
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
                        level = TeacherLevel.ADVANCED,
                        description = p.description ?: "",
                        avatar = p.avatarUrl ?: "https://ui-avatars.com/api/?name=${
                            p.name.replace(
                                " ",
                                "+"
                            )
                        }&background=random",
                        rating = 4.5f,
                        theme = TeacherTheme.ART
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
                    level = TeacherLevel.BEGINNER,
                    description = persona.description ?: "",
                    avatar = persona.avatarUrl ?: "https://ui-avatars.com/api/?name=${
                        persona.name.replace(
                            " ",
                            "+"
                        )
                    }&background=random",

                    rating = 4.5f,
                    theme = TeacherTheme.ART

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
                            level = TeacherLevel.INTERMEDIATE,
                            description = p.description ?: "",
                            avatar = p.avatarUrl ?: "https://ui-avatars.com/api/?name=${
                                p.name.replace(
                                    " ",
                                    "+"
                                )
                            }&background=random",
                            rating = 4.5f,
                            theme = TeacherTheme.ART
                        )
                    }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
