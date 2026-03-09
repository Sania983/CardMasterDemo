package com.CardMaster.controller.cau;

import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import com.CardMaster.exceptions.cau.EntityNotFoundException;
import com.CardMaster.exceptions.cau.GlobalExceptionHandler;
import com.CardMaster.exceptions.cau.UnauthorizedActionException;
import com.CardMaster.exceptions.cau.ValidationException;
import com.CardMaster.service.cau.UnderwritingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnderwritingController.class)
@Import(GlobalExceptionHandler.class) // ensures your @RestControllerAdvice is active
class UnderwritingControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UnderwritingService service;

    // ---------------- POST /applications/{appId}/scores ----------------

    @Test
    void createScore_201() throws Exception {
        // valid request (bureauScore must be positive)
        CreditScoreGenerateRequest req = new CreditScoreGenerateRequest();
        req.setBureauScore(750);

        // response body
        CreditScoreResponse res = new CreditScoreResponse();
        res.setScoreId(10L);
        res.setApplicationId(1L);
        res.setBureauScore(750);
        res.setInternalScore(690);
        res.setGeneratedDate("2026-03-08T10:00:00Z");

        Mockito.when(service.generateScore(eq(1L), any())).thenReturn(res);

        mockMvc.perform(post("/applications/1/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scoreId").value(10))
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.bureauScore").value(750))
                .andExpect(jsonPath("$.internalScore").value(690));
    }

    @Test
    void createScore_400() throws Exception {
        CreditScoreGenerateRequest req = new CreditScoreGenerateRequest();
        req.setBureauScore(650);

        Mockito.when(service.generateScore(eq(1L), any()))
                .thenThrow(new ValidationException("Invalid score request"));

        mockMvc.perform(post("/applications/1/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid score request"));
    }

    // ------------- GET /applications/{appId}/scores/latest -------------

    @Test
    void getLatestScore_200() throws Exception {
        CreditScoreResponse res = new CreditScoreResponse();
        res.setScoreId(11L);
        res.setApplicationId(1L);
        res.setBureauScore(760);
        res.setInternalScore(700);
        res.setGeneratedDate("2026-03-08T10:05:00Z");

        Mockito.when(service.getLatestScore(1L)).thenReturn(res);

        mockMvc.perform(get("/applications/1/scores/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreId").value(11))
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.bureauScore").value(760))
                .andExpect(jsonPath("$.internalScore").value(700));
    }

    @Test
    void getLatestScore_404() throws Exception {
        Mockito.when(service.getLatestScore(1L))
                // your constructor is (String entity, Long id)
                .thenThrow(new EntityNotFoundException("CreditScore", 1L));

        mockMvc.perform(get("/applications/1/scores/latest"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("CreditScore not found with ID: 1"));
    }

    // ---------------- POST /applications/{appId}/decisions ----------------

    @Test
    void createDecision_201() throws Exception {
        UnderwritingDecisionRequest req = new UnderwritingDecisionRequest();
        req.setDecision(UnderwritingDecisionType.APPROVE);
        req.setApprovedLimit(50000.0);
        req.setRemarks("Looks good");

        UnderwritingDecisionResponse res = new UnderwritingDecisionResponse();
        res.setDecisionId(20L);
        res.setApplicationId(1L);
        res.setUnderwriterId(99L);
        res.setDecision(UnderwritingDecisionType.APPROVE);
        res.setApprovedLimit(50000.0);
        res.setRemarks("Looks good");
        res.setDecisionDate("2026-03-08T11:00:00Z");

        Mockito.when(service.createDecision(eq(1L), any(), eq("Bearer x"))).thenReturn(res);

        mockMvc.perform(post("/applications/1/decisions")
                        .header("Authorization", "Bearer x")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.decisionId").value(20))
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.underwriterId").value(99))
                .andExpect(jsonPath("$.decision").value("APPROVE"))
                .andExpect(jsonPath("$.approvedLimit").value(50000.0))
                .andExpect(jsonPath("$.remarks").value("Looks good"));
    }

    @Test
    void createDecision_missingAuth_400() throws Exception {
        UnderwritingDecisionRequest req = new UnderwritingDecisionRequest();
        req.setDecision(UnderwritingDecisionType.APPROVE);
        req.setApprovedLimit(50000.0);

        mockMvc.perform(post("/applications/1/decisions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest()); // missing required @RequestHeader("Authorization")
    }

    @Test
    void createDecision_403() throws Exception {
        UnderwritingDecisionRequest req = new UnderwritingDecisionRequest();
        req.setDecision(UnderwritingDecisionType.APPROVE);
        req.setApprovedLimit(50000.0);
        req.setRemarks("Looks good");

        Mockito.when(service.createDecision(eq(1L), any(), eq("Bearer x")))
                .thenThrow(new UnauthorizedActionException("Not allowed"));

        mockMvc.perform(post("/applications/1/decisions")
                        .header("Authorization", "Bearer x")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Not allowed"));
    }

    // ------------ GET /applications/{appId}/decisions/latest ------------

    @Test
    void getLatestDecision_200() throws Exception {
        UnderwritingDecisionResponse res = new UnderwritingDecisionResponse();
        res.setDecisionId(21L);
        res.setApplicationId(1L);
        res.setUnderwriterId(99L);
        res.setDecision(UnderwritingDecisionType.APPROVE);
        res.setApprovedLimit(60000.0);
        res.setRemarks("Ok");
        res.setDecisionDate("2026-03-08T11:05:00Z");

        Mockito.when(service.getLatestDecision(1L)).thenReturn(res);

        mockMvc.perform(get("/applications/1/decisions/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decisionId").value(21))
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.decision").value("APPROVE"));
    }

    @Test
    void getLatestDecision_404() throws Exception {
        Mockito.when(service.getLatestDecision(1L))
                .thenThrow(new EntityNotFoundException("UnderwritingDecision", 1L));

        mockMvc.perform(get("/applications/1/decisions/latest"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("UnderwritingDecision not found with ID: 1"));
    }
}