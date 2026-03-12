package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.HobbyCardRelation;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface HobbyCardRelationStore {

    HobbyCardRelation save(HobbyCardRelation relation);

    List<HobbyCardRelation> findBySourceCardIds(Collection<UUID> sourceCardIds, int limit);
}
