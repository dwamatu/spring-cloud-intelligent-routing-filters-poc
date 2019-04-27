package com.ideabrains.helloservice;

import com.ideabrains.helloservice.dto.RequestValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by kamauwamatu
 * Project spring-cloud-intelligent-routing-filters-poc
 * User: kamauwamatu
 * Date: 2019-04-26
 * Time: 19:08
 */
@RestController



public class HelloController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String hello(@RequestHeader("x-location") String location, @RequestBody RequestValue requestValue) {

        return requestValue.getValue() + " " + location ;
    }

}
