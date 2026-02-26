package com.CardMaster.service.cau;

import com.CardMaster.Enum.cau.UnderwritingDecisionType;
import com.CardMaster.Enum.iam.UserEnum;
import com.CardMaster.dao.cau.CreditScoreRepository;
import com.CardMaster.dao.cau.UnderwritingDecisionRepository;
import com.CardMaster.dao.iam.UserRepository;
import com.CardMaster.dao.paa.CardApplicationRepository;
import com.CardMaster.dto.cau.CreditScoreGenerateRequest;
import com.CardMaster.dto.cau.CreditScoreResponse;
import com.CardMaster.dto.cau.UnderwritingDecisionRequest;
import com.CardMaster.dto.cau.UnderwritingDecisionResponse;
import com.CardMaster.exceptions.cau.EntityNotFoundException;
import com.CardMaster.exceptions.cau.UnauthorizedActionException;
import com.CardMaster.exceptions.cau.ValidationException;
import com.CardMaster.mapper.cau.UnderwritingMapper;
import com.CardMaster.model.cau.CreditScore;
import com.CardMaster.model.cau.UnderwritingDecision;
import com.CardMaster.model.iam.User;
import com.CardMaster.model.paa.CardApplication;
import com.CardMaster.security.iam.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Minimal, beginner-friendly unit tests for UnderwritingService (Option A).
 * - Service returns DTOs; mapper.to...Response(...) is used inside service.
 * - Only sets fields actually needed by the service.
 */
@ExtendWith(MockitoExtension.class)
class UnderwritingServiceTest {

    @Mock private CreditScoreRepository creditScoreRepository;
    @Mock private UnderwritingDecisionRepository decisionRepository;
    @Mock private CardApplicationRepository applicationRepository;
    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private UnderwritingMapper mapper;

    @InjectMocks
    private UnderwritingService service;

    private CardApplication app;
    private User underwriter;

    @BeforeEach
    void setUp() {
        // Minimal CardApplication: id + requestedLimit + status
        app = CardApplication.builder()
                .applicationId(1L)
                .requestedLimit(200_000.0)
                .applicationDate(LocalDate.now())
                .status(CardApplication.CardApplicationStatus.Submitted)
                .build();

        // Minimal User: id + role (UNDERWRITER)
        underwriter = User.builder()
                .userId(9L)
                .role(UserEnum.UNDERWRITER)
                .build();
    }

    // ----------------------------------------------------------
    // generateScore
    // ----------------------------------------------------------
    @Test
    void generateScore_success_returnsDto() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));
        CreditScoreGenerateRequest req = CreditScoreGenerateRequest.builder()
                .bureauScore(720).build();

        CreditScore saved = CreditScore.builder()
                .scoreId(101L)
                .application(app)
                .bureauScore(720)
                .internalScore(72)
                .generatedDate(LocalDateTime.now())
                .build();
        when(creditScoreRepository.save(any(CreditScore.class))).thenReturn(saved);

        CreditScoreResponse dto = CreditScoreResponse.builder()
                .scoreId(101L).applicationId(1L)
                .bureauScore(720).internalScore(72)
                .generatedDate(saved.getGeneratedDate().toString())
                .build();
        when(mapper.toCreditScoreResponse(saved)).thenReturn(dto);

        CreditScoreResponse out = service.generateScore(1L, req);

        assertThat(out.getScoreId()).isEqualTo(101L);
        assertThat(out.getInternalScore()).isEqualTo(72);
        verify(mapper).toCreditScoreResponse(saved);
    }

    @Test
    void generateScore_invalidBureau_throws() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));
        CreditScoreGenerateRequest req = CreditScoreGenerateRequest.builder()
                .bureauScore(0).build();

        assertThatThrownBy(() -> service.generateScore(1L, req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Bureau score must be positive");
    }

    // ----------------------------------------------------------
    // getLatestScore
    // ----------------------------------------------------------
    @Test
    void getLatestScore_found_returnsDto() {
        CreditScore latest = CreditScore.builder()
                .scoreId(200L)
                .application(app)
                .bureauScore(700)
                .internalScore(70)
                .generatedDate(LocalDateTime.now())
                .build();
        when(creditScoreRepository.findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(1L))
                .thenReturn(Optional.of(latest));

        CreditScoreResponse dto = CreditScoreResponse.builder()
                .scoreId(200L).applicationId(1L)
                .bureauScore(700).internalScore(70)
                .generatedDate(latest.getGeneratedDate().toString())
                .build();
        when(mapper.toCreditScoreResponse(latest)).thenReturn(dto);

        CreditScoreResponse out = service.getLatestScore(1L);

        assertThat(out.getScoreId()).isEqualTo(200L);
        verify(mapper).toCreditScoreResponse(latest);
    }

    @Test
    void getLatestScore_notFound_throws() {
        when(creditScoreRepository.findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getLatestScore(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Latest CreditScore for application");
    }

    // ----------------------------------------------------------
    // createDecision (AUTO): APPROVE / REJECT / CONDITIONAL
    // ----------------------------------------------------------
    @Test
    void createDecision_autoApprove_setsApproved_andFullLimit() {
        when(jwtUtil.extractUsername("token")).thenReturn("9");       // JWT sub = userId
        when(userRepository.findById(9L)).thenReturn(Optional.of(underwriter));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        CreditScore latest = CreditScore.builder()
                .scoreId(11L).application(app)
                .bureauScore(780).internalScore(78)
                .generatedDate(LocalDateTime.now()).build();
        when(creditScoreRepository.findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(1L))
                .thenReturn(Optional.of(latest));

        UnderwritingDecision saved = UnderwritingDecision.builder()
                .decisionId(500L).applicationid(app).underwriterid(underwriter)
                .decision(UnderwritingDecisionType.APPROVE)
                .approvedLimit(app.getRequestedLimit())
                .decisionDate(LocalDateTime.now()).build();
        when(decisionRepository.save(any(UnderwritingDecision.class))).thenReturn(saved);

        UnderwritingDecisionResponse dto = UnderwritingDecisionResponse.builder()
                .decisionId(500L).applicationId(1L).underwriterId(9L)
                .decision(UnderwritingDecisionType.APPROVE).approvedLimit(200000.0)
                .decisionDate(saved.getDecisionDate().toString()).build();
        when(mapper.toUnderwritingDecisionResponse(saved)).thenReturn(dto);

        UnderwritingDecisionRequest req = UnderwritingDecisionRequest.builder().build(); // AUTO

        UnderwritingDecisionResponse out = service.createDecision(1L, req, "Bearer token");

        assertThat(out.getDecision()).isEqualTo(UnderwritingDecisionType.APPROVE);
        assertThat(out.getApprovedLimit()).isEqualTo(200000.0);
        assertThat(app.getStatus()).isEqualTo(CardApplication.CardApplicationStatus.Approved);
        verify(applicationRepository).save(app);
    }

    @Test
    void createDecision_autoReject_setsRejected_andZero() {
        when(jwtUtil.extractUsername("token")).thenReturn("9");
        when(userRepository.findById(9L)).thenReturn(Optional.of(underwriter));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        CreditScore low = CreditScore.builder()
                .scoreId(12L).application(app)
                .bureauScore(590).internalScore(59)
                .generatedDate(LocalDateTime.now()).build();
        when(creditScoreRepository.findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(1L))
                .thenReturn(Optional.of(low));

        UnderwritingDecision saved = UnderwritingDecision.builder()
                .decisionId(501L).applicationid(app).underwriterid(underwriter)
                .decision(UnderwritingDecisionType.REJECT).approvedLimit(0.0)
                .decisionDate(LocalDateTime.now()).build();
        when(decisionRepository.save(any(UnderwritingDecision.class))).thenReturn(saved);

        UnderwritingDecisionResponse dto = UnderwritingDecisionResponse.builder()
                .decisionId(501L).applicationId(1L).underwriterId(9L)
                .decision(UnderwritingDecisionType.REJECT).approvedLimit(0.0)
                .decisionDate(saved.getDecisionDate().toString()).build();
        when(mapper.toUnderwritingDecisionResponse(saved)).thenReturn(dto);

        UnderwritingDecisionRequest req = UnderwritingDecisionRequest.builder().build();

        UnderwritingDecisionResponse out = service.createDecision(1L, req, "Bearer token");

        assertThat(out.getDecision()).isEqualTo(UnderwritingDecisionType.REJECT);
        assertThat(out.getApprovedLimit()).isZero();
        assertThat(app.getStatus()).isEqualTo(CardApplication.CardApplicationStatus.Rejected);
        verify(applicationRepository).save(app);
    }

    @Test
    void createDecision_autoConditional_setsUnderReview_andHalf() {
        when(jwtUtil.extractUsername("token")).thenReturn("9");
        when(userRepository.findById(9L)).thenReturn(Optional.of(underwriter));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        CreditScore mid = CreditScore.builder()
                .scoreId(13L).application(app)
                .bureauScore(700).internalScore(70)
                .generatedDate(LocalDateTime.now()).build();
        when(creditScoreRepository.findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(1L))
                .thenReturn(Optional.of(mid));

        Double expectedHalf = app.getRequestedLimit() * 0.5;

        UnderwritingDecision saved = UnderwritingDecision.builder()
                .decisionId(502L).applicationid(app).underwriterid(underwriter)
                .decision(UnderwritingDecisionType.CONDITIONAL).approvedLimit(expectedHalf)
                .decisionDate(LocalDateTime.now()).build();
        when(decisionRepository.save(any(UnderwritingDecision.class))).thenReturn(saved);

        UnderwritingDecisionResponse dto = UnderwritingDecisionResponse.builder()
                .decisionId(502L).applicationId(1L).underwriterId(9L)
                .decision(UnderwritingDecisionType.CONDITIONAL).approvedLimit(expectedHalf)
                .decisionDate(saved.getDecisionDate().toString()).build();
        when(mapper.toUnderwritingDecisionResponse(saved)).thenReturn(dto);

        UnderwritingDecisionRequest req = UnderwritingDecisionRequest.builder().build();

        UnderwritingDecisionResponse out = service.createDecision(1L, req, "Bearer token");

        assertThat(out.getDecision()).isEqualTo(UnderwritingDecisionType.CONDITIONAL);
        assertThat(out.getApprovedLimit()).isEqualTo(expectedHalf);
        assertThat(app.getStatus()).isEqualTo(CardApplication.CardApplicationStatus.UnderReview);
        verify(applicationRepository).save(app);
    }

    // ----------------------------------------------------------
    // createDecision (MANUAL)
    // ----------------------------------------------------------
    @Test
    void createDecision_manualApprove_requiresPositiveLimit() {
        when(jwtUtil.extractUsername("token")).thenReturn("9");
        when(userRepository.findById(9L)).thenReturn(Optional.of(underwriter));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));

        UnderwritingDecisionRequest req = UnderwritingDecisionRequest.builder()
                .decision(UnderwritingDecisionType.APPROVE)
                .approvedLimit(0.0) // invalid
                .build();

        assertThatThrownBy(() -> service.createDecision(1L, req, "Bearer token"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Approved limit must be positive");
    }

    // ----------------------------------------------------------
    // Guards (auth/header)
    // ----------------------------------------------------------
    @Test
    void createDecision_missingHeader_throwsUnauthorized() {
        UnderwritingDecisionRequest req = UnderwritingDecisionRequest.builder().build();
        assertThatThrownBy(() -> service.createDecision(1L, req, null))
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessageContaining("Missing or invalid Authorization header");
    }

    @Test
    void createDecision_nonNumericSub_throwsUnauthorized() {
        when(jwtUtil.extractUsername("token")).thenReturn("not-a-number");
        UnderwritingDecisionRequest req = UnderwritingDecisionRequest.builder().build();

        assertThatThrownBy(() -> service.createDecision(1L, req, "Bearer token"))
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessageContaining("numeric userId");
    }

    // ----------------------------------------------------------
    // getLatestDecision
    // ----------------------------------------------------------
    @Test
    void getLatestDecision_found_returnsDto() {
        UnderwritingDecision dec = UnderwritingDecision.builder()
                .decisionId(900L).applicationid(app).underwriterid(underwriter)
                .decision(UnderwritingDecisionType.APPROVE).approvedLimit(200000.0)
                .decisionDate(LocalDateTime.now()).build();
        when(decisionRepository.findTopByApplicationid_ApplicationIdOrderByDecisionDateDesc(1L))
                .thenReturn(Optional.of(dec));

        UnderwritingDecisionResponse dto = UnderwritingDecisionResponse.builder()
                .decisionId(900L).applicationId(1L).underwriterId(9L)
                .decision(UnderwritingDecisionType.APPROVE).approvedLimit(200000.0)
                .decisionDate(dec.getDecisionDate().toString()).build();
        when(mapper.toUnderwritingDecisionResponse(dec)).thenReturn(dto);

        UnderwritingDecisionResponse out = service.getLatestDecision(1L);

        assertThat(out.getDecisionId()).isEqualTo(900L);
        verify(mapper).toUnderwritingDecisionResponse(dec);
    }

    @Test
    void getLatestDecision_notFound_throws() {
        when(decisionRepository.findTopByApplicationid_ApplicationIdOrderByDecisionDateDesc(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getLatestDecision(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Latest UnderwritingDecision");
    }
}