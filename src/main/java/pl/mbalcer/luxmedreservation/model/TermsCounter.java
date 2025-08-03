package pl.mbalcer.luxmedreservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermsCounter {
    private int termsNumber;
    private List<Object> partialTermsCounters; // Array of unknown type
}