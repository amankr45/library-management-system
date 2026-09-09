package com.aman.LibraryManagementSystem.repository;

import com.aman.LibraryManagementSystem.entity.Member;
import com.aman.LibraryManagementSystem.enums.MemberStatus;
import com.aman.LibraryManagementSystem.enums.MembershipType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    // ============================================================
    // SAVE
    // ============================================================

    @Test
    void shouldSaveMemberSuccessfully() {

        Member member = new Member(
                "Test Member",
                "test-member-001@test.com",
                "9876543210",
                MembershipType.STUDENT
        );

        Member savedMember =
                memberRepository.save(member);

        assertThat(savedMember.getId())
                .isNotNull();

        assertThat(savedMember.getName())
                .isEqualTo("Test Member");

        assertThat(savedMember.getEmail())
                .isEqualTo("test-member-001@test.com");

        assertThat(savedMember.getPhone())
                .isEqualTo("9876543210");

        assertThat(savedMember.getMembershipType())
                .isEqualTo(MembershipType.STUDENT);
    }

    // ============================================================
    // FIND BY ID
    // ============================================================

    @Test
    void shouldFindMemberByIdSuccessfully() {

        Member member = memberRepository.save(
                new Member(
                        "Find Member",
                        "find-member-001@test.com",
                        "9876543211",
                        MembershipType.STUDENT
                )
        );

        Optional<Member> result =
                memberRepository.findById(member.getId());

        assertThat(result)
                .isPresent();

        assertThat(result.get().getId())
                .isEqualTo(member.getId());

        assertThat(result.get().getName())
                .isEqualTo("Find Member");
    }

    // ============================================================
    // FIND BY EMAIL
    // ============================================================

    @Test
    void shouldFindMemberByEmailSuccessfully() {

        Member member = memberRepository.save(
                new Member(
                        "Email Member",
                        "email-member-001@test.com",
                        "9876543212",
                        MembershipType.STUDENT
                )
        );

        Optional<Member> result =
                memberRepository.findByEmail(
                        "email-member-001@test.com"
                );

        assertThat(result)
                .isPresent();

        assertThat(result.get().getId())
                .isEqualTo(member.getId());

        assertThat(result.get().getEmail())
                .isEqualTo("email-member-001@test.com");
    }

    // ============================================================
    // FIND BY NON-EXISTING EMAIL
    // ============================================================

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {

        Optional<Member> result =
                memberRepository.findByEmail(
                        "non-existing@test.com"
                );

        assertThat(result)
                .isEmpty();
    }

    // ============================================================
    // FIND BY NAME CONTAINING IGNORE CASE
    // ============================================================

    @Test
    void shouldFindMembersByNameIgnoringCase() {

        memberRepository.save(
                new Member(
                        "Aman Kumar",
                        "name-member-001@test.com",
                        "9876543213",
                        MembershipType.STUDENT
                )
        );

        memberRepository.save(
                new Member(
                        "AMIT KUMAR",
                        "name-member-002@test.com",
                        "9876543214",
                        MembershipType.FACULTY
                )
        );

        memberRepository.save(
                new Member(
                        "Rahul Sharma",
                        "name-member-003@test.com",
                        "9876543215",
                        MembershipType.STUDENT
                )
        );

        List<Member> members =
                memberRepository.findByNameContainingIgnoreCase(
                        "kumar"
                );

        assertThat(members)
                .hasSize(2);

        assertThat(members)
                .allMatch(member ->
                        member.getName()
                                .toLowerCase()
                                .contains("kumar")
                );
    }

    // ============================================================
    // FIND BY MEMBERSHIP TYPE
    // ============================================================

    @Test
    void shouldFindMembersByMembershipType() {

        Member student1 = memberRepository.save(
                new Member(
                        "Student One",
                        "type-member-001@test.com",
                        "9876543216",
                        MembershipType.STUDENT
                )
        );

        Member student2 = memberRepository.save(
                new Member(
                        "Student Two",
                        "type-member-002@test.com",
                        "9876543217",
                        MembershipType.STUDENT
                )
        );

        memberRepository.save(
                new Member(
                        "Faculty One",
                        "type-member-003@test.com",
                        "9876543218",
                        MembershipType.FACULTY
                )
        );

        List<Member> students =
                memberRepository.findByMembershipType(
                        MembershipType.STUDENT
                );

        assertThat(students)
                .hasSize(2);

        assertThat(students)
                .extracting(Member::getId)
                .containsExactlyInAnyOrder(
                        student1.getId(),
                        student2.getId()
                );
    }

    // ============================================================
    // FIND BY STATUS
    // ============================================================

    @Test
    void shouldFindMembersByStatus() {

        Member activeMember = memberRepository.save(
                new Member(
                        "Active Member",
                        "status-member-001@test.com",
                        "9876543219",
                        MembershipType.STUDENT
                )
        );

        Member inactiveMember = memberRepository.save(
                new Member(
                        "Inactive Member",
                        "status-member-002@test.com",
                        "9876543220",
                        MembershipType.STUDENT
                )
        );

        activeMember.setStatus(MemberStatus.ACTIVE);
        inactiveMember.setStatus(MemberStatus.INACTIVE);

        memberRepository.save(activeMember);
        memberRepository.save(inactiveMember);

        List<Member> activeMembers =
                memberRepository.findByStatus(
                        MemberStatus.ACTIVE
                );

        List<Member> inactiveMembers =
                memberRepository.findByStatus(
                        MemberStatus.INACTIVE
                );

        assertThat(activeMembers)
                .hasSize(1);

        assertThat(activeMembers.getFirst().getId())
                .isEqualTo(activeMember.getId());

        assertThat(inactiveMembers)
                .hasSize(1);

        assertThat(inactiveMembers.getFirst().getId())
                .isEqualTo(inactiveMember.getId());
    }

    // ============================================================
    // EXISTS BY EMAIL
    // ============================================================

    @Test
    void shouldReturnTrueWhenEmailExists() {

        memberRepository.save(
                new Member(
                        "Exists Member",
                        "exists-member-001@test.com",
                        "9876543221",
                        MembershipType.STUDENT
                )
        );

        boolean exists =
                memberRepository.existsByEmail(
                        "exists-member-001@test.com"
                );

        assertThat(exists)
                .isTrue();
    }

    // ============================================================
    // EMAIL DOES NOT EXIST
    // ============================================================

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        boolean exists =
                memberRepository.existsByEmail(
                        "does-not-exist@test.com"
                );

        assertThat(exists)
                .isFalse();
    }

    // ============================================================
    // NAME DOES NOT EXIST
    // ============================================================

    @Test
    void shouldReturnEmptyListWhenNameDoesNotExist() {

        List<Member> members =
                memberRepository.findByNameContainingIgnoreCase(
                        "Non Existing Name"
                );

        assertThat(members)
                .isEmpty();
    }

    // ============================================================
    // MEMBERSHIP TYPE HAS NO MEMBERS
    // ============================================================

    @Test
    void shouldReturnEmptyListWhenMembershipTypeHasNoMembers() {

        List<Member> members =
                memberRepository.findByMembershipType(
                        MembershipType.STAFF
                );

        assertThat(members)
                .isEmpty();
    }

    // ============================================================
    // STATUS HAS NO MEMBERS
    // ============================================================

    @Test
    void shouldReturnEmptyListWhenStatusHasNoMembers() {

        List<Member> members =
                memberRepository.findByStatus(
                        MemberStatus.SUSPENDED
                );

        assertThat(members)
                .isEmpty();
    }
}