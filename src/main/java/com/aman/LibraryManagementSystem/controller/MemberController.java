package com.aman.LibraryManagementSystem.controller;


import com.aman.LibraryManagementSystem.dto.request.MemberRequest;
import com.aman.LibraryManagementSystem.dto.response.MemberResponse;
import com.aman.LibraryManagementSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@Valid
                                       @RequestBody
                                       MemberRequest request
    ){
        return memberService.createMember(request);
    }

    @GetMapping("/{id}")
    public MemberResponse getMemberById(@PathVariable
                                        Long id
    ){
        return memberService.getMemberById(id);
    }

    @GetMapping
    public List<MemberResponse> getAllMembers(){
        return memberService.getAllMembers();
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(
            @PathVariable
            Long id
    ){
        memberService.deleteMember(id);
    }
}
