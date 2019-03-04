package com.pluralsight.bookstore.repository;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.TextUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public class BookRepository {

    @PersistenceContext(unitName = "bookStorePU")
    private EntityManager entityManager;

    @Inject TextUtil textUtil;

    @Inject IsbnGenerator isbnGenerator;

    public Book find(@NotNull  Long id){
        return entityManager.find(Book.class, id);
    }

    public List<Book> findAll(){
        TypedQuery<Book> bookTypedQuery = entityManager.createQuery("SELECT b from Book b order by b.title desc ", Book.class );
        return bookTypedQuery.getResultList();
    }

    public Long countAll(){
        TypedQuery<Long> longTypedQuery = entityManager.createQuery("select count (b) from Book b" , Long.class);
        return longTypedQuery.getSingleResult();
    }

    @Transactional(REQUIRED)
    public Book create(@NotNull Book book){
        book.setTitle(textUtil.sanitize(book.getTitle()));
        book.setIsbn(isbnGenerator.generateNumber());
        entityManager.persist(book);
        return book;
    }
    @Transactional(REQUIRED)
    public void delete(@NotNull Long id){
        entityManager.remove(entityManager.getReference(Book.class, id));
    }


}
