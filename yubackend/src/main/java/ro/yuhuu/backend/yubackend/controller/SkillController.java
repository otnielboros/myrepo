package ro.yuhuu.backend.yubackend.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ro.yuhuu.backend.yubackend.service.GenericService;

@Api("SkillController")
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/skill")
public class SkillController {
    @Autowired
    private GenericService userService;

    @GetMapping("/all")
    public @ResponseBody
    ResponseEntity getAllSkills(){
        return new ResponseEntity(userService.getAllSkills(), HttpStatus.OK);
    }
}
