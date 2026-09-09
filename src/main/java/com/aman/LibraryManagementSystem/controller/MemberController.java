package com.aman.LibraryManagementSystem.controller;


import com.aman.LibraryManagementSystem.dto.request.MemberRequest;
import com.aman.LibraryManagementSystem.dto.response.MemberResponse;
import com.aman.LibraryManagementSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Members",
        description = "Endpoints for managing library members"
)
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @Operation(
            summary = "Create a member",
            description = "Registers a new library member."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@Valid
                                       @RequestBody
                                       MemberRequest request
    ){
        return memberService.createMember(request);
    }

    @Operation(
            summary = "Get a member by ID",
            description = "Retrieves one member using their unique identifier."
    )
    @GetMapping("/{id}")
    public MemberResponse getMemberById(@PathVariable
                                        Long id
    ){
        return memberService.getMemberById(id);
    }

    @Operation(
            summary = "Get all members",
            description = "Retrieves all registered library members."
    )
    @GetMapping
    public List<MemberResponse> getAllMembers(){
        return memberService.getAllMembers();
    }

    @Operation(
            summary = "Update a member",
            description = "Updates the details of an existing member."
    )
    @PutMapping("/{id}")
    public MemberResponse updateMember(
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            MemberRequest request
    ){
        return memberService.updateMember(id,request);
    }

    @Operation(
            summary = "Delete a member",
            description = "Deletes a member using their ID."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(
            @PathVariable
            Long id
    ){
        memberService.deleteMember(id);
    }
}
