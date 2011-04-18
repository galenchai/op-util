package com.taobao.top.core;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.taobao.top.core.framework.TopPipeInput;
import com.taobao.top.timwrapper.manager.TadgetManager;

/**
 * 
 * @author haishi
 * 
 */
public class ApiConfigReaderCombineMultiChoiseTest {
	ApiConfigReader reader = new ApiConfigReader();

	private static Api api = null;

	private static String getXml(java.lang.String name) {
		return ApiConfigReaderCombineTest.class.getResource(name).getFile();
	}

	@BeforeClass
	public static void beforeClass() throws ApiConfigException, IOException {
		ApplicationContext springContext = new ClassPathXmlApplicationContext(
				"/com/taobao/top/core/spring-api_ApiConfigReaderTest.xml");
		ApiConfigReader reader = (ApiConfigReader) springContext
				.getBean("apiConfigReaderForTest");
		FileInputStream fis = new FileInputStream(
				getXml("/com/taobao/top/core/ApiConfigReaderTest_combine_multichoise.api.xml"));
		// Check api
		api = reader.xml2Api(fis);
		fis.close();
	}

	@Test
	public void testCombineCheckMissingArgument()
			throws ApiConfigException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse resp = new MockHttpServletResponse();
		TadgetManager tad = new TadgetManager();
		TopPipeInput pipeInput = new TopPipeInput(req,resp,tad);
		pipeInput.setVersion("2.0");
		ErrorCode checked = api.checkCombine(pipeInput);
		assertEquals(ErrorCode.MISSING_REQUIRED_ARGUMENTS, checked);
	}
	
	@Test
	public void testCombineCheckMissingArgumentV1()
			throws ApiConfigException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse resp = new MockHttpServletResponse();
		TadgetManager tad = new TadgetManager();
		TopPipeInput pipeInput = new TopPipeInput(req,resp,tad);
		req.addParameter("v", "1.0");
		ErrorCode checked = api.checkCombine(pipeInput);
		assertEquals(ErrorCode.MISSING_REQUIRED_ARGUMENTS, checked);
	}

	@Test
	public void testCombineCheckSuccess() throws ApiConfigException,
			IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse resp = new MockHttpServletResponse();
		TadgetManager tad = new TadgetManager();
		TopPipeInput input = new TopPipeInput(req,resp,tad);
		
		req.addParameter("v", "2.0");
		req.addParameter("trade_id", "123");
		req.addParameter("a", "anyvalue");
		req.addParameter("cid", "anyvalue");
		ErrorCode checked = api.checkCombine(input);
		assertEquals(null, checked);
		List<ApiApplicationParameter> combineParams = api.getApplicationCombineParams();
		assertEquals(6, combineParams.size());
	}
	
	@Test
	public void testCombineCheckInvalidValue() throws ApiConfigException,
			IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse resp = new MockHttpServletResponse();
		TadgetManager tad = new TadgetManager();
		TopPipeInput input = new TopPipeInput(req,resp,tad);
		
		req.addParameter("v", "1.0");
		req.addParameter("trade_id", "123");
		ErrorCode checked = api.checkCombine(input);
		assertEquals(ErrorCode.MISSING_REQUIRED_ARGUMENTS, checked);
	}
}
