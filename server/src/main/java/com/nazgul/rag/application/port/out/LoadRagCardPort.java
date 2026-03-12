package com.nazgul.rag.application.port.out;

import com.nazgul.rag.domain.entity.RagCard;
import java.util.List;

public interface LoadRagCardPort {

    List<RagCard> searchCards(String query, int limit);
}
