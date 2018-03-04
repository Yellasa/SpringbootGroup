package com.frank.chapter411;

import com.alibaba.fastjson.JSON;
import com.frank.chapter411.domain.User;
import com.frank.chapter411.service.UserService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
public class Chapter411ApplicationTests {

	@Autowired
	private UserService userService;

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
	public  void testSave(){
		userService.save(new User(1L,18,"sanxiong","18817394122"));
		userService.save(new User(2L,28,"sixiong","18817394122"));
		userService.save(new User(3L,22,"sanmeng","18817394122"));
		userService.save(new User(4L,38,"simeng","18817394122"));
		userService.save(new User(5L,48,"x2asdda","18817394122"));
	}

	@Test
	public void testUserB_002(){
		Iterator<User> users = userService.findByUserAge(28).iterator();
		List<User> userList = new ArrayList<>();
		while (users.hasNext()){
			userList.add(users.next());
		}
		System.out.println("userList => " + JSON.toJSONString(userList));
	}

	@Test
	public void testUserB_003(){
		Iterator<User> users = userService.searchUsers(10,1,"si").iterator();
		List<User> userList = new ArrayList<>();
		while (users.hasNext()){
			userList.add(users.next());
		}
		System.out.println("userList => " + JSON.toJSONString(userList));
	}


	@Test
	public void test_003(){
		System.out.println(JSON.toJSONString(userService.findByUserId(3L)));
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
	public void test675(){
		// 构建查询条件
		//QueryBuilder queryBuilder = QueryBuilders.queryString(title); // Pekkle: spring boot 1.4.1 集成elastic search后看不到此方法，用如下方法代替。

		String query_str = "{\"query\":{\"query_string\":{\"age:>25\"}}}";
		QueryBuilder queryBuilder = QueryBuilders.simpleQueryStringQuery(query_str);
		Iterable<User> users = this.userService.search(queryBuilder);
		users.forEach(user->{
			System.out.println(user);
		});


	}
}
