package com.aman.LibraryManagementSystem.service.impl;

import com.aman.LibraryManagementSystem.dto.request.MemberRequest;
import com.aman.LibraryManagementSystem.dto.response.MemberResponse;
import com.aman.LibraryManagementSystem.entity.Member;
import com.aman.LibraryManagementSystem.enums.MemberStatus;
import com.aman.LibraryManagementSystem.exception.member.DuplicateMemberException;
import com.aman.LibraryManagementSystem.exception.member.MemberNotFoundException;
import com.aman.LibraryManagementSystem.mapper.MemberMapper;
import com.aman.LibraryManagementSystem.repository.MemberRepository;
import com.aman.LibraryManagementSystem.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository,MemberMapper memberMapper){
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    public MemberResponse createMember(MemberRequest request){
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateMemberException(request.getEmail());
        }

        Member member = memberMapper.toEntity(request);
        member.setStatus(MemberStatus.ACTIVE);
        Member savedMember = memberRepository.save(member);

        return memberMapper.toResponse(savedMember);
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        return memberMapper.toResponse(member);
    }

    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper :: toResponse).toList();
    }

    @Override
    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        if (!member.getEmail().equals(request.getEmail())
                && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateMemberException(request.getEmail());
        }

        memberMapper.updateEntity(request, member);
        Member updatedMember = memberRepository.save(member);

        return memberMapper.toResponse(updatedMember);
    }

    @Override
    public void deleteMember(Long id) {
        if(!memberRepository.existsById(id)){
            throw new MemberNotFoundException(id);
        }
        memberRepository.deleteById(id);
    }
}
