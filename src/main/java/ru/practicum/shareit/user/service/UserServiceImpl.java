package ru.practicum.shareit.user.service;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(toList());
    }

    @Override
    public UserDto getById(long id) {
        var user = getUserById(id);

        log.info("User '{}' is successfully retrieved", user.getName());
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto add(UserDto userDto) {
        throwIfEmailAlreadyExist(userDto);
        var user = userRepository.add(userMapper.toUser(userDto));

        log.info("User '{}' is successfully added", user.getName());
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        var user = getUserById(id);

        var email = userDto.getEmail();

        if (isNoneBlank(email) && !user.getEmail().equals(email)) {
            throwIfEmailAlreadyExist(userDto);
            user.setEmail(email);
        }

        if (isNoneBlank(userDto.getName())) {
            user.setName(userDto.getName());
        }

        return userMapper.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        var user = getById(id);
        userRepository.delete(id);
        log.info("User '{}' is successfully removed", user.getName());
    }

    private void throwIfEmailAlreadyExist(UserDto userDto) {
        boolean isEmailAlreadyExist = userRepository.findAll()
                .stream()
                .anyMatch(user -> user.getEmail().equals(userDto.getEmail()));

        if (isEmailAlreadyExist) {
            throw new ConflictException(format("User with email '%s' is already exist", userDto.getEmail()));
        }
    }

    private User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(format("User with id '%d' is not exist", id)));
    }
}
