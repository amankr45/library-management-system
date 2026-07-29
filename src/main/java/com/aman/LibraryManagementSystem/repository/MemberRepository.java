package com.aman.LibraryManagementSystem.repository;

import com.aman.LibraryManagementSystem.entity.Member;
import com.aman.LibraryManagementSystem.enums.MembershipType;
import com.aman.LibraryManagementSystem.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    List<Member> findByNameContainingIgnoreCase(String name);

    List<Member> findByMembershipType(MembershipType membershipType);

    List<Member> findByStatus(MemberStatus memberStatus);

    boolean existsByEmail(String email);
}
