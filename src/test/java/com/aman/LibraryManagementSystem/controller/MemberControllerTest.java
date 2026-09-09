package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.MemberRequest;
import com.aman.LibraryManagementSystem.dto.response.MemberResponse;
import com.aman.LibraryManagementSystem.enums.MemberStatus;
import com.aman.LibraryManagementSystem.enums.MembershipType;
import com.aman.LibraryManagementSystem.exception.member.DuplicateMemberException;
import com.aman.LibraryManagementSystem.exception.member.MemberNotFoundException;
import com.aman.LibraryManagementSystem.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aman.LibraryManagementSystem.config.SecurityConfig;
import com.aman.LibraryManagementSystem.security.CustomUserDetailsService;
import org.springframework.context.annotation.Import;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    private MemberResponse createMemberResponse() {

        return new MemberResponse(
                1L,
                "Aman Kumar",
                "aman@test.com",
                "9876543210",
                MembershipType.STUDENT,
                MemberStatus.ACTIVE
        );
    }

    @Test
    void shouldCreateMemberSuccessfully() throws Exception {

        when(memberService.createMember(any(MemberRequest.class)))
                .thenReturn(createMemberResponse());

        mockMvc.perform(
                        post("/members")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "name": "Aman Kumar",
                          "email": "aman@test.com",
                          "phone": "9876543210",
                          "membershipType": "STUDENT"
                        }
                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Aman Kumar"))
                .andExpect(jsonPath("$.memberStatus").value("ACTIVE"));

        verify(memberService).createMember(any(MemberRequest.class));
    }

    @Test
    void shouldGetMemberByIdSuccessfully() throws Exception {

        when(memberService.getMemberById(1L))
                .thenReturn(createMemberResponse());

        mockMvc.perform(
                        get("/members/{id}", 1L)
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("aman@test.com"));

        verify(memberService).getMemberById(1L);
    }

    @Test
    void shouldGetAllMembersSuccessfully() throws Exception {

        when(memberService.getAllMembers())
                .thenReturn(List.of(createMemberResponse()));

        mockMvc.perform(get("/members").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Aman Kumar"))
                .andExpect(jsonPath("$[0].membershipType").value("STUDENT"));

        verify(memberService).getAllMembers();
    }

    @Test
    void shouldUpdateMemberSuccessfully() throws Exception {

        MemberResponse response = createMemberResponse();
        response.setName("Aman Sharma");

        when(memberService.updateMember(
                org.mockito.ArgumentMatchers.eq(1L),
                any(MemberRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/members/{id}", 1L)
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "name": "Aman Sharma",
                          "email": "aman@test.com",
                          "phone": "9876543210",
                          "membershipType": "STUDENT"
                        }
                        """)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Aman Sharma")
                );

        verify(memberService).updateMember(
                org.mockito.ArgumentMatchers.eq(1L),
                any(MemberRequest.class)
        );
    }

    @Test
    void shouldDeleteMemberSuccessfully() throws Exception {

        doNothing().when(memberService).deleteMember(1L);

        mockMvc.perform(
                        delete("/members/{id}", 1L)
                                .with(jwt())
                )
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(1L);
    }

    @Test
    void shouldReturnNotFoundWhenMemberDoesNotExist() throws Exception {

        when(memberService.getMemberById(999L))
                .thenThrow(new MemberNotFoundException(999L));

        mockMvc.perform(get("/members/{id}", 999L)
                        .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value("Member not found with id : 999")
                )
                .andExpect(jsonPath("$.path").value("/members/999"));
    }

    @Test
    void shouldReturnBadRequestForInvalidMemberRequest() throws Exception {

        mockMvc.perform(
                        post("/members")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "A",
                                          "email": "aman@test.com",
                                          "phone": "9876543210",
                                          "membershipType": "STUDENT"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(
                        jsonPath("$.fieldErrors.name")
                                .value(
                                        "Name must be between 2 and 100 characters."
                                )
                );
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

        when(memberService.createMember(any(MemberRequest.class)))
                .thenThrow(new DuplicateMemberException("aman@test.com"));

        mockMvc.perform(
                        post("/members")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Aman Kumar",
                                          "email": "aman@test.com",
                                          "phone": "9876543210",
                                          "membershipType": "STUDENT"
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Member with 'aman@test.com' already exists."
                                )
                );
    }
}