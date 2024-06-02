package roomescape.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.application.dto.request.SignupRequest;
import roomescape.application.dto.response.MemberResponse;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.Role;
import roomescape.exception.BadRequestException;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberResponse createMember(SignupRequest request) {
        Member member = new Member(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                Role.USER
        );

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new BadRequestException(String.format("해당 이메일의 회원이 이미 존재합니다. (email: %s)", member.getEmail()));
        }

        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse getById(long id) {
        Member member = memberRepository.getById(id);

        return MemberResponse.from(member);
    }
}
