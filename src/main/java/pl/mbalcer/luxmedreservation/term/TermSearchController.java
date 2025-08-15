package pl.mbalcer.luxmedreservation.term;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class TermSearchController {

    private final TermSearchService service;

    public TermSearchController(TermSearchService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TermSearchDTO create(@RequestHeader("X-User-Email") String userEmail,
                                @RequestBody @Valid CreateTermSearchRequest req) {
        TermSearch saved = service.create(userEmail, req);
        return TermSearchMapper.toDto(saved);
    }

    @GetMapping
    public List<TermSearchDTO> list(@RequestHeader("X-User-Email") String userEmail) {
        return service.listForUser(userEmail).stream()
                .map(TermSearchMapper::toDto)
                .toList();
    }
}

