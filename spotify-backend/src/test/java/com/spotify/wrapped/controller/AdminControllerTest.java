package com.spotify.wrapped.controller;

import com.spotify.wrapped.entity.DeletionRequestEntity;
import com.spotify.wrapped.entity.UserEntity;
import com.spotify.wrapped.repository.DailyTopArtistsRepository;
import com.spotify.wrapped.repository.DailyTopTracksRepository;
import com.spotify.wrapped.repository.PlaybackHistoryRepository;
import com.spotify.wrapped.repository.UserRepository;
import com.spotify.wrapped.service.PrivacyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // NOWY IMPORT
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // UŻYWAMY NOWEJ ADNOTACJI DLA SPRING BOOT 3.4+
    private UserRepository userRepository;
    @MockitoBean
    private PrivacyService privacyService;
    @MockitoBean
    private DailyTopTracksRepository tracksRepo;
    @MockitoBean
    private DailyTopArtistsRepository artistsRepo;
    @MockitoBean
    private PlaybackHistoryRepository historyRepo;

    @Test
    void shouldReturnPendingRequests() throws Exception {
        DeletionRequestEntity req = new DeletionRequestEntity("spotify_user", 7);
        when(privacyService.getPendingRequests()).thenReturn(List.of(req));

        mockMvc.perform(get("/api/admin/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].spotifyId").value("spotify_user"))
                .andExpect(jsonPath("$[0].daysToKeep").value(7));
    }

    @Test
    void shouldApproveRequest() throws Exception {
        when(privacyService.approveRequest(1L)).thenReturn(120L);

        mockMvc.perform(post("/api/admin/request/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deletedEntries").value(120));
    }

    @Test
    void shouldReturnAllUsersWithAggregatedDocsCount() throws Exception {
        UserEntity user = new UserEntity("spot1", "Wika", "w@test.pl", LocalDate.now());
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(tracksRepo.countBySpotifyId("spot1")).thenReturn(15L);
        when(artistsRepo.countBySpotifyId("spot1")).thenReturn(5L);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].spotifyId").value("spot1"))
                .andExpect(jsonPath("$[0].totalDocs").value(20));
    }

    @Test
    void shouldDeleteRecordsGlobally() throws Exception {
        when(tracksRepo.deleteByDateBefore(any())).thenReturn(50L);
        when(artistsRepo.deleteByDateBefore(any())).thenReturn(10L);

        mockMvc.perform(delete("/api/admin/records").param("days", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deletedEntries").value(60));
    }
}