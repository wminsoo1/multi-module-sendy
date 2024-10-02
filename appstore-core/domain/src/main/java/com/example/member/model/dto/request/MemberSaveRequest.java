package com.example.member.model.dto.request;

import com.example.member.model.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberSaveRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @Pattern(regexp = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{8,32}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~32자 입니다.")
    private String password;

    @Email
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private String email;

    private MemberSaveRequest() {
    }

    public Member toMember() {
        return Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
    }
}
