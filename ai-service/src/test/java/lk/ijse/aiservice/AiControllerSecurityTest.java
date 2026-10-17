package lk.ijse.aiservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AiControllerSecurityTest {
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        AiController controller = new AiController();
        ReflectionTestUtils.setField(controller, "internalApiKey", "test-internal-key");
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new AiExceptionHandler())
                .build();
    }

    @Test
    void userHistoryRequiresAValidSignedUserAssertion() throws Exception {
        mvc.perform(get("/api/v1/ai/recommendations/users/42")
                        .header("X-AI-Internal-Key", "test-internal-key")
                        .header("X-Authenticated-User", "43")
                        .header("X-User-Assertion-Expiry", "4102444800")
                        .header("X-User-Assertion", "invalid"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    @Test
    void orderDataRoutesRequireInternalKey() throws Exception {
        AiController controller = new AiController();
        ReflectionTestUtils.setField(controller, "internalApiKey", "");
        MockMvc unconfiguredMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new AiExceptionHandler()).build();
        unconfiguredMvc.perform(get("/api/v1/ai/forecast/products/17"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Internal AI data endpoints are not configured."));
    }

    @Test
    void forecastRejectsTooFewObservations() throws Exception {
        mvc.perform(post("/api/v1/ai/forecast")
                        .contentType("application/json")
                        .content("{\"history\":[{\"date\":\"2026-10-01\",\"unitsSold\":2},{\"date\":\"2026-10-02\",\"unitsSold\":1}],\"periodsAhead\":2}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("At least seven distinct daily observations are required."));
    }

}
