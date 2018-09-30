package cn.whatable.beans.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("demo")
public class DemoController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping()
	String index() {
		return "demo/index";
	}

	@RequiresRoles("role")
	@GetMapping("need-authorized-by-role")
	String needAuthorizedByRole() {
		return "demo/need-authorized-by-role";
	}

	@RequiresRoles("role")
	@RequiresPermissions("permission")
	@GetMapping("need-authorized-by-permission")
	String needAuthorizedByPermission() {
		return "demo/need-authorized-by-permission";
	}

}
