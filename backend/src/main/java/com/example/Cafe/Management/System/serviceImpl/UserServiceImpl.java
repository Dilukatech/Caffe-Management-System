package com.example.Cafe.Management.System.serviceImpl;

import com.example.Cafe.Management.System.JWT.CustomerUserDetailService;
import com.example.Cafe.Management.System.JWT.JwtFilter;
import com.example.Cafe.Management.System.JWT.JwtUtil;
import com.example.Cafe.Management.System.POJO.User;
import com.example.Cafe.Management.System.constents.CafeConstants;
import com.example.Cafe.Management.System.dao.UserDao;
import com.example.Cafe.Management.System.service.UserService;
import com.example.Cafe.Management.System.utils.CafeUtils;
import com.example.Cafe.Management.System.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
//        Log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String,String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
            return false;
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
       log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUserDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUserDetailService.getUserDetail().getEmail(),
                                    customerUserDetailService.getUserDetail().getRole()) + "\"}",
                    HttpStatus.OK);
                }else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
       }catch (Exception ex){
           log.error("",ex);
       } return new ResponseEntity<String>("{\"message\":\"" + "Bad Credential." + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
      try{
        if(jwtFilter.isAdmin()){
            return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
        }
      }catch(Exception ex){
          ex.printStackTrace();
      }
      return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
       try{
        if (jwtFilter.isAdmin()){
           Optional<User> optional =  userDao.findById(Integer.parseInt(requestMap.get("id")));
           if(!optional.isEmpty()){
              userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
              return CafeUtils.getResponseEntity("User Status updated Successfully.",HttpStatus.OK);
           }else{
             return CafeUtils.getResponseEntity("User id doesn't exist.",HttpStatus.OK);
           }
        }else{
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
