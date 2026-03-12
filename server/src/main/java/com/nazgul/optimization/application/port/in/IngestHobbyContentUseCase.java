package com.nazgul.optimization.application.port.in;

public interface IngestHobbyContentUseCase {

    IngestHobbyContentResult ingest(IngestHobbyContentCommand command);
}
