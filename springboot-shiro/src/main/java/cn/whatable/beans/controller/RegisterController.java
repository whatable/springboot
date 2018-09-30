package cn.whatable.beans.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.whatable.jpa.dao.UserDao;
import cn.whatable.jpa.entity.User;
import cn.whatable.util.Encoder;

@Controller
@EnableAutoConfiguration
public class RegisterController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDao userDao;

	@GetMapping("register")
	String registerStart() {
		return "register";
	}

	@PostMapping("register")
	ModelAndView registerSubmit(@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password, HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv) throws IOException {

		User u = new User();
		u.setUsername(username);
		Optional<User> exists = userDao.findOne(Example.of(u));
		if (exists.isPresent()) {
			// 用户名已注册
			mv.addObject("code", "FAIL::USERNAME_CONFLICT");
		} else {
			String encrypted = Encoder.base64Encode(password);
			u.setPassword(encrypted);
			u = userDao.save(u);

			mv.addObject("code", "SUCCESS");
		}
		mv.setViewName("register-result");
		return mv;
	}

}
