package com.silviomoser.demo.api.memberlist;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.services.MembershipService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class MemberListApi implements ApiController {


    @Autowired
    MembershipService membershipService;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List members of a given group")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CalendarEvent.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_MEMBERLIST, method = RequestMethod.GET)
    public List<Membership> getMemberList(@RequestParam(name = "group") String group) {
        return membershipService.getMemberList(group);
    }

}
