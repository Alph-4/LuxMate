package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.remote.KtorbackendApi
import org.julienjnnqin.luxmateapp.domain.entity.Teacher
import org.julienjnnqin.luxmateapp.domain.repository.TeacherRepository

class TeacherRepositoryImpl(
    private val backendApi: KtorbackendApi,
    ) : TeacherRepository {
    private val mockTeachers = listOf(
        Teacher(
            id = "teacher_001",
            name = "Pierre",
            subject = "Mathématiques",
            level = "Collégial",
            description = "Méthodique et concret.",
            avatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnvLJFxDYQKxPYe8CliPjg3bdgJk2s33Zx5sX-RLKYh-InxHw5Ngjc0GweMCAeBvJLCqwsdqE-0gBXz5itTs1bMgJ3Htok09dK4Ulnp7DgkxXYOo1QWzNiUUjoNE2I2884no8qWF-HzdfsIguuYDpHTL4p3mZBhnBC3FQPdRSJwuzOA-1CdH7Rsnuq1IU9O7i1-0D1J8kgwpgXolHicctrf8rIABp2I3Lk_Zuiso081LE97xSRypjOxCPlyCnWlRcwIEw5iSWg8FW6",
            isOnline = true,
            rating = 4.8f
        ),
        Teacher(
            id = "teacher_002",
            name = "Marie",
            subject = "Physique",
            level = "Lycéen",
            description = "Experte en sciences naturelles.",
            avatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnvLJFxDYQKxPYe8CliPjg3bdgJk2s33Zx5sX-RLKYh-InxHw5Ngjc0GweMCAeBvJLCqwsdqE-0gBXz5itTs1bMgJ3Htok09dK4Ulnp7DgkxXYOo1QWzNiUUjoNE2I2884no8qWF-HzdfsIguuYDpHTL4p3mZBhnBC3FQPdRSJwuzOA-1CdH7Rsnuq1IU9O7i1-0D1J8kgwpgXolHicctrf8rIABp2I3Lk_Zuiso081LE97xSRypjOxCPlyCnWlRcwIEw5iSWg8FW6",
            isOnline = false,
            rating = 4.9f
        ),
        Teacher(
            id = "teacher_003",
            name = "Jean",
            subject = "Français",
            level = "Primaire",
            description = "Passion pour la littérature.",
            avatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnvLJFxDYQKxPYe8CliPjg3bdgJk2s33Zx5sX-RLKYh-InxHw5Ngjc0GweMCAeBvJLCqwsdqE-0gBXz5itTs1bMgJ3Htok09dK4Ulnp7DgkxXYOo1QWzNiUUjoNE2I2884no8qWF-HzdfsIguuYDpHTL4p3mZBhnBC3FQPdRSJwuzOA-1CdH7Rsnuq1IU9O7i1-0D1J8kgwpgXolHicctrf8rIABp2I3Lk_Zuiso081LE97xSRypjOxCPlyCnWlRcwIEw5iSWg8FW6",
            isOnline = true,
            rating = 4.7f
        ),
        Teacher(
            id = "teacher_004",
            name = "Sophie",
            subject = "Histoire",
            level = "Collégial",
            description = "Dynamique et engageante.",
            avatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnvLJFxDYQKxPYe8CliPjg3bdgJk2s33Zx5sX-RLKYh-InxHw5Ngjc0GweMCAeBvJLCqwsdqE-0gBXz5itTs1bMgJ3Htok09dK4Ulnp7DgkxXYOo1QWzNiUUjoNE2I2884no8qWF-HzdfsIguuYDpHTL4p3mZBhnBC3FQPdRSJwuzOA-1CdH7Rsnuq1IU9O7i1-0D1J8kgwpgXolHicctrf8rIABp2I3Lk_Zuiso081LE97xSRypjOxCPlyCnWlRcwIEw5iSWg8FW6",
            isOnline = true,
            rating = 4.6f
        ),
    )

    override suspend fun getAllTeachers(): Result<List<Teacher>> {
        return try {
            Result.success(mockTeachers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTeacherById(id: String): Result<Teacher> {
        return try {
            val teacher = mockTeachers.find { it.id == id }
            if (teacher != null) {
                Result.success(teacher)
            } else {
                Result.failure(Exception("Teacher not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchTeachers(query: String): Result<List<Teacher>> {
        return try {
            val filtered = mockTeachers.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.subject.contains(query, ignoreCase = true)
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

