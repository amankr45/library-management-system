package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.MemberRequest;
import com.aman.LibraryManagementSystem.dto.response.MemberResponse;

import java.util.List;

public interface MemberService {
    MemberResponse createMember(MemberRequest request);
    MemberResponse getMemberById(Long id);
    List<MemberResponse> getAllMembers();
    MemberResponse updateMember(Long id, MemberRequest request);
    void deleteMember(Long id);

}
