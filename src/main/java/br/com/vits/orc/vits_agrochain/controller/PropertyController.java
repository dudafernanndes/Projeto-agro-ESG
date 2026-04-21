package br.com.vits.orc.vits_agrochain.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import br.com.vits.orc.vits_agrochain.model.Property;
import br.com.vits.orc.vits_agrochain.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@RestController
@RequestMapping("/properties")
@Tag(name = "Propriedades", description = "Gerenciamento de usuários do sistema Verdantis")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as propriedades com paginação", description = "Retorna lista paginada de propriedades com links HATEOAS")
    public PagedModel<EntityModel<Property>> getAll(
            @PageableDefault(size = 10, sort = "propertyName") Pageable pageable, 
            PagedResourcesAssembler<Property> assembler) {
        log.info("Listando propriedades paginadas - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        var page = propertyService.listAllPaginated(pageable);
        return assembler.toModel(page, Property::toEntityModel);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todas as propriedades sem paginação", description = "Retorna lista completa de propriedades")
    public List<Property> listAll(){
        log.info("Listando todas as propriedades");
        return propertyService.listAll();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova propriedade", description = "Cria uma nova propriedade")
    public Property createProperty(@RequestBody @Valid Property property, @RequestParam Long userId){
        log.info("Criando propriedade: {} para userId: {}", property, userId);
        return propertyService.createProperty(property, userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar propriedade por ID", description = "Retorna uma propriedade específica com links HATEOAS")
    public EntityModel<Property> getById(@PathVariable Long id) {
        log.info("Buscando propriedade com id: {}", id);
        var property = propertyService.getPropertyById(id);
        return property.toEntityModel();
    }
}
