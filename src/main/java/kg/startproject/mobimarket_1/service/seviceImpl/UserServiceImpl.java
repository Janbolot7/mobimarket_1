package kg.startproject.mobimarket_1.service.seviceImpl;

import kg.startproject.mobimarket_1.dto.RegistrationUserDto;
import kg.startproject.mobimarket_1.dto.UserDto;
import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.exceptions.NotFoundException;
import kg.startproject.mobimarket_1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }



    public UserDto updateUser(UserDto userDto, long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует!"));
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(user.getId());
        updatedUserDto.setEmail(user.getEmail());
        updatedUserDto.setPassword(user.getPassword());

        return updatedUserDto;
    }


    public UserDto getUserById(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует!"));
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public List<UserDto> findAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

    public void deleteUser(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует!"));
        userRepository.deleteById(user.getId());
    }
}

