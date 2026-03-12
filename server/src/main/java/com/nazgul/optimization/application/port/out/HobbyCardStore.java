package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HobbyCardStore {

    HobbyCard save(HobbyCard card);

    Optional<HobbyCard> findById(UUID cardId);

    List<HobbyCard> findByIds(Collection<UUID> cardIds);

    List<HobbyCard> search(String query, Long hobbyId, int limit);
}
