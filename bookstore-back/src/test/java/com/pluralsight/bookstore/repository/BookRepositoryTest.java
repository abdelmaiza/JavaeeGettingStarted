package com.pluralsight.bookstore.repository;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@RunWith(Arquillian.class)
public class BookRepositoryTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static Long bookId;

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private BookRepository bookRepository;

    @Inject
    private IsbnGenerator isbnGenerator;

    @Inject
    private TextUtil textUtil;

    // ======================================
    // =             Deployment             =
    // ======================================

    @Deployment
    public static Archive<?> createDeploymentPackage() {

        return ShrinkWrap.create(JavaArchive.class)
            .addClass(Book.class)
            .addClass(Language.class)
            .addClass(BookRepository.class)
            .addClass(NumberGenerator.class)
            .addClass(IsbnGenerator.class)
            .addClass(TextUtil.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldBeDeployed() {
        assertNotNull(bookRepository);
        assertNotNull(isbnGenerator);
        assertNotNull(textUtil);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoBook() {
        // Count all
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        // Find all
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test
    @InSequence(3)
    public void shouldCreateABook() {
        // Creates a book
        Book book = new Book("isbn", "a   title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        book = bookRepository.create(book);
        // Checks the created book
        assertNotNull(book);
        assertNotNull(book.getId());
        bookId = book.getId();
    }

    @Test
    @InSequence(4)
    public void shouldFindTheCreatedBook() {
        // Finds the book
        Book bookFound = bookRepository.find(bookId);
        // Checks the found book
        assertNotNull(bookFound.getId());
        assertTrue(bookFound.getIsbn().startsWith("13-84356-"));
        assertEquals("a title", bookFound.getTitle());
    }

    @Test
    @InSequence(5)
    public void shouldGetOneBook() {
        // Count all
        assertEquals(Long.valueOf(1), bookRepository.countAll());
        // Find all
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    @InSequence(6)
    public void shouldDeleteTheCreatedBook() {
        // Deletes the book
        bookRepository.delete(bookId);
        // Checks the deleted book
        Book bookDeleted = bookRepository.find(bookId);
        assertNull(bookDeleted);
    }

    @Test
    @InSequence(7)
    public void shouldGetNoMoreBook() {
        // Count all
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        // Find all
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test(expected = Exception.class)
    @InSequence(10)
    public void shouldFailCreatingANullBook() {
        bookRepository.create(null);
    }

    @Test(expected = Exception.class)
    @InSequence(11)
    public void shouldFailCreatingABookWithNullTitle() {
        bookRepository.create(new Book("isbn", null, 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description"));
    }

    @Test(expected = Exception.class)
    @InSequence(12)
    public void shouldFailCreatingABookWithLowUnitCostTitle() {
        bookRepository.create(new Book("isbn", "title", 0F, 123, Language.ENGLISH, new Date(), "imageURL", "description"));
    }

    @Test
    @InSequence(13)
    public void shouldNotFailCreatingABookWithNullISBN() {
        Book bookFound = bookRepository.create(new Book(null, "title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description"));
        assertTrue(bookFound.getIsbn().startsWith("13-84356-"));
    }

    @Test(expected = Exception.class)
    @InSequence(14)
    public void shouldFailInvokingFindByIdWithNull() {
        bookRepository.find(null);
    }

    @Test
    @InSequence(15)
    public void shouldNotFindUnknownId() {
        assertNull(bookRepository.find(99999L));
    }

    @Test(expected = Exception.class)
    @InSequence(16)
    public void shouldFailInvokingDeleteByIdWithNull() {
        bookRepository.delete(null);
    }
    @Test(expected = Exception.class)
    @InSequence(17)
    public void shouldNotDeleteUnknownId() {
        bookRepository.delete(99999L);
    }
}
