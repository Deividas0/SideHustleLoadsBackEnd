package com.example.demo.Utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${SQLURL}")
    public String SQLURL;

    @Value("${SQLUSERNAME}")
    public String SQLUSERNAME;

    @Value("${SQLPASSWORD}")
    public String SQLPASSWORD;
}
