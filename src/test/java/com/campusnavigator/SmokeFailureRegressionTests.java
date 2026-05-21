package com.campusnavigator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class SmokeFailureRegressionTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void announcementRoutesSupportCrudAnd404s() throws Exception {
        mockMvc.perform(get("/api/announcement"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/announcement/postAnnouncement")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        MvcResult created = mockMvc.perform(post("/api/announcement/postAnnouncement")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Smoke\",\"content\":\"Test\",\"category\":\"General\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.announcementID").exists())
                .andReturn();

        int announcementID = com.jayway.jsonpath.JsonPath.read(created.getResponse().getContentAsString(), "$.announcementID");

        mockMvc.perform(put("/api/announcement/putAnnouncement/{announcementID}", announcementID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Smoke2\",\"content\":\"Test2\",\"category\":\"General\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/announcement/putAnnouncement/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Missing\",\"content\":\"Missing\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/announcement/deleteAnnouncement/{announcementID}", announcementID))
                .andExpect(status().isOk());
    }

    @Test
    void emergencyRoutesAreScopedUnderApiEmergency() throws Exception {
        mockMvc.perform(get("/api/emergency/print"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/print"))
                .andExpect(status().isNotFound());
    }

    @Test
    void signupRejectsInvalidAndDuplicateUsers() throws Exception {
        String email = "smoke-" + UUID.randomUUID() + "@example.com";
        String payload = "{\"name\":\"Smoke User\",\"email\":\"" + email + "\",\"password\":\"secret123\"}";

        mockMvc.perform(post("/api/user/postUserEntity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/user/postUserEntity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/user/postUserEntity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/user/postUserEntity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Missing Password\",\"email\":\"missing-password@example.com\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/user/postUserEntity")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + "x".repeat(500) + "\",\"email\":\"long@example.com\",\"password\":\"secret123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidEdgeCasesReturn4xxInsteadOf500() throws Exception {
        mockMvc.perform(put("/api/user/putUserRecord/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Nobody\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/maps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/maps/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/pois")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/campusservice/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/event/deleteEvent/99999"))
                .andExpect(status().isNotFound());
    }
}
