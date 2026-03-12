package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.ContentChunk;

public interface CreateContentChunkUseCase {

    ContentChunk createChunk(CreateContentChunkCommand command);
}
