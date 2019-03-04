package com.pluralsight.bookstore.rest;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.repository.BookRepository;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.*;


@RunWith(Arquillian.class)
@RunAsClient
public class BookEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static String bookId;
    private Response response;

    // ======================================
    // =             Deployment             =
    // ======================================

    @Deployment(testable = false)
    public static Archive<?> createDeploymentPackage() {

        return ShrinkWrap.create(WebArchive.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(BookRepository.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addClass(TextUtil.class)
                .addClass(BookEndpoint.class)
                .addClass(JAXRSConfiguration.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(2)
    public void shouldGetNoBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Count all
        response = webTarget.path("count").request().get();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        // Find all
        response = webTarget.request(APPLICATION_JSON).get();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(3)
    public void shouldCreateABook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Creates a book
        Book book = new Book("isbn", "a   title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        response = webTarget.request(APPLICATION_JSON).post(Entity.entity(book, APPLICATION_JSON));
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        // Checks the created book
        String location = response.getHeaderString("location");
        assertNotNull(location);
        bookId = location.substring(location.lastIndexOf("/") + 1);
    }

    @Test
    @InSequence(4)
    public void shouldFindTheCreatedBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Finds the book
        response = webTarget.path(bookId).request(APPLICATION_JSON).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        // Checks the found book
        Book bookFound = response.readEntity(Book.class);
        assertNotNull(bookFound.getId());
        assertTrue(bookFound.getIsbn().startsWith("13-84356-"));
        assertEquals("a title", bookFound.getTitle());
    }

    @Test
    @InSequence(5)
    public void shouldGetOneBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Count all
        response = webTarget.path("count").request().get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(Long.valueOf(1), response.readEntity(Long.class));
        // Find all
        response = webTarget.request(APPLICATION_JSON).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(1, response.readEntity(List.class).size());
    }

    @Test
    @InSequence(6)
    public void shouldDeleteTheCreatedBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Deletes the book
        response = webTarget.path(bookId).request(APPLICATION_JSON).delete();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        // Checks the deleted book
        Response checkResponse = webTarget.path(bookId).request(APPLICATION_JSON).get();
        assertEquals(NOT_FOUND.getStatusCode(), checkResponse.getStatus());
    }

    @Test
    @InSequence(7)
    public void shouldGetNoMoreBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Count all
        response = webTarget.path("count").request().get();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        // Find all
        response = webTarget.request(APPLICATION_JSON).get();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(10)
    public void shouldFailCreatingANullBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.request(APPLICATION_JSON).post(null);
        assertEquals(UNSUPPORTED_MEDIA_TYPE.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(11)
    public void shouldFailCreatingABookWithNullTitle(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        Book book = new Book("isbn", null, 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        response = webTarget.request(APPLICATION_JSON).post(Entity.entity(book, APPLICATION_JSON));
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(12)
    public void shouldFailCreatingABookWithLowUnitCostTitle(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        Book book = new Book("isbn", "title", 0F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        response = webTarget.request(APPLICATION_JSON).post(Entity.entity(book, APPLICATION_JSON));
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(13)
    public void shouldNotFailCreatingABookWithNullISBN(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        Book book = new Book(null, "title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        response = webTarget.request(APPLICATION_JSON).post(Entity.entity(book, APPLICATION_JSON));
        assertEquals(CREATED.getStatusCode(), response.getStatus());
    }

    @Test(expected = Exception.class)
    @InSequence(14)
    public void shouldFailInvokingFindByIdWithNull(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.path(null).request(APPLICATION_JSON).get();
    }

    @Test
    @InSequence(15)
    public void shouldNotFindUnknownId(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.path("999").request(APPLICATION_JSON).get();
        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test(expected = Exception.class)
    @InSequence(16)
    public void shouldFailInvokingDeleteByIdWithNull(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.path(null).request(APPLICATION_JSON).delete();
    }

    @Test
    @InSequence(17)
    public void shouldFailInvokingFindByIdWithZero(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.path("0").request(APPLICATION_JSON).get();
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(18)
    public void shouldFailInvokingDeleteByIdWithZero(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.path("0").request(APPLICATION_JSON).delete();
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(19)
    public void shouldNotDeleteUnknownId(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        response = webTarget.path("999").request(APPLICATION_JSON).delete();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}

