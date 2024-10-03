package com.example.member.service;

import com.example.global.security.JwtToken;
import com.example.global.security.JwtTokenProvider;
import com.example.member.exception.MemberException;
import com.example.member.model.Roles;
import com.example.member.model.dto.request.SignUpRequest;
import com.example.member.model.dto.request.SignInRequest;
import com.example.member.model.dto.response.MemberSaveResponse;
import com.example.member.model.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.member.exception.MemberErrorCode.DUPLICATE_PASSWORD;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtToken signIn(SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getName(), signInRequest.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }


    public MemberSaveResponse signUp(SignUpRequest signUpRequest) {
        validateDuplicateEmail(signUpRequest.getEmail());

        final Member member = signUpRequest.toMember();
        member.updatePassword(passwordEncoder.encode(signUpRequest.getPassword())); //동시성?
        member.updateRoles(Roles.USER);

        Member savedMember;
        try {
            savedMember = memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new MemberException(DUPLICATE_PASSWORD);
        }

        return MemberSaveResponse.fromMember(savedMember);
    }

    private void validateDuplicateEmail(String email) {
        final boolean isDuplicate = memberRepository.findByEmail(email).isPresent();
        if (isDuplicate) {
            throw MemberException.fromErrorCode(DUPLICATE_PASSWORD);
        }
    }
}
