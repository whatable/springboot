package cn.whatable.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class IndexController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("")
	String home() {
		return "home";
	}

	@GetMapping("demo")
	String demo() {
		return "demo";
	}

}
