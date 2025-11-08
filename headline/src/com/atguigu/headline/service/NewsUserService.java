package com.atguigu.headline.service;

import com.atguigu.headline.pojo.NewsUser;

public interface NewsUserService {
    NewsUser findByUsername(String username);

    NewsUser findByUid(Integer userId);

    Integer registUser(NewsUser registUser);
}
