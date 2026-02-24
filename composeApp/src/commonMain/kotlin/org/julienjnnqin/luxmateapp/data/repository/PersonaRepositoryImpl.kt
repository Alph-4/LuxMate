package org.julienjnnqin.luxmateapp.data.repository

import org.julienjnnqin.luxmateapp.data.remote.backendApi
import org.julienjnnqin.luxmateapp.domain.repository.PersonaRepository

class PersonaRepositoryImpl(private val api: backendApi) : PersonaRepository {
    override suspend fun getPersonas(): List<org.julienjnnqin.luxmateapp.data.model.Persona> {
        return api.getPersonas()
    }

    override suspend fun getPersonaById(
            id: String
    ): org.julienjnnqin.luxmateapp.data.model.Persona {
        return api.getPersonaById(id)
    }
}
