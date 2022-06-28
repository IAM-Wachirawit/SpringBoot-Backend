package com.nitendo.backend;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTests {
	@Autowired
	private UserService userService;
	interface TestCreateData {
		String email = "nat@test.com";
		String password = "1234mm5678";
		String name = "Natthapon Sirichai";
	}


	interface TestUpdateData {
		String name = "New Changname";
	}

	@Order(1)
	@Test
	void testCreate() throws BaseException {
		User user = userService.create(
				TestCreateData.email,
				TestCreateData.password,
				TestCreateData.name
		);

		// Check not null
		Assertions.assertNotNull(user);
		Assertions.assertNotNull(user.getId());

		// Check equals
			Assertions.assertEquals(TestCreateData.email, user.getEmail());
		boolean isMatched = userService.matchPassword(TestCreateData.password, user.getPassword());
		Assertions.assertTrue(isMatched);
		Assertions.assertEquals(TestCreateData.name, user.getName());

	}

	@Order(2)
	@Test
	void testUpdate() throws BaseException {
		Optional<User> opt = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(opt.isPresent());
		User user = opt.get();
		User updatedUser = userService.updateName(user.getId(), TestUpdateData.name);

		Assertions.assertNotNull(updatedUser);
		Assertions.assertEquals(TestUpdateData.name, updatedUser.getName());
	}

	@Order(3)
	@Test
	void testDelete() {
		// Find email
		Optional<User> opt = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(opt.isPresent());
		// Send data to detele
		User user = opt.get();
		userService.deleteById(user.getId());
		// Test extract data again
		Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(optDelete.isEmpty());
	}
}
