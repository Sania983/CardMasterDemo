package com.CardMaster.controller.cau;

import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import com.CardMaster.exceptions.cau.EntityNotFoundException;
import com.CardMaster.exceptions.cau.ValidationException;
import com.CardMaster.exceptions.cau.UnauthorizedActionException;
import com.CardMaster.service.cau.UnderwritingService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnderwritingControllerTest {

    private final UnderwritingService service = mock(UnderwritingService.class);
    private final UnderwritingController controller = new UnderwritingController(service);

    // ---------------- CREATE SCORE ----------------

    @Test
    void createScore_success() {
        CreditScoreGenerateRequest req = new CreditScoreGenerateRequest();
        req.setBureauScore(750);

        CreditScoreResponse res = new CreditScoreResponse();
        res.setBureauScore(750);
        res.setInternalScore(690);

        when(service.generateScore(1L, req)).thenReturn(res);

        ResponseEntity<CreditScoreResponse> response = controller.createScore(1L, req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());        // <-- changed
        assertEquals(750, response.getBody().getBureauScore());
    }

    @Test
    void createScore_validationError() {
        CreditScoreGenerateRequest req = new CreditScoreGenerateRequest();
        req.setBureauScore(700);

        when(service.generateScore(1L, req))
                .thenThrow(new ValidationException("Invalid score request"));

        assertThrows(
                ValidationException.class,
                () -> controller.createScore(1L, req)
        );
    }

    // ---------------- GET LATEST SCORE ----------------

    @Test
    void getLatestScore_success() {
        CreditScoreResponse res = new CreditScoreResponse();
        res.setBureauScore(760);
        res.setInternalScore(700);

        when(service.getLatestScore(1L)).thenReturn(res);

        ResponseEntity<CreditScoreResponse> response = controller.getLatestScore(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());             // <-- changed
        assertEquals(760, response.getBody().getBureauScore());
    }

    @Test
    void getLatestScore_notFound() {
        when(service.getLatestScore(1L))
                .thenThrow(new EntityNotFoundException("CreditScore", 1L));

        assertThrows(
                EntityNotFoundException.class,
                () -> controller.getLatestScore(1L)
        );
    }

    // ---------------- CREATE DECISION ----------------

    @Test
    void createDecision_success() {
        UnderwritingDecisionRequest req = new UnderwritingDecisionRequest();
        req.setRemarks("ok");

        UnderwritingDecisionResponse res = new UnderwritingDecisionResponse();
        res.setRemarks("ok");

        when(service.createDecision(1L, req, "Bearer x")).thenReturn(res);

        ResponseEntity<UnderwritingDecisionResponse> response =
                controller.createDecision(1L, req, "Bearer x");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());        // <-- changed
        assertEquals("ok", response.getBody().getRemarks());
    }

    @Test
    void createDecision_unauthorized() {
        UnderwritingDecisionRequest req = new UnderwritingDecisionRequest();

        when(service.createDecision(1L, req, "Bearer x"))
                .thenThrow(new UnauthorizedActionException("Not allowed"));

        assertThrows(
                UnauthorizedActionException.class,
                () -> controller.createDecision(1L, req, "Bearer x")
        );
    }

    // ---------------- GET LATEST DECISION ----------------

    @Test
    void getLatestDecision_success() {
        UnderwritingDecisionResponse res = new UnderwritingDecisionResponse();
        res.setRemarks("Approved");

        when(service.getLatestDecision(1L)).thenReturn(res);

        ResponseEntity<UnderwritingDecisionResponse> response =
                controller.getLatestDecision(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());             // <-- changed
        assertEquals("Approved", response.getBody().getRemarks());
    }

    @Test
    void getLatestDecision_notFound() {
        when(service.getLatestDecision(1L))
                .thenThrow(new EntityNotFoundException("UnderwritingDecision", 1L));

        assertThrows(
                EntityNotFoundException.class,
                () -> controller.getLatestDecision(1L)
        );
    }
}