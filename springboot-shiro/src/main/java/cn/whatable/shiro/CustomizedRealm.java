package cn.whatable.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.whatable.beans.service.UserService;
import cn.whatable.jpa.entity.Permission;
import cn.whatable.jpa.entity.Role;
import cn.whatable.jpa.entity.User;
import cn.whatable.util.Encoder;

/**
 * 自定义的realm bean，实现了身份验证（Authentication）和权限认证（Authorization）的逻辑。Realm
 * bean会被注入到核心安全管理器{@link SecurityManager} bean中使用，后者在{@link ShiroConfig}中声明。
 * 
 * @author jiangshaoh
 *
 */
@Component
public class CustomizedRealm extends AuthorizingRealm {

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public CustomizedRealm() {
		// 自定义密码（Credential）匹配验证的逻辑（基于MD5加密）
		this.setCredentialsMatcher(new CredentialsMatcher() {
			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				if (info != null && token.getCredentials() != null) {
					if (token.getCredentials() instanceof char[]) {
						return info.getCredentials().equals(Encoder.base64Encode((char[]) token.getCredentials()));
					}
				}
				return false;
			}
		});
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 校验权限
		final SimpleAuthorizationInfo anthorization = new SimpleAuthorizationInfo();

		User u = (User) principals.getPrimaryPrincipal();
		logger.info(">>>>> 校验权限 for username={}", u.getUsername());
		
		for (Role role : u.getRoles()) {
			anthorization.addRole(role.getRoleName());
			logger.info(">>>>> 填加角色 {} @ {}", role.getRoleName(), u.getUsername());
			for (Permission permission : role.getPermissions()) {
				anthorization.addStringPermission(permission.getPermission());
				logger.info(">>>>> 填加授权 {} @ {}", permission.getPermission(), u.getUsername());
			}
		}

		return anthorization;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 验证登录身份
		User u = userService.findByUsername((String) token.getPrincipal());
		return u == null ? null : new SimpleAuthenticationInfo(u, u.getPassword(), this.getName());// 本方法返回null就是用户不存在::UnknownAccountException
	}

}