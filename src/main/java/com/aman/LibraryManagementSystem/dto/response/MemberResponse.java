package com.aman.LibraryManagementSystem.dto.response;

import com.aman.LibraryManagementSystem.enums.MembershipType;
import com.aman.LibraryManagementSystem.enums.MemberStatus;

public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private MembershipType membershipType;
    private MemberStatus memberStatus;

    public MemberResponse(){
    }

    public MemberResponse(Long id,
                          String name,
                          String email,
                          String phone,
                          MembershipType membershipType,
                          MemberStatus memberStatus)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.memberStatus = memberStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MemberStatus getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }
}
