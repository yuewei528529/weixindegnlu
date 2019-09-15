package com.tmy.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.tmy.model.Dengluuser;
import com.tmy.model.OAuthUser;
import com.tmy.model.User;
import com.tmy.oauth.service.CustomOAuthService;
import com.tmy.oauth.service.OAuthServices;
import com.tmy.repository.DengluuserRepository;
import com.tmy.repository.OauthUserRepository;
import com.tmy.repository.UserRepository;

import net.sf.json.JSONArray;

@Controller
public class AccountController {
    
    public static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired OAuthServices oAuthServices;
    @Autowired OauthUserRepository oauthUserRepository;
    @Autowired UserRepository userRepository;
    @Autowired DengluuserRepository dengluuserRepository;
    @RequestMapping(value = {"", "/login"}, method=RequestMethod.GET)
    public String showLogin(@RequestParam(value = "code", required = true) String code,Model model,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
       // model.addAttribute("oAuthServices", oAuthServices.getAllOAuthServices());
    	CustomOAuthService oAuthService = oAuthServices.getOAuthService("weixin");
        Token accessToken = oAuthService.getAccessToken(null, new Verifier(code));
        OAuthUser oAuthInfo = oAuthService.getOAuthUser(accessToken);
        
        System.out.println("-----------------"+oAuthInfo.getoAuthNichegn());
        OAuthUser oAuthUser = oauthUserRepository.findByOAuthTypeAndOAuthId(oAuthInfo.getoAuthType(), 
                oAuthInfo.getoAuthId());
        
        Dengluuser dengluuser = new Dengluuser();
        dengluuser.setcode(code);
        dengluuser.setoAuthNichegn(oAuthInfo.getoAuthNichegn());
        if(dengluuserRepository.existsByoAuthNichegn(oAuthInfo.getoAuthNichegn())) {
        	dengluuserRepository.deleteByoAuthNichegn(oAuthInfo.getoAuthNichegn());
        }
        dengluuserRepository.save(dengluuser);
        
        if(oAuthUser == null){
            model.addAttribute("oAuthInfo", oAuthInfo);
            request.getSession().setAttribute("oAuthNichegn", oAuthInfo.getoAuthNichegn());
            return "register";
        }
        request.setAttribute("qq", "ww");
        String cc=java.net.URLEncoder.encode(oAuthUser.getUser().getUsername(),"UTF-8");
       // return "redirect:http://www.pgepc.cn:81/oa?username="+cc;
       // request.getRequestDispatcher("http://localhost:8081/bvv/1.jsp").forward(request,response);
        
        return "redirect:http://localhost:81/oa?username="+code;
    }
    
   /* @RequestMapping(value = "/oauth/{type}/callback", method=RequestMethod.GET)
    public String claaback(@RequestParam(value = "code", required = true) String code,
            @PathVariable(value = "type") String type,
            HttpServletRequest request, Model model){
        CustomOAuthService oAuthService = oAuthServices.getOAuthService(type);
        Token accessToken = oAuthService.getAccessToken(null, new Verifier(code));
        OAuthUser oAuthInfo = oAuthService.getOAuthUser(accessToken);
        
        OAuthUser oAuthUser = oauthUserRepository.findByOAuthTypeAndOAuthId(oAuthInfo.getoAuthType(), 
                oAuthInfo.getoAuthId());
        if(oAuthUser == null){
            model.addAttribute("oAuthInfo", oAuthInfo);
            return "register";
        }
        request.getSession().setAttribute("oauthUser", oAuthUser);
        return "redirect:http://localhost:8085/oa";
    }*/
    
    @RequestMapping(value = "/register", method=RequestMethod.POST)
    public String register(Model model, User user,
            @RequestParam(value = "oAuthType", required = false, defaultValue = "") String oAuthType,
            @RequestParam(value = "oAuthId", required = true, defaultValue = "") String oAuthId,
            HttpServletRequest request){
    	System.out.println("pppppppppppppppppppppp"+request.getAttribute("oAuthNichegn"));
        OAuthUser oAuthInfo = new OAuthUser();
        oAuthInfo.setoAuthId(oAuthId);
        oAuthInfo.setoAuthType(oAuthType);
        oAuthInfo.setoAuthNichegn(request.getSession().getAttribute("oAuthNichegn").toString());
        if(userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("errorMessage", "用户名已存在");
            model.addAttribute("oAuthInfo", oAuthInfo);
            return "register";
        }
        user = userRepository.save(user);
        OAuthUser oAuthUser = oauthUserRepository.findByOAuthTypeAndOAuthId(oAuthType, oAuthId);
        if(oAuthUser == null){
            oAuthInfo.setUser(user);
            oAuthUser = oauthUserRepository.save(oAuthInfo);
        }
        request.getSession().setAttribute("oauthUser", oAuthUser);
        return "redirect:/success";
    }
  
    @RequestMapping(value = "/success", method=RequestMethod.GET)
    @ResponseBody
    public Object success(HttpServletRequest request,@RequestParam(value = "code", required = true) String code){
    	List<Dengluuser> cc;
    	String ff=dengluuserRepository.findBycode(code).getoAuthNichegn();
    	
    //	JSONArray jsonArray = JSONArray.fromObject(cc);
    	  //  String result = jsonArray.toString();
    
        return ff  ;
        
    }
    

}
