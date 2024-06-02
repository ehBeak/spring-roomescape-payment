package roomescape.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.application.dto.request.LoginRequest;
import roomescape.application.dto.response.MemberResponse;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.exception.BadRequestException;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public String createToken(Long memberId) {
        return tokenProvider.createToken(memberId.toString());
    }

    public Long getMemberIdByToken(String token) {
        return tokenProvider.getMemberId(token);
    }

    public MemberResponse validatePassword(LoginRequest loginRequest) {
        Member member = memberRepository.getByEmail(loginRequest.email());

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }

        return MemberResponse.from(member);
    }
}
