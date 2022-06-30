package com.nitendo.backend;

import com.nitendo.backend.business.EmailBusiness;
import com.nitendo.backend.entity.Address;
import com.nitendo.backend.entity.Social;
import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.service.AddressService;
import com.nitendo.backend.service.SocialService;
import com.nitendo.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmailBusinessTests {
	@Autowired
	private EmailBusiness emailBusiness;

	@Order(1)
	@Test
	void testSendActivateEmail() throws BaseException {
		emailBusiness.sendActivateUserEmail(
				TestData.email,
				TestData.name,
				TestData.token
		);
	}

	interface TestData {
		String email = "w.chottiniphat@gmail.com";	// Email คนรับ
		String name = "Wachirawit Chot";			// ชื่อคนรับ
		String token	= "ranDomdaTa#123321";
	}
}
