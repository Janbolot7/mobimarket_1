package kg.startproject.mobimarket_1.service.seviceImpl;

import kg.startproject.mobimarket_1.model.Role;
import kg.startproject.mobimarket_1.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER").get();
    }
}
