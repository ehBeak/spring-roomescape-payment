package roomescape.domain.reservation.detail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import roomescape.domain.exception.DomainValidationException;

@Embeddable
public class ReservationDetail {

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ReservationTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Theme theme;

    protected ReservationDetail() {
    }

    public ReservationDetail(LocalDate date, ReservationTime time, Theme theme) {
        validate(date, time, theme);

        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validate(LocalDate date, ReservationTime time, Theme theme) {
        if (date == null) {
            throw new DomainValidationException("예약 날짜는 필수 값입니다.");
        }

        if (time == null) {
            throw new DomainValidationException("예약 시간은 필수 값입니다.");
        }

        if (theme == null) {
            throw new DomainValidationException("테마는 필수 값입니다.");
        }
    }

    public boolean isBefore(LocalDateTime currentDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());

        return dateTime.isBefore(currentDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationDetail that = (ReservationDetail) o;
        return Objects.equals(date, that.date) && Objects.equals(time, that.time)
                && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, theme);
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
