package cn.whatable.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@EnableAutoConfiguration
public class LoginController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("login")
	String loginStart() {
		return "login";
	}

	@PostMapping("login")
	ModelAndView loginSubmit(@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password, HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelAndView mv) throws IOException {
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(username, password));
			session.setAttribute("user", subject.getPrincipal());
			WebUtils.redirectToSavedRequest(request, response, "");
			return null;
		} catch (UnknownAccountException e) {
			// 账号不存在
			logger.info(">>>> 账号不存在");
			mv.addObject("code", "ACCOUNT_NOT_EXISTS");
			mv.setViewName("403");
			return mv;
		} catch (IncorrectCredentialsException e) {
			// 密码不正确
			logger.info(">>>> 密码不正确");
			mv.addObject("code", "PASSWORD_NOT_MATCH");
			mv.setViewName("403");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("500");
			mv.addObject("code", "EXCEPTION::" + e.getMessage());
			return mv;
		}
	}

	@RequestMapping("403")
	String e403() {
		return "403";
	}
}
