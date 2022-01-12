package com.javainuse.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin()
public class HelloWorldController {

	@GetMapping( "/hello" )
	public String hello(Principal principal) {
		return "Hello EPAMers!";
	}

}
