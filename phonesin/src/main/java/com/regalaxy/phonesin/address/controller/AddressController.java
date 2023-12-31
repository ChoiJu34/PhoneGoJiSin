package com.regalaxy.phonesin.address.controller;

import com.regalaxy.phonesin.address.model.AddressDto;
import com.regalaxy.phonesin.address.model.AgencyDto;
import com.regalaxy.phonesin.address.model.LocationDto;
import com.regalaxy.phonesin.address.model.service.AddressService;
import com.regalaxy.phonesin.member.model.jwt.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@CrossOrigin("*")
@Api(value = "주소 API", description = "주소 Controller")
public class AddressController {
    private final AddressService addressService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";


    @ApiOperation(value = "주소 리스트 조회")
    @GetMapping("/list")//주소 목록
    public ResponseEntity<?> addressList(@ApiIgnore @RequestHeader String authorization){
        String token = authorization.replace("Bearer ", "");
        Long memberId = jwtTokenProvider.getMemberId(token);
        List<AddressDto> list = addressService.addressList(memberId);
        return new ResponseEntity<List<AddressDto>>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "대리점 주소 리스트 조회")
    @GetMapping("/list/samsung")//삼성 대리점 목록//현재 위치
    public ResponseEntity<?> samsungList(@RequestParam Double latitude, @RequestParam Double longitude){
        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(latitude);
        locationDto.setLongitude(longitude);
        List<AgencyDto> list = addressService.samsungList(locationDto);
        return new ResponseEntity<List<AgencyDto>>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "대리점 주소 검색 조회")
    @GetMapping("/list/samsung/search")//삼성 대리점 목록//검색
    public ResponseEntity<?> samsungListSearsch(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam String search){
        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(latitude);
        locationDto.setLongitude(longitude);
        locationDto.setSearch(search);
        List<AgencyDto> list = addressService.samsungListSearch(locationDto);
        return new ResponseEntity<List<AgencyDto>>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "주소 삭제")
    @DeleteMapping("/delete/{addressId}")//삼성 대리점 목록//검색
    public ResponseEntity<?> delete(@PathVariable("addressId") Long address_id){
        addressService.delete(address_id);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

    @ApiOperation(value = "주소 생성")
    @PostMapping("/create/{address}")//삼성 대리점 목록//검색
    public ResponseEntity<?> create(@PathVariable("address") String address, @ApiIgnore @RequestHeader String authorization){
        Long memberId = jwtTokenProvider.getMemberId(authorization.replace("Bearer ", ""));
        addressService.create(address, memberId);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }
}
