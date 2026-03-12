package com.nazgul.rag.application.port.in;

public interface SearchRagUseCase {

    SearchRagResult search(SearchRagQuery query);
}
