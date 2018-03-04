package com.frank.chapter411.service;

import com.frank.chapter411.domain.User;
import com.frank.chapter411.repository.UserRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jianweilin
 * @date 2017/10/24
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Integer PAGE_SIZE = 10;
    private static final Integer PAGE_NUMBER = 0;

    String SCORE_MODE_SUM = "sum"; //权重分求和模式

    Float MIN_SCORE = 10.0F; //由于无相关性的分值默认为1， 设置权重分最小值为10



    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteByUserId(Long userId) {
        userRepository.delete(userId);
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findByUserName(String userName, PageRequest pageRequest) {
        return userRepository.findByUserName(userName,pageRequest);
    }

    @Override
    public List<User> findByUserPhone(String userPhone) {
            return userRepository.findByUserPhone(userPhone);
    }

    @Override
    public List<User> findByUserAge(Integer age)
    {
        return userRepository.findByAge(age);
    }

    @Override
    public List<User> searchUsers(Integer pageNumber, Integer pageSize, String searchContent)
    {
        if(pageSize==0) {
            pageSize = PAGE_SIZE;
        }
        if(pageNumber<0) {
            pageNumber = PAGE_NUMBER;
        }

        SearchQuery searchQuery = getUserSearchQuery(pageNumber,pageSize,searchContent);

        logger.info("\n searchCity: searchContent [" + searchContent + "] \n DSL = \n "
                + searchQuery.getQuery().toString());


        Page<User> cityPage = userRepository.search(searchQuery);
        return cityPage.getContent();

    }

    @Override
    public Iterable<User> search(QueryBuilder queryBuilder)
    {
        return userRepository.search(queryBuilder);
    }

    /**
     * 组装搜索Query对象
     * @param pageNumber
     * @param pageSize
     * @param searchContent
     * @return
     */
    private SearchQuery getUserSearchQuery(int pageNumber, int pageSize, String searchContent) {
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.matchPhraseQuery("userName", searchContent),
                        ScoreFunctionBuilders.weightFactorFunction(1000))
                //.add(QueryBuilders.matchPhraseQuery("other", searchContent),
                //ScoreFunctionBuilders.weightFactorFunction(1000))
                .scoreMode(SCORE_MODE_SUM).setMinScore(MIN_SCORE);
        //设置分页，否则只能按照ES默认的分页给
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder().withPageable(pageable).withQuery(functionScoreQueryBuilder).build();
    }
}
