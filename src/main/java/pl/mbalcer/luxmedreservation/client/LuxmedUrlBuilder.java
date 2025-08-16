package pl.mbalcer.luxmedreservation.client;

import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LuxmedUrlBuilder {
    private static final String BASE_URL = "https://portalpacjenta.luxmed.pl/PatientPortal/NewPortal/terms/index";

    private Integer searchPlaceId;
    private Integer serviceVariantId;
    private LocalDate searchDateFrom;
    private LocalDate searchDateTo;
    private Boolean delocalized;
    private List<Long> doctorsIds = new ArrayList<>();

    public LuxmedUrlBuilder searchPlaceId(int id) {
        this.searchPlaceId = id;
        return this;
    }

    public LuxmedUrlBuilder serviceVariantId(int id) {
        this.serviceVariantId = id;
        return this;
    }

    public LuxmedUrlBuilder searchDateFrom(LocalDate date) {
        this.searchDateFrom = date;
        return this;
    }

    public LuxmedUrlBuilder searchDateTo(LocalDate date) {
        this.searchDateTo = date;
        return this;
    }

    public LuxmedUrlBuilder delocalized(boolean value) {
        this.delocalized = value;
        return this;
    }

    public LuxmedUrlBuilder addDoctorId(Long id) {
        this.doctorsIds.add(id);
        return this;
    }

    public String build() {
        if (searchPlaceId == null || serviceVariantId == null || searchDateFrom == null || searchDateTo == null || delocalized == null) {
            throw new IllegalStateException("Brak wymaganych parametrów");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("searchPlace.id", searchPlaceId)
                .queryParam("serviceVariantId", serviceVariantId)
                .queryParam("searchDateFrom", searchDateFrom)
                .queryParam("searchDateTo", searchDateTo)
                .queryParam("delocalized", delocalized);

        doctorsIds.forEach(id -> builder.queryParam("doctorsIds", id));

        return builder.build().toUriString();
    }
}

