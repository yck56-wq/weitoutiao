package com.atguigu.headline.dao.impl;

import com.atguigu.headline.dao.BaseDao;
import com.atguigu.headline.dao.NewsHeadlineDao;
import com.atguigu.headline.pojo.NewsHeadline;
import com.atguigu.headline.pojo.vo.HeadlineDetailVo;
import com.atguigu.headline.pojo.vo.HeadlinePageVo;
import com.atguigu.headline.pojo.vo.HeadlineQueryVo;

import java.util.ArrayList;
import java.util.List;

public class NewsHeadlineDaoImpl extends BaseDao implements NewsHeadlineDao {
    @Override
    public List<HeadlinePageVo> findPageList(HeadlineQueryVo headlinePageVo) {
        List param = new ArrayList();
        String  sql="select hid,title,type,page_views pageViews,TIMESTAMPDIFF(HOUR,create_time,NOW()) pastHours,publisher from news_headline where is_deleted=0 ";
        if(headlinePageVo.getType() != 0){
            sql = sql.concat("and type = ? ");
            param.add(headlinePageVo.getType());
        }
        if (headlinePageVo.getKeyWords() != null && !headlinePageVo.getKeyWords().isEmpty()) {
            sql = sql.concat(" and title  like ? ");
            param.add("%"+headlinePageVo.getKeyWords()+"%");
        }
        sql = sql.concat(" order by pastHours asc , page_views desc ");
        sql = sql.concat(" limit ?, ? ");
        param.add((headlinePageVo.getPageNum() - 1) * headlinePageVo.getPageSize());
        param.add(headlinePageVo.getPageSize());
        return baseQuery(HeadlinePageVo.class,sql,param.toArray());
    }

    @Override
    public int findPageCount(HeadlineQueryVo headlinePageVo) {
        List param = new ArrayList();
        String  sql="select count(1) from news_headline where is_deleted=0 ";
        if(headlinePageVo.getType() != 0){
            sql = sql.concat("and type = ? ");
            param.add(headlinePageVo.getType());
        }
        if (headlinePageVo.getKeyWords() != null && !headlinePageVo.getKeyWords().isEmpty()) {
            sql = sql.concat(" and title  like ? ");
            param.add("%"+headlinePageVo.getKeyWords()+"%");
        }
        Long count = baseQueryObject(Long.class, sql, param.toArray());
        return count.intValue();
    }

    @Override
    public int incrPageViews(int hid) {
        String sql ="update news_headline set page_views = page_views +1 where hid =?";
        return baseUpdate(sql,hid);
    }

    @Override
    public HeadlineDetailVo findHeadDetail(int hid) {
        String sql ="select hid,title,article,type, tname typeName ,page_views pageViews,TIMESTAMPDIFF(HOUR,create_time,NOW()) pastHours,publisher,nick_name author from news_headline h left join  news_type t on h.type = t.tid left join news_user u  on h.publisher = u.uid where hid = ?";
        List<HeadlineDetailVo> list = baseQuery(HeadlineDetailVo.class, sql, hid);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public int addNewsHeadline(NewsHeadline newsHeadline) {
        String sql = "insert into news_headline values(DEFAULT,?,?,?,?,0,NOW(),NOW(),0)";
        return baseUpdate(sql,
                newsHeadline.getTitle(),
                newsHeadline.getArticle(),
                newsHeadline.getType(),
                newsHeadline.getPublisher());
    }

    @Override
    public NewsHeadline findByHid(Integer hid) {
        String sql ="select hid,title,article,type,publisher,page_views pageViews from news_headline where hid =?";
        List<NewsHeadline> list = baseQuery(NewsHeadline.class, sql, hid);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public int update(NewsHeadline newsHeadline) {
        String sql ="update news_headline set title = ?, article= ? , type =? , update_time = NOW() where hid = ? ";
        return baseUpdate(
                sql,
                newsHeadline.getTitle(),
                newsHeadline.getArticle(),
                newsHeadline.getType(),
                newsHeadline.getHid()
        );
    }

    @Override
    public int removeByHid(int hid) {
        String sql ="update news_headline set is_deleted =1 ,  update_time =NOW() where hid = ? ";
        return baseUpdate(sql,hid);
    }
}
