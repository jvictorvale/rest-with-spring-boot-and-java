package br.com.jvictorvale.services;

import br.com.jvictorvale.controllers.BookController;
import br.com.jvictorvale.data.dto.BookDTO;
import static br.com.jvictorvale.mapper.ObjectMapper.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.jvictorvale.exception.RequiredObjectIsNullException;
import br.com.jvictorvale.exception.ResourceNotFoundException;
import br.com.jvictorvale.model.Book;
import br.com.jvictorvale.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    BookRepository repository;

    public List<BookDTO> findAll() {
        logger.info("Finding all books");

        var books = parseListObjects(repository.findAll(), BookDTO.class);
        books.forEach(this::addHateoasLinks);
        return books;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one book");

        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No book found with id: " + id));

        var dto = parseObject(book, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO bookDTO) {

        if(bookDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating new book");

        var book = parseObject(bookDTO, Book.class);

        var dto = parseObject(repository.save(book), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO bookDTO) {
        if(bookDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating book");

        Book book = repository.findById(bookDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No book found with id: " + bookDTO.getId()));

        book.setTitle(bookDTO.getTitle());
        book.setLaunchDate(bookDTO.getLaunchDate());
        book.setPrice(bookDTO.getPrice());
        book.setAuthor(bookDTO.getAuthor());

        var dto = parseObject(repository.save(book), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting book");

        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No book found with id: " + id));

        repository.delete(book);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
