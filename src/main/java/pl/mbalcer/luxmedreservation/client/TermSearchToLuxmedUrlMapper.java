package pl.mbalcer.luxmedreservation.client;

import pl.mbalcer.luxmedreservation.term.TermSearchDTO;

public class TermSearchToLuxmedUrlMapper {
    public static String mapDtoToUrl(TermSearchDTO dto) {
        LuxmedUrlBuilder builder = new LuxmedUrlBuilder()
                .searchPlaceId(dto.cityId())
                .serviceVariantId(dto.serviceVariantId().intValue())
                .searchDateFrom(dto.dateFrom())
                .searchDateTo(dto.dateTo())
                .delocalized(dto.delocalized());

        if (dto.doctorIds() != null) {
            dto.doctorIds().forEach(builder::addDoctorId);
        }

        return builder.build();
    }
}
