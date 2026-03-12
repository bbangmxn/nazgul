package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.HobbyCardRelation;

public interface LinkHobbyCardRelationUseCase {

    HobbyCardRelation link(LinkHobbyCardRelationCommand command);
}
