package br.com.vits.orc.vits_agrochain.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import br.com.vits.orc.vits_agrochain.model.UserType;
import br.com.vits.orc.vits_agrochain.service.UserTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user-types")
@Tag(name = "Tipos de Usuário", description = "Gerenciamento de tipos de usuário (Produtor, Gestor, Comprador)")
public class UserTypeController {

    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    // POST futuramente apenas para Admins

    @GetMapping
    @Operation(summary = "Listar tipos de usuário", description = "Retorna todos os tipos de usuário cadastrados")
    public List<UserType> listAll() {
        log.info("Listando todos os tipos de usuário");
        return userTypeService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuário por ID", description = "Retorna um tipo de usuário específico pelo ID")
    public UserType getById(@PathVariable Long id) {
        log.info("Buscando tipo de usuário com id: {}", id);
        return userTypeService.getUserTypeById(id);
    }

}
