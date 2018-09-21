package cn.whatable.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import cn.whatable.jpa.entity.Permission;

public interface PermissionDao extends JpaRepository<Permission, Long>, CrudRepository<Permission, Long> {

}
