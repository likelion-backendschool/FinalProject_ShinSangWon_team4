package com.ll.ebooks.domain.member.service;

import com.ll.ebooks.domain.cash.entity.CashLog;
import com.ll.ebooks.domain.cash.service.CashService;
import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberInfoModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberPasswordModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.PasswordFindRequestDto;
import com.ll.ebooks.domain.member.dto.request.UsernameFindRequestDto;
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
import java.util.Random;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private final CashService cashService;

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

        /* 메일 전송 - 메일이 너무 많이 보내져서 비활성화 ... (기능 동작)*/

/*        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(joinRequestDto.getEmail());
        message.setSubject("멋북스 회원가입을 축하합니다.");
        message.setText("재밋게 즐겨주세요.");
        javaMailSender.send(message);*/

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

    @Transactional
    public Long findUsername(UsernameFindRequestDto usernameFindRequestDto) {
        Member member = memberRepository.findByEmail(usernameFindRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        /* 메일 전송 */
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(usernameFindRequestDto.getEmail());
        message.setSubject("요청하신 아이디를 알려드립니다.");
        message.setText("아이디는 %s 입니다.".formatted(member.getUsername()));
        javaMailSender.send(message);

        return member.getId();
    }
    @Transactional
    public Long findPassword(PasswordFindRequestDto passwordFindRequestDto) {
        Member member = memberRepository.findByEmail(passwordFindRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        String tempPassword = getTempPassword();

        /* 메일 전송 */
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(passwordFindRequestDto.getEmail());
        message.setSubject("[멋북스] 요청하신 임시 비밀번호입니다.");
        message.setText("비밀번호는 %s 입니다.".formatted(tempPassword));
        javaMailSender.send(message);

        member.modifyPassword(passwordEncoder.encode(tempPassword));

        return member.getId();
    }

    @Transactional
    public Long addCash(Member member, int price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        int newRestCash = member.getRestCash() + cashLog.getPrice();
        member.modifyCash(newRestCash);
        memberRepository.save(member);

        return member.getId();
    }

    //select문으로 매번 가져와야 하기 때문에 이렇게 해야 함
    public int getRestCash(Member member) {
        Member selectedMember = findByUsername(member.getUsername()).get();

        return selectedMember.getRestCash();
    }

    //임시 비밀번호 발급
    public String getTempPassword() {
        // 숫자 0
        final int leftLimit = 48;
        // 소문자 'z'
        final int rightLimit = 122;
        final int passwordLength = 10;

        Random random = new Random();
        String tempPassword = random.ints(leftLimit, rightLimit + 1)
                .filter(x -> (x <= 57 || x >= 65) && (x <= 90 || x >= 97))
                .limit(passwordLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return tempPassword;
    }
}
