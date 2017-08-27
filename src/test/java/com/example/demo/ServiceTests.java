package com.example.demo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.ResultObj;
import com.example.demo.domain.SpiderRule;
import com.example.demo.service.ExtractService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTests {

	@Autowired
	private ExtractService extractService;
	
	@Test
	public void test1GetTopMovie(){
		extractService.getTopMovie(2);
	}
	
	public void printf(List<ResultObj> datas) {
		for (ResultObj data : datas) {
			System.out.println(data.getLinkText());
			System.out.println(data.getLinkHref());
			System.out.println("***********************************");
		}
		System.out.println("**************结束*************");
	}
}
