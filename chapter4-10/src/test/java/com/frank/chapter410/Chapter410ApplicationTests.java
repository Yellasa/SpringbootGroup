package com.frank.chapter410;

import com.alibaba.fastjson.JSON;
import com.frank.chapter410.domain.User;
import com.frank.chapter410.service.UserService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Chapter410ApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private Client client;
	@Test
	public void test_001(){
		List<User> userList = new ArrayList<>();
		Iterator<User> iterator = userService.findAll().iterator();
		while (iterator.hasNext()){
			userList.add(iterator.next());
		}
		System.out.println("userList => " + JSON.toJSONString(userList));
	}

	@Test
	public void test_002(){
		Page<User> userPage = userService.findByUserName("张三",new PageRequest(0,10));
		System.out.println("element nums => " + userPage.getTotalElements());
		System.out.println("user page => " + JSON.toJSONString(userPage.getContent()));
	}


	@Test
	public void test_003(){
		System.out.println(JSON.toJSONString(userService.findByUserId(1L)));
	}

	@Test
	public void test_004(){
		System.out.println(JSON.toJSONString(userService.findByUserPhone("18817394122")));
	}

	@Test
	public void test_005(){
		userService.deleteByUserId(1L);
	}


	@Test
	public void testCC(){
		SearchRequestBuilder responsebuilder = client.prepareSearch("yttest").setTypes("userb");
		SearchResponse myresponse= responsebuilder.setQuery(QueryBuilders.termsQuery("age", "18", "8", "99")).setExplain(true).execute().actionGet();
		SearchHits hits = myresponse.getHits();
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());}


	}

}
