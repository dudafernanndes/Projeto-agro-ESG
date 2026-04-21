package br.com.vits.orc.vits_agrochain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.vits.orc.vits_agrochain.model.Property;
import br.com.vits.orc.vits_agrochain.model.User;
import br.com.vits.orc.vits_agrochain.repository.PropertyRepository;
import java.util.List;

@Service
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    public PropertyService(PropertyRepository propertyRepository, UserService userService){
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }
    
    public Property createProperty(Property property, Long userId){
        User user = userService.getUserById(userId);
        property.setUser(user);
        return propertyRepository.save(property);
    }

    public List<Property> listAll() {
        return propertyRepository.findAll();
    }

    public Page<Property> listAllPaginated(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }
}
