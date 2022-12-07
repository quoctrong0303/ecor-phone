package com.spring.quoctrong.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.quoctrong.dto.HttpError;

@Controller
public class HandleErrorController implements ErrorController{

	
	@RequestMapping("/error")
	  public String handleError(HttpServletRequest request, Model model) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    if (status != null) {
	      Integer statusCode = Integer.valueOf(status.toString());
	      if (statusCode == HttpStatus.NOT_FOUND.value()) {
	    	model.addAttribute("error", new HttpError(404, "Not Found", "Trang không tồn tại!"));
	        return "error/error";
	      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	    	  model.addAttribute("error", new HttpError(500, "Internal Server Error", "Có lỗi xãy ra, vui lòng thử lại!"));
	        return "error/error";
	      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
	    	  model.addAttribute("error", new HttpError(403, "Forbidden", "Bạn không có quyền truy cập vào trang này!"));
	    	  return "error/error";
	      }
	    }
	    return "error/error";
	  }
}
