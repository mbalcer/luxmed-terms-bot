package pl.mbalcer.luxmedreservation.term;

import org.springframework.stereotype.Service;
import pl.mbalcer.luxmedreservation.client.model.MedicalAppointmentResponse;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TermsProcessingService {

    public List<TermsInfoForDay> extractAvailableTerms(MedicalAppointmentResponse response) {
        return Optional.ofNullable(Objects.requireNonNull(response).termsForService())
                .orElseThrow()
                .termsInfoForDays()
                .stream()
                .filter(day -> day.termsCounter().termsNumber() > 0)
                .toList();
    }
}