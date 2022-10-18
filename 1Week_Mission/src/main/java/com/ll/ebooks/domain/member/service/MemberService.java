package com.ll.ebooks.domain.member.service;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberInfoModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberPasswordModifyRequestDto;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.entity.Role;
import com.ll.ebooks.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender javaMailSender;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {
        /* 비밀번호 암호화 */
        joinRequestDto.encodingPassword(passwordEncoder.encode(joinRequestDto.getPassword()));
        /* 가입 시 nickname이 존재하지 않으면, Member 존재 하면 WRITER 역할 부여 */
        if(joinRequestDto.getNickname().equals("")) {
            return memberRepository.save(joinRequestDto.toEntity(Role.MEMBER)).getId();
        }

        Long memberId = memberRepository.save(joinRequestDto.toEntity(Role.WRITER)).getId();

        /* 메일 전송 */
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(joinRequestDto.getEmail());
        message.setSubject("멋북스 회원가입을 축하합니다.");
        message.setText("재밋게 즐겨주세요.");
        javaMailSender.send(message);

        return memberId;

    }

    @Transactional
    public Long modify(MemberInfoModifyRequestDto memberInfoModifyRequestDto, Member member) {
        if(memberInfoModifyRequestDto.getNickname().equals("")) {
            member.modify(memberInfoModifyRequestDto.getEmail(), memberInfoModifyRequestDto.getNickname(), Role.MEMBER);
            return member.getId();
        }

        member.modify(memberInfoModifyRequestDto.getEmail(), memberInfoModifyRequestDto.getNickname(), Role.WRITER);
        return member.getId();
    }

    @Transactional
    public Long modifyPassword(MemberPasswordModifyRequestDto memberPasswordModifyRequestDto, Member member) {

        memberPasswordModifyRequestDto.encodingPassword(passwordEncoder.encode(memberPasswordModifyRequestDto.getPassword()));
        member.modifyPassword(memberPasswordModifyRequestDto.getPassword());

        return member.getId();
    }
    @Transactional
    public boolean passwordConfirm(String password, Member member) {
        //비밀번호가 같으면 true 리턴
        if(passwordEncoder.matches(password, member.getPassword())) {
            return true;
        }

        return false;
    }


}
