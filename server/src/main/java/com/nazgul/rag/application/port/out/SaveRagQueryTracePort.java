package com.nazgul.rag.application.port.out;

import com.nazgul.rag.domain.entity.RagQueryTrace;

public interface SaveRagQueryTracePort {

    RagQueryTrace save(RagQueryTrace trace);
}
