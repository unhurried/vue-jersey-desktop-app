package vjda.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import vjda.repository.entity.ToDo;

/** Spring Data JPA repository for ToDoEntity */
public interface ToDoRepository extends PagingAndSortingRepository<ToDo, Long> {}