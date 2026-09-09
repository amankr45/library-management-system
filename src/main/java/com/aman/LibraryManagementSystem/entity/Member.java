package com.aman.LibraryManagementSystem.entity;

import com.aman.LibraryManagementSystem.enums.MembershipType;
import com.aman.LibraryManagementSystem.enums.MemberStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "memberSeqGen"
    )
    @SequenceGenerator(
            name = "memberSeqGen",
            sequenceName = "member_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MembershipType membershipType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberStatus status;

    @OneToMany(
            mappedBy = "member",
            fetch = FetchType.LAZY
    )
    private List<BookIssue> bookIssues = new ArrayList<>();

    protected Member() {
    }

    public Member(
            String name,
            String email,
            String phone,
            MembershipType membershipType
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.status = MemberStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public List<BookIssue> getBookIssues(){
        return bookIssues;
    }

    // Relationship helper method
    public void addBookIssues(BookIssue bookIssue){
        bookIssues.add(bookIssue);
    }
}