package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mbalcer.luxmedreservation.model.TermsInfoForDay;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
@Slf4j
public class TermsApi {
    private final TermsService termsService;

    public TermsApi(TermsService termsService) {
        this.termsService = termsService;
    }

    @GetMapping("/check")
    public List<TermsInfoForDay> checkTerms() {
        return termsService.checkTerms();
    }
}
