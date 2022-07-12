package com.nitendo.backend;

import com.nitendo.backend.entity.Address;
import com.nitendo.backend.entity.Social;
import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.service.AddressService;
import com.nitendo.backend.service.SocialService;
import com.nitendo.backend.service.UserService;
import com.nitendo.backend.util.SecurityUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTests {
	@Autowired
	private UserService userService;

	@Autowired
	private SocialService socialService;

	@Autowired
	private AddressService addressService;


	@Order(1)
	@Test
	void testCreate() throws BaseException {
		String token = SecurityUtil.generateToken();
		User user = userService.create(
				TestCreateData.email,
				TestCreateData.password,
				TestCreateData.name,
				token,
				new Date()
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
	void testCreateSocial() throws BaseException {
		Optional<User> opt = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(opt.isPresent());
		User user = opt.get();
		Social social = user.getSocial();
		Assertions.assertNull(social);	// ยังไม่ create ต้องมีค่าเป็น Null

		social = socialService.create(user,
				SocialTestCreateData.facebook,
				SocialTestCreateData.line,
				SocialTestCreateData.instagram,
				SocialTestCreateData.tiktok
		);
		Assertions.assertNotNull(social);	// create แล้วต้องไม่เป็น Null
		Assertions.assertEquals(SocialTestCreateData.facebook, social.getFacebook());

	}

	@Order(4)
	@Test
	void testCreateAddress() throws BaseException {
		Optional<User> opt = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(opt.isPresent());
		User user = opt.get();
		List<Address> addresses = user.getAddresses();
		Assertions.assertTrue(addresses.isEmpty());	// List ยังไม่ create ต้องมีค่าเป็น empty

		createAddress(user, AddressTestCreateData.line1, AddressTestCreateData.line2, AddressTestCreateData.zipcode);
		createAddress(user, AddressTestCreateData2.line1, AddressTestCreateData2.line2, AddressTestCreateData2.zipcode);
	}

	private void createAddress(User user, String line1, String line2, String zipcode) {
		Address address = addressService.create(
				user,
				line1,
				line2,
				zipcode
		);

		Assertions.assertNotNull(address);	// create แล้วต้องไม่เป็น Null
		Assertions.assertEquals(line1, address.getLine1());
		Assertions.assertEquals(line2, address.getLine2());
		Assertions.assertEquals(zipcode, address.getZipcode());
	}

	@Order(5)
	@Test
	void testDelete() {
		// Find email
		Optional<User> opt = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(opt.isPresent());

		User user = opt.get();

		// check social before delete
		Social social = user.getSocial();
		Assertions.assertNotNull(social);	// ต้องมี Social เพราะผ่าน create (test2) มาแล้ว
		Assertions.assertEquals(SocialTestCreateData.facebook, social.getFacebook()); // ข้อมูลที่มีต้องเป็นข้อมูลที่เคย create ไว้

		// check address before delete
		List<Address> addresses = user.getAddresses();
		Assertions.assertFalse(addresses.isEmpty());	// ต้องไม่ empty
		Assertions.assertEquals(2, addresses.size());	// check address มี 2 ที่อยู่ตามที่ create

		// Send data to delete
		userService.deleteById(user.getId());

		// Test extract data again
		Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
		Assertions.assertTrue(optDelete.isEmpty());
	}

	interface TestCreateData {
		String email = "nat@test.com";
		String password = "1234mm5678";
		String name = "Natthapon Sirichai";
	}

	interface TestUpdateData {
		String name = "New Changname";
	}

	interface SocialTestCreateData {
		String facebook = "iamfacebook";
		String line = " iamline";
		String instagram = "iaminstagram";
		String tiktok = "iamtiktok";
	}

	interface AddressTestCreateData {
		String line1 = "123/45";
		String line2 = "Muang";
		String zipcode = "10500";
	}

	interface AddressTestCreateData2 {
		String line1 = "88/99";
		String line2 = "Dusit";
		String zipcode = "10300";
	}
}
