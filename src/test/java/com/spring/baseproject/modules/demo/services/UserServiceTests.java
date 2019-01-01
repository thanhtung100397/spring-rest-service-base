package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.BaseMockitoJUnitTests;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.modules.demo.models.dtos.UpdateUserDto;
import com.spring.baseproject.modules.demo.models.dtos.UserDto;
import com.spring.baseproject.modules.demo.models.entities.User;
import com.spring.baseproject.modules.demo.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.instanceOf;

public class UserServiceTests extends BaseMockitoJUnitTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private Map<String, User> users = new HashMap<>();

    @Override
    public void init() {
        //TODO define all mocks for all method of UserRepository which were used in UserService

        when(userRepository.getUsers())
                .then((Answer<List<User>>) invocation -> {
                    List<User> result = new ArrayList<>(users.values());
                    result.sort(Comparator.comparing(User::getUsername));
                    return result;
                });

        when(userRepository.getUserByUsername(anyString()))
                .then((Answer<User>) invocation -> {
                    String username = invocation.getArgument(0);
                    return users.get(username);
                });

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            users.put(user.getUsername(), user);
            return null;
        }).when(userRepository).saveUser(any(User.class));

        doAnswer(invocation -> {
            String username = invocation.getArgument(0);
            users.remove(username);
            return null;
        }).when(userRepository).deleteUser(anyString());
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Test
    public void testGetUsers_SUCCESS() {
        User user1 = new User();
        user1.setUsername("username2");
        user1.setPassword("123456");
        user1.setEmail("email@abc.xyz");
        user1.setPhone("09123456789");

        User user2 = new User();
        user2.setUsername("username1");
        user2.setPassword("654321");
        user2.setEmail("abc@def.ghi");
        user2.setPhone("0987654321");

        users.put(user1.getUsername(), user1);
        users.put(user2.getUsername(), user2);

        BaseResponse response = userService.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));
        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        List<User> users = new ArrayList<>();
        try {
            users = (List<User>) responseBody.getData();
        } catch (Exception ignore) {
        }
        assertEquals(2, users.size());
        assertEquals(user2, users.get(0));
        assertEquals(user1, users.get(1));
    }

    @Test
    public void testFindUser_SUCCESS() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("123456");
        user.setEmail("email@abc.xyz");
        user.setPhone("0912345678");
        users.put(user.getUsername(), user);

        BaseResponse response = userService.findUser(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        assertThat(responseBody.getData(), instanceOf(User.class));
        User responseUser = (User) responseBody.getData();
        assertEquals(user.getUsername(), responseUser.getUsername());
        assertEquals(user.getPassword(), responseUser.getPassword());
        assertEquals(user.getEmail(), responseUser.getEmail());
        assertEquals(user.getPhone(), responseUser.getPhone());
    }

    @Test
    public void testFindUser_USER_NOT_FOUND() {
        BaseResponse response = userService.findUser("username");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.getCode());
        assertEquals(0, users.size());
    }

    @Test
    public void testCreateNewUser_SUCCESS() {
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("123456");
        userDto.setEmail("email@abc.xyz");
        userDto.setPhone("0912345678");

        BaseResponse response = userService.createNewUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        User user = users.get(userDto.getUsername());
        assertNotNull(user);
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPhone(), user.getPhone());
    }

    @Test
    public void testCreateNewUser_USERNAME_EXITS() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("123456");
        user.setEmail("email@abc.xyz");
        user.setPhone("0912345678");
        users.put(user.getUsername(), user);

        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("abc123");
        userDto.setEmail("abc@def.ghi");
        userDto.setPhone("0987654321");

        BaseResponse response = userService.createNewUser(userDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.CONFLICT.value(), responseBody.getCode());

        User userExist = users.get(userDto.getUsername());
        assertNotNull(userExist);
        assertEquals(user.getUsername(), userExist.getUsername());
        assertEquals(user.getPassword(), userExist.getPassword());
        assertEquals(user.getEmail(), userExist.getEmail());
        assertEquals(user.getPhone(), userExist.getPhone());
    }

    @Test
    public void testUpdateUser_SUCCESS() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("123456");
        user.setEmail("email@abc.xyz");
        user.setPhone("0912345678");
        users.put(user.getUsername(), user);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPassword("654321");
        updateUserDto.setEmail("abc@def.ghi");
        updateUserDto.setPhone("0987654321");

        BaseResponse response = userService.updateUser(user.getUsername(), updateUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        User userExist = users.get(user.getUsername());
        assertEquals(1, users.size());
        assertEquals(updateUserDto.getPassword(), userExist.getPassword());
        assertEquals(updateUserDto.getEmail(), userExist.getEmail());
        assertEquals(updateUserDto.getPhone(), userExist.getPhone());
    }

    @Test
    public void testUpdateUser_USER_NOT_FOUND() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPassword("654321");
        updateUserDto.setEmail("abc@def.ghi");
        updateUserDto.setPhone("0987654321");

        BaseResponse response = userService.updateUser("username", updateUserDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.getCode());

        assertEquals(0, users.size());
    }

    @Test
    public void testDeleteUser_SUCCESS() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("123456");
        user.setEmail("email@abc.xyz");
        user.setPhone("0912345678");
        users.put(user.getUsername(), user);

        BaseResponse response = userService.deleteUser("username");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        assertEquals(0, users.size());
    }

    @Test
    public void testDeleteUser_USER_NOT_FOUND() {
        BaseResponse response = userService.deleteUser("username");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.getCode());

        assertEquals(0, users.size());
    }
}
