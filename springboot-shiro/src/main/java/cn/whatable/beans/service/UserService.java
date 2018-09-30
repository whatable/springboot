package cn.whatable.beans.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import cn.whatable.jpa.dao.UserDao;
import cn.whatable.jpa.entity.User;

@Service
public class UserService {
	@Autowired
	UserDao userDao;

	public User findByUsername(String username) {
		User u = new User();
		u.setUsername(username);
		Optional<User> opt = userDao.findOne(Example.of(u));
		return opt.isPresent() ? opt.get() : null;
	}
}
