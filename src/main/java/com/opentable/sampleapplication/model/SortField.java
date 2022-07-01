package com.opentable.sampleapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortField {
    private String fieldName;
    private String sortOrder;
}
