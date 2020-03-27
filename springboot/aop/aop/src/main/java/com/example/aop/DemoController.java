package com.example.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test1")

public class DemoController {

    @RequestMapping(value="get" , method= RequestMethod.GET)
    public ResponseEntity<Map<String,String>> test(){
        Map<String,String> resp = new HashMap<>();
        resp.put("msg","success");
        return new ResponseEntity<Map<String,String>>(resp, HttpStatus.OK);
    }


    @CustomAn
    @RequestMapping(value="getann" , method= RequestMethod.GET)
    public ResponseEntity<Map<String,String>> test2(){
        Map<String,String> resp = new HashMap<>();
        resp.put("msg","success custom");
        return new ResponseEntity<Map<String,String>>(resp, HttpStatus.OK);
    }
}
