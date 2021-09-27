package amaralus.apps.businesappdemo.rest;

import amaralus.apps.businesappdemo.services.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Validated
public abstract class CrudController<E> {

    protected final CrudService<E, String> crudService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody @Valid E entity) {
        crudService.save(entity);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid E entity) {
        crudService.save(entity);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public E get(@PathVariable @NotBlank String id) {
        var entity = crudService.getById(id);
        if (entity == null)
            throw new ResponseStatusException(NOT_FOUND);
        return entity;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotBlank String id) {
        crudService.delete(id);
    }
}
