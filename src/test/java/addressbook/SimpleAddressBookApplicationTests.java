package addressbook;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Integration test for SimpleAddressBookApplication. It is depending on H2 in memory db. The initial data is in
 * `resources/import.sql`
 *
 *
 */
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = SimpleAddressBookApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SimpleAddressBookApplicationTests {


	@Autowired
	private MockMvc mvc;

	/**
	 * Test 'api/addressbook' with no parameter. Expect 2  addressbook to be returned
	 *
	 * @throws Exception
	 */
	@Test
	void testGetAddressBook() throws Exception {
		mvc.perform(get("/api/addressbook")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)));
	}

	/**
	 * Test 'api/addressbook/{id}'
	 *
	 * @throws Exception
	 */
	@Test
	void testGetAddressBookById() throws Exception {
		mvc.perform(get("/api/addressbook/76f36362-92d9-4676-8384-abb62f54ce03")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("Test1")));
	}


	/**
	 *
	 * Test create a addressbook. The subsequent get Addressbook by Id request should return created Addressbook
	 *
	 * @throws Exception
	 */
	@Test
	void testCreateAddressBook () throws Exception {
		MockHttpServletRequestBuilder mockRequest = post("/api/addressbook")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"testAddressBookCreation\"}");


		String newAddressBookId = JsonPath.read(mvc.perform(mockRequest).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), "$");

		MockHttpServletRequestBuilder confirmRequest = get("/api/addressbook/" + newAddressBookId);
		mvc.perform(confirmRequest).andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("testAddressBookCreation")));

	}


	/**
	 * Test Update of a AddressBook. The second get request should get result match give Json Payload
	 *
	 * @throws Exception
	 */
	@Test
	public void testUpdateAddressBook () throws Exception {
		MockHttpServletRequestBuilder mockRequest = put("/api/addressbook/f18856f9-70c8-41e4-9cd7-2fe5bbf40e34")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"testAddressBookUpdate\"}");

		mvc.perform(mockRequest).andDo(print()).andExpect(status().isOk());

		MockHttpServletRequestBuilder confirmRequest = get("/api/addressbook/f18856f9-70c8-41e4-9cd7-2fe5bbf40e34");
		mvc.perform(confirmRequest)
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("testAddressBookUpdate")));
	}


	/**
	 * Test 'api/addressbook/contact/{id}'
	 *
	 * @throws Exception
	 */
	@Test
	void testGetContactById() throws Exception {
		mvc.perform(get("/api/addressbook/contact/cbdd5f68-b59a-40de-bcb9-3c7e96bcbb88")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is("John")));
	}



	/**
	 * Test 'api/addressbook/contact'. Except 3 result as 1 among 4 initial data is duplicated into a different
	 * addressbook.
	 *
	 * @throws Exception
	 */
	@Test
	void testGetAllContactUnique() throws Exception {
		mvc.perform(get("/api/addressbook/contact")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3)));
	}


	/**
	 * Test 'api/addressbook/{id}/contact' . Expect 2 contact returned
	 *
	 * @throws Exception
	 */
	@Test
	void testGetAllContactInAddressBook() throws Exception {
		mvc.perform(get("/api/addressbook/76f36362-92d9-4676-8384-abb62f54ce03/contact")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)));
	}

	/**
	 *
	 * Test create a contact '/api/addressbook/{id}/contact`. The subsequent get Addressbook by Id request should return created Addressbook
	 *
	 * @throws Exception
	 */
	@Test
	void testCreateContact () throws Exception {
		MockHttpServletRequestBuilder mockRequest = post("/api/addressbook/76f36362-92d9-4676-8384-abb62f54ce03/contact")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"firstName\": \"TestMe\",\n" +
						"    \"surName\": \"TestGo\",\n" +
						"    \"email\": \"tst3@test.com\",\n" +
						"    \"phoneNumber\": \"12345667\",\n" +
						"    \"state\": \"VIC\",\n" +
						"    \"country\": \"Australia\"\n" +
						"  }");


		String newContactId = JsonPath.read(mvc.perform(mockRequest).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), "$");

		MockHttpServletRequestBuilder confirmRequest = get("/api/addressbook/contact/" + newContactId);
		mvc.perform(confirmRequest).andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is("TestMe")));
	}

	/**
	 *
	 * Test update a contact 'api/addressbook/{addressBookId}/contact/{id}`.
	 *
	 * Two subsequent get request used to confirm the update
	 *  1. Get contact to confirm the attribute change
	 *  2. list target addressbook, should has 3 contact
	 *
	 * @throws Exception
	 */
	@Test
	void testUpdateContact () throws Exception {
		MockHttpServletRequestBuilder mockRequest = put("/api/addressbook/f18856f9-70c8-41e4-9cd7-2fe5bbf40e34/contact/cbdd5f68-b59a-40de-bcb9-3c7e96bcbb88")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" +
						"    \"firstName\": \"TestMe\",\n" +
						"    \"surName\": \"TestGo\",\n" +
						"    \"email\": \"tst5@test.com\",\n" +
						"    \"phoneNumber\": \"12345667\",\n" +
						"    \"state\": \"VIC\",\n" +
						"    \"country\": \"Australia\"\n" +
						"  }");


		mvc.perform(mockRequest).andDo(print()).andExpect(status().isOk());

		mvc.perform(get("/api/addressbook/f18856f9-70c8-41e4-9cd7-2fe5bbf40e34/contact")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3)));

		mvc.perform(get("/api/addressbook/contact/cbdd5f68-b59a-40de-bcb9-3c7e96bcbb88")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is("TestMe")));

	}


	/**
	 * Test Delete a AddressBook DELETE /api/addressbook/{id}. The second get request should return empty result with given Id
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteAddressBook () throws Exception {
		MockHttpServletRequestBuilder mockRequest = delete("/api/addressbook/76f36362-92d9-4676-8384-abb62f54ce03");
		mvc.perform(mockRequest).andDo(print()).andExpect(status().isOk());

		MockHttpServletRequestBuilder confirmRequest = get("/api/addressbook/76f36362-92d9-4676-8384-abb62f54ce03");
		mvc.perform(confirmRequest).andExpect(content().string(""));
	}


	/**
	 * Test Delete a AddressBook DELETE /api/addressbook/contact/{id}.
	 *
	 * Two confirming get request will be fired.
	 *
	 * 1. The get contact by Id request will return empty.
	 * 2. The get all contact under address book will return only 1 element
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteContact () throws Exception {
		MockHttpServletRequestBuilder mockRequest = delete("/api/addressbook/contact/cbdd5f68-b59a-40de-bcb9-3c7e96bcbb88");
		mvc.perform(mockRequest).andDo(print()).andExpect(status().isOk());

		MockHttpServletRequestBuilder confirmRequest = get("/api/addressbook/contact/cbdd5f68-b59a-40de-bcb9-3c7e96bcbb88");
		mvc.perform(confirmRequest).andExpect(content().string(""));

		mvc.perform(get("/api/addressbook/76f36362-92d9-4676-8384-abb62f54ce03/contact")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)));
	}
}
