package roomescape.application.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WaitingRequest(
        LocalDateTime currentDateTime,
        LocalDate date,
        Long themeId,
        Long timeId,
        Long memberId
) {
}
