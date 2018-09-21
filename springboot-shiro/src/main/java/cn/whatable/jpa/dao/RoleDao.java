package cn.whatable.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import cn.whatable.jpa.entity.Role;

public interface RoleDao extends JpaRepository<Role, Long>, CrudRepository<Role, Long> {

}
