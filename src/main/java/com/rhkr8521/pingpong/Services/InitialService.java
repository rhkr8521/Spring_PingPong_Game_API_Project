package com.rhkr8521.pingpong.Services;

import com.rhkr8521.pingpong.Entitys.UserEntity;
import com.rhkr8521.pingpong.Repositorys.RoomRepository;
import com.rhkr8521.pingpong.Repositorys.UserRepository;
import com.rhkr8521.pingpong.Repositorys.UserRoomRepository;
import com.rhkr8521.pingpong.dto.fakerapiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
public class InitialService {

    private static final Logger log = LoggerFactory.getLogger(InitialService.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    public InitialService(UserRepository userRepository, RestTemplate restTemplate, RoomRepository roomRepository, UserRoomRepository userRoomRepository) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.roomRepository = roomRepository;
        this.userRoomRepository = userRoomRepository;
    }

    //API 파싱 후 유저정보 등록
    public void initialize(int seed, int quantity) {
        deleteAllDataFromTables();
        log.info("Initializing with seed: {}, quantity: {}", seed, quantity);
        String url = String.format("https://fakerapi.it/api/v1/users?_seed=%d&_quantity=%d&_locale=ko_KR", seed, quantity);
        fakerapiResponse response = restTemplate.getForObject(url, fakerapiResponse.class);

        if (response != null && response.getData() != null) {
            response.getData().forEach(fakerUser -> {
                UserEntity userEntity = new UserEntity();
                userEntity.setFakerId(fakerUser.getId());
                userEntity.setName(fakerUser.getUsername());
                userEntity.setEmail(fakerUser.getEmail());

                // ID에 따라 ACTIVE, WAIT, NON_ACTIVE 부여
                if (fakerUser.getId() <= 30) {
                    userEntity.setStatus(UserEntity.UserStatus.ACTIVE);
                } else if (fakerUser.getId() <= 60) {
                    userEntity.setStatus(UserEntity.UserStatus.WAIT);
                } else {
                    userEntity.setStatus(UserEntity.UserStatus.NON_ACTIVE);
                }

                userEntity.setCreatedAt(LocalDateTime.now());
                userEntity.setUpdatedAt(LocalDateTime.now());
                userRepository.save(userEntity);
            });
        }
    }

    // 모든 테이블 데이터 삭제
    private void deleteAllDataFromTables() {
        userRepository.deleteAll();
        roomRepository.deleteAll();
        userRoomRepository.deleteAll();
    }
    
}
