package pl.mbalcer.luxmedreservation.term;

import pl.mbalcer.luxmedreservation.authorization.LoginRequest;
import pl.mbalcer.luxmedreservation.authorization.UserCredential;

import java.util.HashSet;

public final class TermSearchMapper {

    private TermSearchMapper() {
    }

    public static TermSearchDTO toDto(TermSearch ts) {
        UserCredential userCredential = ts.getUser();
        return new TermSearchDTO(
                ts.getId(),
                new LoginRequest(userCredential.getEmail(), userCredential.getPasswordEncrypted()),
                ts.getCityId(),
                ts.getServiceVariantId(),
                ts.getDoctorIds(),
                ts.getDateFrom(),
                ts.getDateTo(),
                ts.isDelocalized(),
                ts.getStatus(),
                ts.getCreatedAt(),
                ts.getUpdatedAt(),
                ts.getLastCheckedAt(),
                ts.getLastFoundAt()
        );
    }

    public static TermSearch fromCreateDto(CreateTermSearchRequest termSearchRequest) {
        TermSearch termSearch = new TermSearch();
        termSearch.setCityId(termSearchRequest.cityId());
        termSearch.setDateFrom(termSearchRequest.dateFrom());
        termSearch.setDateTo(termSearchRequest.dateTo());
        termSearch.setServiceVariantId(termSearchRequest.serviceVariantId());
        termSearch.setDelocalized(termSearchRequest.delocalized());
        termSearch.setDoctorIds(termSearchRequest.doctorIds() == null ? new HashSet<>() : new HashSet<>(termSearchRequest.doctorIds()));
        return termSearch;
    }
}