package roomescape.domain.member;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import roomescape.domain.exception.DomainValidationException;

@Entity
public class Member {

    private static final int EMAIL_MAX_LENGTH = 255;
    private static final int PASSWORD_MAX_LENGTH = 255;
    private static final int NAME_MAX_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    protected Member() {
    }

    public Member(String email, String password, String name, Role role) {
        this(null, email, password, name, role);
    }

    public Member(Long id, String email, String password, String name, Role role) {
        validate(email, password, name, role);

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    private void validate(String email, String password, String name, Role role) {
        validateEmail(email);
        validatePassword(password);
        validateName(name);
        validateRole(role);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainValidationException("이메일은 필수 값입니다.");
        }

        if (email.length() > EMAIL_MAX_LENGTH) {
            throw new DomainValidationException(String.format("이메일은 %d자를 넘을 수 없습니다.", EMAIL_MAX_LENGTH));
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new DomainValidationException("비밀번호는 필수 값입니다.");
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new DomainValidationException(String.format("비밀번호는 %d자를 넘을 수 없습니다.", PASSWORD_MAX_LENGTH));
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("이름은 필수 값입니다.");
        }

        if (name.length() > NAME_MAX_LENGTH) {
            throw new DomainValidationException(String.format("이름은 %d자를 넘을 수 없습니다.", NAME_MAX_LENGTH));
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new DomainValidationException("역할은 필수 값입니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member member)) {
            return false;
        }

        return this.getId() != null && Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
