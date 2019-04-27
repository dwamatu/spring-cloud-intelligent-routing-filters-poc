package com.ideabrains.helloservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by kamauwamatu
 * Project spring-cloud-intelligent-routing-filters-poc
 * User: kamauwamatu
 * Date: 2019-04-26
 * Time: 19:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestValue {
    private String key;
    private String value;
}
