package com.regalaxy.phonesin.back.controller;

import com.regalaxy.phonesin.back.model.BackDto;
import com.regalaxy.phonesin.back.model.service.BackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BackController {

    private final BackService backService;

    // 반납 신청서 작성하기
    // RequestBody로 JSON 데이터로 받기
    @PostMapping("/back/apply")
    public ResponseEntity<Map<String, Object>> apply(@RequestBody BackDto backDto) {
        Map<String, Object> resultMap = new HashMap<>();
        backService.apply(backDto);
        resultMap.put("back", backDto);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    // 반납 신청서 상세 정보보기
    @GetMapping("/back/info")
    public ResponseEntity<Map<String, Object>> backInfo(@RequestBody BackDto backDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("back", backService.backInfo(backDto.getBackId()));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    // 반납 신청서 수정
    // 나중에 유저는 rentalStatus를 수정하지 못하도록 설정.
    @PutMapping("/back/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody BackDto backDto) {
        Map<String, Object> resultMap = new HashMap<>();
        BackDto updatedBackDto = backService.updateBack(backDto);
        resultMap.put("updatedBack", updatedBackDto);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }
}