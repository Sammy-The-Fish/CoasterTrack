package com.example.coastertrack.domain.usecases

import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.domain.mappers.toEntity
import com.example.coastertrack.domain.model.RollercoasterEntity
import javax.inject.Inject

class GetRollercoasterDetailsUseCase @Inject constructor(
    private val repository: RcdbRepository
) {

    /**
     * executes the use case
     * @param id the id of the rollercoaster of which details are to be fetched
     * @return returns null if id invalid
     */
    suspend fun execute(id: Int): RollercoasterEntity? {
        val response = repository.getRollercoasterById(id)


        // invalid Ids will produce an empty result
        if (response.isEmpty()) return null

        // as an ID is used, there can only be one item in this list
        return response[0].toEntity()
    }
}