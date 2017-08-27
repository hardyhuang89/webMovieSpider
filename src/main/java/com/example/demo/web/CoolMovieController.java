package com.example.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movieWebSpider")
public class CoolMovieController {

	@RequestMapping("/welcome")
	public String welcomePage(){
		return "welcome to movieWebSpider!!!";
	}
		
}
