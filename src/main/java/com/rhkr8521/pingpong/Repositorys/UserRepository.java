package com.rhkr8521.pingpong.Repositorys;

import com.rhkr8521.pingpong.Entitys.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
