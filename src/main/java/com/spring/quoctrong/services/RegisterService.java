package com.spring.quoctrong.services;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.spring.quoctrong.models.Role;
import com.spring.quoctrong.models.SecureToken;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.repositories.RoleRepository;
import com.spring.quoctrong.repositories.SecureTokenRepository;
import com.spring.quoctrong.repositories.UserRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class RegisterService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	SecureTokenRepository secureTokenRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	private SecureTokenService secureTokenService;
	public User save(User user) throws Exception{
	
			if (userRepository.existsByEmail(user.getEmail())) {
				throw new Exception("Email đã được sử dụng!");
			}	
		Role role = roleRepository.findByName("USER").orElseThrow(() -> new Exception("Không tìm thấy role!"));
		user.addRole(role);
		user.setEnabled(false);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
		SecureToken secureToken = secureTokenService.createSecureToken();
		secureToken.setUser(user);
		secureTokenRepository.save(secureToken);
		String subject = "[SHOP] - Xác nhận email của bạn";
		String senderName = "SHOP";
		String verifyURL = siteURL + "/verify?code=" + secureToken.getToken();
		ClassPathResource resource = new ClassPathResource("/static/img/others/handshake.png");		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
		helper.setFrom("noreply@shop.com", senderName);
		helper.setTo(user.getEmail());
		helper.setSubject(subject);
		helper.addInline("shakeHand", resource);
		String html = "<div style=\"\r\n"
				+ "        background: linear-gradient(\r\n"
				+ "            to bottom,\r\n"
				+ "            #FFD333 0%,\r\n"
				+ "            #FFD333 50%,\r\n"
				+ "            white 50%,\r\n"
				+ "            white 100%\r\n"
				+ "        );\r\n"
				+ "        width: 100%;\r\n"
				+ "        height: 100vh;\r\n"
				+ "        display: flex;\r\n"
				+ "        justify-content: center;\r\n"
				+ "        padding: 5%;\r\n"
				+ "    \">\r\n"
				+ "        <div style=\"\r\n"
				+ "            width: 80%;\r\n"
				+ "            text-align: center;\r\n"
				+ "            background: white;\r\n"
				+ "            box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;\r\n"
				+ "        \"\r\n"
				+ ">\r\n"
				+ "            <h2 style=\"\r\n"
				+ "                font-weight: 600;\r\n"
				+ "                font-size: 24px;\r\n"
				+ "                margin-top: 20px;\r\n"
				+ "            \" >Xin chào, " + user.getFullname() + " </h2>\r\n"
				+ "            <img style=\"\r\n"
				+ "                margin: auto;\r\n"
				+ "                width: 260px;\r\n"
				+ "            \" src=\"cid:shakeHand\">\r\n"
				+ "            <p>Để kích hoạt tài khoản, hãy nhấn vào đường link bên dưới.</p>\r\n"
				+ "                <a style=\"\r\n"
				+ "                    outline: none;\r\n"
				+ "                    margin-top: 20px;\r\n"
				+ "                    padding: 8px 16px;\r\n"
				+ "                    background-color: #FFD333;\r\n"
				+ "                    color: white;\r\n"
				+ "                    border-radius: 8px;\r\n"
				+ "                    font-weight: 700;\r\n"
				+ "                    border: none;\r\n"
				+ "                    text-decoration: none;\r\n"
				+ "                    display: inline-block;\r\n"
				+ "                    box-shadow: rgba(0, 0, 0, 0.1) 0px 3px 8px;\r\n"
				+ "                \" href="+ verifyURL +">\r\n"
				+ "                    Kích hoạt tài khoản\r\n"
				+ "                </a>\r\n"
				+ "        </div>\r\n"
				+ "    </div>";
		helper.setText(html, true);		
		
		mailSender.send(message);
		
	}
	
	public boolean verify(String code) throws Exception {
		SecureToken secureToken = secureTokenRepository.findByToken(code).orElseThrow(() -> new Exception("Token không hợp lệ"));
		if (secureToken.isExpired()) {
			secureTokenRepository.delete(secureToken);
			throw new Exception("Token đã hết hạn!");
		};
		User user = secureToken.getUser();
		if (user == null || user.isEnabled()) {
			return false;
		} else {		
			user.setEnabled(true);
			userRepository.save(user);
		}
		secureTokenRepository.delete(secureToken);
		return true;
	}
	
	
}
