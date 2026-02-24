package org.julienjnnqin.luxmateapp.domain.repository

import org.julienjnnqin.luxmateapp.data.model.Persona

interface PersonaRepository {
    suspend fun getPersonas(): List<Persona>
    suspend fun getPersonaById(id: String): Persona
}
