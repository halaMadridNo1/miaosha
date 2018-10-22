package com.imooc.service;

import com.imooc.dao.UserDao;
import com.imooc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ${User} on 2018/10/11
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx() {
        User u1= new User();
        u1.setId(2);
        u1.setName("2222");
        userDao.insert(u1);

        User u2= new User();
        u2.setId(1);
        u2.setName("11111");
        userDao.insert(u2);

        return true;
    }
}
