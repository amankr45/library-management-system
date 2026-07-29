package com.aman.LibraryManagementSystem.mapper;

import com.aman.LibraryManagementSystem.dto.request.MemberRequest;
import com.aman.LibraryManagementSystem.dto.response.MemberResponse;
import com.aman.LibraryManagementSystem.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequest request) {

        if (request == null) {
            return null;
        }

        return new Member(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getMembershipType()
        );
    }

    public MemberResponse toResponse(Member member) {

        if (member == null) {
            return null;
        }

        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getMembershipType(),
                member.getStatus()
        );
    }

    public void updateEntity(MemberRequest request, Member member) {

        if (request == null || member == null) {
            return;
        }

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMembershipType(request.getMembershipType());
    }
}