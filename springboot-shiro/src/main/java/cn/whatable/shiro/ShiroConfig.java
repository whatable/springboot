package cn.whatable.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro全局初始配置
 * 
 * @author jiangshaoh
 *
 */
@Configuration
public class ShiroConfig {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * shiro过滤工厂bean：：设置过滤和跳转
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		logger.info(">>>> initializing shiroFilterFactoryBean");

		ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();

		// Shiro的核心安全接口,这个属性是必须的
		factory.setSecurityManager(securityManager);
		factory.setLoginUrl("/login");
		factory.setSuccessUrl("/");
		factory.setUnauthorizedUrl("/403");

		/*
		 * 定义shiro过滤链 Map结构 *
		 * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
		 * * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种 *
		 * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.
		 * FormAuthenticationFilter
		 */
		Map<String, String> filter = new LinkedHashMap<String, String>();
		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filter.put("/logout", "logout");

		// <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filter.put("/login", "anon");// anon 可以理解为不拦截
		filter.put("/register", "anon");
		filter.put("/favicon.ico", "anon");
		filter.put("/h2-console/**", "anon");
//		filter.put("/**", "authc");
		filter.put("/**", "anon");

		factory.setFilterChainDefinitionMap(filter);

		return factory;
	}

	// 配置核心安全事务管理器bean
	@Bean
	public SecurityManager securityManager(CustomizedRealm customizedRealm) {
		logger.info(">>>> initializing customizedRealm");
		return new DefaultWebSecurityManager(customizedRealm);
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

}
