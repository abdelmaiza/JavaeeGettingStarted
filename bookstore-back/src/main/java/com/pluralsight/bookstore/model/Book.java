package com.pluralsight.bookstore.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by b010nsm on 12/02/2019.
 */
@Entity
@ApiModel(description = "Book resource representation")
public class Book {

    @Id @GeneratedValue
    @ApiModelProperty("Identifier")
    private Long id;

    @Column(length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty("ISBN number")
    private String isbn;

    @Column(length = 200)
    @NotNull
    @Size(min = 1 , max = 200)
    @ApiModelProperty("Title of the book")
    private String title;

    @Column(name = "image_url")
    @ApiModelProperty("URL of the image cover")
    private String imgUrl;

    @Enumerated
    @ApiModelProperty( value = "Language in which the book has been written")
    private Language language;

    @Column(name = "unit_cost")
    @Min(1)
    @ApiModelProperty("Unit cost")
    private Float unitCost;

    @Column(name = "nb_of_pages")
    @ApiModelProperty("Number of pages")
    private Integer nbOfPages;

    @Column(name = "publication_date")
    @Temporal(TemporalType.DATE)
    @Past
    private Date publicationDate;

    @Column(length = 10000)
    @Size(min = 1 , max = 10000)
    @ApiModelProperty("Summary describing the book")
    private String description;

    public Book() {
    }

    public Book(String isbn, String a_titile, float unit_cost, int nb_of_pages, Language language, Date date,String imgUrl, String description) {
        this.isbn = isbn;
        this.title = a_titile;
        this.unitCost = unit_cost;
        this.nbOfPages = nb_of_pages;
        this.language = language;
        this.publicationDate = date;
        this.imgUrl = imgUrl;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Float unitCost) {
        this.unitCost = unitCost;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getNbOfPages() {
        return nbOfPages;
    }

    public void setNbOfPages(Integer nbOfPages) {
        this.nbOfPages = nbOfPages;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override public String toString() {
        return "Book{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", unitCost=" + unitCost
            + ", isbn='" + isbn + '\'' + ", publicationDate=" + publicationDate + ", nbOfPages=" + nbOfPages + ", imgUrl='" + imgUrl + '\''
            + ", language=" + language + '}';
    }
}
