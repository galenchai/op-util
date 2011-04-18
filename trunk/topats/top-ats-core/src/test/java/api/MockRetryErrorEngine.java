package api;

import com.taobao.top.ats.engine.ApiEngine;
import com.taobao.top.ats.engine.ApiRequest;
import com.taobao.top.ats.engine.ApiResponse;

/**
 * 
 * @author moling
 * @since 1.0, 2010-8-25
 */
public class MockRetryErrorEngine implements ApiEngine {

	public ApiResponse invokeApi(ApiRequest request) {
		ApiResponse response = new ApiResponse();
		response.setErrCode("isv.other-errors");
		response.setErrMsg("error,error");
		return response;
	}

}
