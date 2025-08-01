package br.com.jvictorvale.repository;

import br.com.jvictorvale.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
