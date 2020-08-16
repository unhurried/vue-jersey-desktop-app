package backend.repository

import backend.repository.entity.ToDo
import org.springframework.data.repository.PagingAndSortingRepository

/** Spring Data JPA repository for ToDoEntity  */
interface ToDoRepository : PagingAndSortingRepository<ToDo, Long>