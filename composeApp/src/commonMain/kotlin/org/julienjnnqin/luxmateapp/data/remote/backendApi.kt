package org.julienjnnqin.luxmateapp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.CancellationException

interface backendApi {
    //PERSONAS
    suspend fun getPersonas(): List<Unit>
    suspend fun getPersonaById(): Unit

    //CHAT SESSIONS
    suspend fun getSessions(): List<Unit>
    suspend fun createSession(): Unit
    suspend fun getSession(): Unit
    //MESSAGES

    //USER PROFILES
}

class KtorbackendApi(private val client: HttpClient) : backendApi {
    companion object {
        private const val API_URL =
            ""
    }

    override suspend fun getPersonas(): List<Unit> {
        return try {
            client.get(API_URL).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()

            emptyList()
        }
    }

    override suspend fun getPersonaById() {
        TODO("Not yet implemented")
    }
}

