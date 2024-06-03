package roomescape.presentation.api;

import java.net.URI;
import java.time.Clock;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.application.WaitingService;
import roomescape.application.dto.request.WaitingRequest;
import roomescape.application.dto.response.WaitingResponse;
import roomescape.presentation.Auth;
import roomescape.presentation.dto.Accessor;
import roomescape.presentation.dto.request.WaitingWebRequest;

@RestController
@RequestMapping("/waitings")
public class WaitingController {

    private final WaitingService waitingService;
    private final Clock clock;

    public WaitingController(WaitingService waitingService, Clock clock) {
        this.waitingService = waitingService;
        this.clock = clock;
    }

    @PostMapping
    public ResponseEntity<WaitingResponse> addWaiting(
            @RequestBody @Valid WaitingWebRequest request,
            @Auth Accessor accessor
    ) {
        WaitingRequest waitingRequest = request.toWaitingRequest(clock, accessor.id());
        WaitingResponse waitingResponse = waitingService.addWaiting(waitingRequest);

        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id()))
                .body(waitingResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaiting(
            @PathVariable Long id,
            @Auth Accessor accessor
    ) {
        waitingService.deleteWaitingById(id, accessor.id());

        return ResponseEntity.noContent().build();
    }
}
