package com.CardMaster.service.bsp;

import com.CardMaster.model.bsp.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {

    /**
     * Generate a new statement.
     */
    public Statement generateStatement(Statement statement) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Close a statement.
     */
    public Statement closeStatement(Long statementId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Get a statement by ID.
     */
    public Statement getById(Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * List all statements.
     */
    public List<Statement> listAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}