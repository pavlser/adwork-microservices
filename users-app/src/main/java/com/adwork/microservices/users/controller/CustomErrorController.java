package com.adwork.microservices.users.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CustomErrorController implements ErrorController {

	private ErrorAttributes errorAttributes = null;

	@Autowired
	public CustomErrorController(ErrorAttributes errorAttributes) {
		Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping("/error")
	@ResponseBody
	public String handleError(HttpServletRequest req) {
		return "Error: " + errorAttributes.getError((WebRequest)req);
	}

	/*
	 * @RequestMapping
	 * 
	 * @ResponseBody public Map<String, Object> error(HttpServletRequest aRequest){
	 * Map<String, Object> body =
	 * getErrorAttributes(aRequest,getTraceParameter(aRequest)); String trace =
	 * (String) body.get("trace"); if(trace != null){ String[] lines =
	 * trace.split("\n\t"); body.put("trace", lines); } return body; }
	 */

	/*private boolean getTraceParameter(HttpServletRequest request) {
		String parameter = request.getParameter("trace");
		if (parameter == null) {
			return false;
		}
		return !"false".equals(parameter.toLowerCase());
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
		return errorAttributes.getErrorAttributes((WebRequest) requestAttributes, includeStackTrace);
	}*/
}
