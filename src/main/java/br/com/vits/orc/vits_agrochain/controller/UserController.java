package br.com.vits.orc.vits_agrochain.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.vits.orc.vits_agrochain.model.User;
import br.com.vits.orc.vits_agrochain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema Verdantis")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários com paginação", description = "Retorna lista paginada de usuários com links HATEOAS")
    public PagedModel<EntityModel<User>> getAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            PagedResourcesAssembler<User> assembler) {
        log.info("Listando usuários paginados - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        var page = userService.listAllPaginated(pageable);
        return assembler.toModel(page, User::toEntityModel);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os usuários sem paginação", description = "Retorna lista completa de usuários")
    public List<User> listAll() {
        log.info("Listando todos os usuários");
        return userService.listAllPaginated(Pageable.unpaged()).getContent();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico com links HATEOAS")
    public EntityModel<User> getById(@PathVariable Long id) {
        log.info("Buscando usuário com id: {}", id);
        var user = userService.getUserById(id);
        return user.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no sistema (produtor, gestor ou comprador)")
    public User createUser(@RequestBody @Valid User user) {
        log.info("Criando usuário: {}", user);
        return userService.createUser(user);
    }
}