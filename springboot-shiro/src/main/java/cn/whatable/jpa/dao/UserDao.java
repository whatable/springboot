package cn.whatable.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import cn.whatable.jpa.entity.User;

public interface UserDao extends JpaRepository<User, Long> {

}
