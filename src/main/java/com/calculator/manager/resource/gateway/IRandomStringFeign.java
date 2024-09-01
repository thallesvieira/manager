package com.calculator.manager.resource.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interface feign configured to call random string.
 * Get url from environment and configure method with parameters
 */
@FeignClient(name = "randomString", url = "${random.string.url}")
public interface IRandomStringFeign {

    @GetMapping("/strings/")
    String getRandomStrings(
            @RequestParam("num") int num,
            @RequestParam("len") int len,
            @RequestParam("digits") String digits,
            @RequestParam("upperalpha") String upperalpha,
            @RequestParam("loweralpha") String loweralpha,
            @RequestParam("unique") String unique,
            @RequestParam("format") String format,
            @RequestParam("rnd") String rnd
    );
}
