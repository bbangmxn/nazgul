package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.HobbyCard;

public interface CreateHobbyCardUseCase {

    HobbyCard createCard(CreateHobbyCardCommand command);
}
