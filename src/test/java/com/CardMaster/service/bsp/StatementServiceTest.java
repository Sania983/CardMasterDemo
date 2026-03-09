package com.CardMaster.service.bsp;

import com.CardMaster.model.bsp.Statement;
import com.CardMaster.model.cias.CardAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StatementServiceTest {

    @InjectMocks
    private StatementService service;

    private Statement statement;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Create a CardAccount entity stub
        CardAccount account = new CardAccount();
        account.setAccountId(100L);

        // Create a Statement entity stub
        statement = new Statement();
        statement.setStatementId(1L);
        statement.setAccountId(account); // ✅ assign entity, not a long
        statement.setPeriodStart(LocalDate.now().minusMonths(1));
        statement.setPeriodEnd(LocalDate.now());
        statement.setTotalDue(5000.0);
        statement.setMinimumDue(500.0);
        statement.setGeneratedDate(LocalDate.now());
    }

    @Test
    void testGenerateStatementThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.generateStatement(statement));
    }

    @Test
    void testCloseStatementThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.closeStatement(1L));
    }

    @Test
    void testGetByIdThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.getById(1L));
    }

    @Test
    void testListAllThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.listAll());
    }
}
