package cn.whatable.shiro;

import java.util.Optional;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import cn.whatable.jpa.dao.UserDao;
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
	UserDao userDao;

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
		return new SimpleAuthorizationInfo();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		final String username = (String) token.getPrincipal();
		User u = new User();
		u.setUsername(username);
		Example<User> example = Example.of(u);
		Optional<User> opt = userDao.findOne(example);
		if (opt.isPresent()) {
			u = opt.get();
			return new SimpleAuthenticationInfo(u, u.getPassword(), this.getName());
		}
		return null;// 本方法返回null就是用户不存在::UnknownAccountException
	}

}