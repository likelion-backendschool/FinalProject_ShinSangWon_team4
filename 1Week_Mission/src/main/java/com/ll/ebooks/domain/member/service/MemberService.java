package com.ll.ebooks.domain.member.service;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.entity.Role;
import com.ll.ebooks.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {
        /*비밀번호 암호화*/
        joinRequestDto.encodingPassword(passwordEncoder.encode(joinRequestDto.getPassword()));
        /*가입 시 nickname이 존재하지 않으면, Member 존재 하면 WRITER 역할 부여*/
        if(joinRequestDto.getNickname().equals("")) {
            return memberRepository.save(joinRequestDto.toEntity(Role.MEMBER)).getId();
        }

        return memberRepository.save(joinRequestDto.toEntity(Role.WRITER)).getId();

    }

}
