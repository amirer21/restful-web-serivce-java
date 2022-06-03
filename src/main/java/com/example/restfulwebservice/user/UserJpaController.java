package com.example.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")//모든 메서드에서 가지는 기본경로
public class UserJpaController {
    //user repository 의존성주입
    @Autowired
    private UserRepository userRepository;
    //전체 사용자 목록 조회
    //http://localhost:8088/jpa/users
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }


    //spirng 2.2 이상부터 변경됨
    //https://jeonghoon.netlify.app/Spring/SpringBoot13-hateoas/
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        //Optional 검색어에 따라서 데이터가 존재할 수도 있고 없을 수도 있기때문에
        Optional<User> user = userRepository.findById(id);
        //data가 존재하는가? 없다면 예왜발생
        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }

        //spirng 2.2 이상부터 변경됨
        //Resource<User> resource = new Resource<>(user.get());
        EntityModel<User> entityModel = EntityModel.of(user.get()); //반환 객체
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers()); //반환 객체의 링크
        entityModel.add(linkTo.withRel("all-users"));

        return entityModel;//해당 리소스 반환
    }

    //사용자 삭제
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    //사용자 추가
    @PostMapping("/users")
    //@Valid 유효성검사, @RequestBody 클라이언트에서 JSON으로 받는다.
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user); //전달받은 User도메인 객체를 저장

        //id값을 자동으로 지정
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())//가져온 id값을 헤더값으로 전달
                .toUri(); //uri 데이터로 변경

        return ResponseEntity.created(location).build();
    }

    //post entity를 사용할 수 있는 api 메서드
    //전체 posting 목록 반환
    //jps/users/101번째/posts를 호출
    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        //Optional 검색어에 따라서 데이터가 존재할 수도 있고 없을 수도 있기때문에
        Optional<User> user = userRepository.findById(id);
        //data가 존재하는가? 없다면 예왜발생
        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }
        return user.get().getPosts();
    }
}
