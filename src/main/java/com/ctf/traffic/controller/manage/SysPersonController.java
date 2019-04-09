package com.ctf.traffic.controller.manage;

import com.alibaba.fastjson.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.po.response.*;
import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.service.*;
import com.ctf.traffic.util.*;
import io.swagger.annotations.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.annotation.*;
import javax.servlet.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.config.annotation.*;

@RestController
@RequestMapping("/s/sysperson")
@Api(description = "管理端系统操作员接口")
public class SysPersonController {
    @Resource
    private com.ctf.traffic.repository.sys.SysPersonRepository personRepository;
    @Resource
    private SysPersonService personService;

    /**
     * 管理员登录
     *
     * @param empNo
     * @param password
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @RequestMapping("/publicLogin")
    String publicLogin(@RequestParam("empNo") String empNo, @RequestParam("password") String password,
                       HttpServletRequest httpServletRequest) {
        boolean loginCheck = personService.login(empNo, password);
        if (loginCheck) {
            SysPerson sysPerson = personService.findByEmpNo(empNo);

            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("person", sysPerson);

            Map<String, String> map = new HashMap<String, String>();
            map.put("userId", sysPerson.getId().toString());
            return new RtnJson(true, "登录成功", map).toString();
        } else {
            return new RtnJson(false, "用户名或密码错误").toString();
        }

    }

    /**
     * 根据员工编号查询员工
     *
     * @param empNo
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/findPersonByEmpNo")
    String findPersonByEmpNo(@RequestParam("empNo") String empNo) throws Exception {
        SysPerson p = personService.findByEmpNo(empNo);
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", p.getName());
        map.put("gender", p.getGender().toString());
        map.put("phone", p.getPhone());
        return new RtnJson(true, "获取用户信息成功", map).toString();
    }

    /**
     * 添加员工
     *
     * @param o
     * @param roleIds
     * @param validDate1
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/add")
    String add(SysPerson o, @RequestParam("roleIds") String roleIds, @RequestParam("validDate1") String validDate1,
               @RequestParam(value = "substationId", required = false) Long substationId)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date vd1;
        try {
            vd1 = sdf.parse(validDate1);
        } catch (Exception e) {
            throw new RuntimeException("SysPerson add 日期格式转换异常");
        }
        SysPerson p = personService.add(o, roleIds, vd1, substationId == null ? 0 : substationId);
        Map<String, String> map = new HashMap<>();
        map.put("newPersonEmpNo", p.getEmpNo().toString());
        return new RtnJson(true, "保存成功", map).toString();
    }

    /**
     * 设置头像
     *
     * @param file
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/setImg")
    String setImg(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) throws Exception {
        SysPerson person = personService.findById(id);
        // 获取shop图片目录的相对值路径
        String imageUrl = ImageUtil.generateThumbnail(file.getInputStream(), file.getOriginalFilename(),
                PathUtil.getPersonImagePath(person.getEmpNo()));
        person.setImageUrl(imageUrl);
        personRepository.saveOrUpdate(person);
        //更新session中的person的imageUrl
        ContextThreadLocal.getPerson().setImageUrl(imageUrl);
        return null;
    }

    /**
     * 获取头像
     *
     * @param id
     * @param registry
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/getImg")
    String setImg(@RequestParam("id") String id, ResourceHandlerRegistry registry) throws Exception {
        SysPerson person = personService.findById(id);

        // 获取shop图片目录的相对值路径

        String imageUrl = person.getImageUrl();

        File file = new File(PathUtil.getImgBasePath() + imageUrl, "userImage");
        registry.addResourceHandler("/image/**").addResourceLocations(PathUtil.getImgBasePath() + imageUrl);

        return null;
    }

    /**
     * 查询所有员工
     *
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/findAll")
    CommonResponse findAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return new CommonResponse(true, personService.findByExample(new SysPerson(), page, size));
    }

    /**
     * 初始化员工界面
     *
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/initUserInfoById")
    String initUserInfoById(HttpServletRequest httpServletRequest) throws Exception {
        Optional<SysPerson> o = personRepository.findById(ContextThreadLocal.getPerson().getId());
        SysPerson p = o.orElse(null);
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("userName", p.getName());
        map.put("validate", p.getValidDate().getTime());
        map.put("phone", p.getPhone());
        map.put("gender", p.getGender());

        map.put("roles", p.getRoles());

        return new RtnJson(true, "根据id查询用户成功", map).toString();
    }

    /**
     * 根据id删除员工
     *
     * @param toDeletePersonId
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/deleteById")
    String deleteById(@RequestParam("toDeletePersonId") String toDeletePersonId) throws Exception {
        Long id = Long.parseLong(toDeletePersonId);
        personRepository.deleteById(id);
        return new RtnJson(true, "根据id删除用户成功").toString();
    }

    /**
     * 员工信息修改
     *
     * @param o
     * @param roleIds
     * @param validDate1
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/update")
    String update(SysPerson o, @RequestParam(value = "roleIds", required = false) String roleIds,
                  @RequestParam(value = "validDate1", required = false) String validDate1,
                  @RequestParam(value = "substationId", required = false) Long substationId)
            throws Exception {
        personService.update(o, roleIds, validDate1, substationId == null ? 0 : substationId);

        return new RtnJson(true, "修改成功").toString();
    }

    /**
     * 根据角色查询员工
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    @RequestMapping("/person/findByRole")
    String findByRole(long roleId) throws Exception {
        return new RtnJson(true, "该角色的操作员查询成功", personService.findByRole(roleId)).toString();
    }

    @GetMapping("/person/findBySubstationId")
    @ApiParam("通过协调中心Id获取管理员")
    public CommonResponse findBySubstationId(@RequestParam("substationId") long substationId,
                                             @RequestParam(value = "empNo", required = false) String empNo,
                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, personService.findBySubstationId(substationId, empNo, page, size));
    }

    @RequestMapping("/person/changepassword")
    CommonResponse changepassword(@RequestParam(value = "oldpassword", required = true) String oldpassword,
                          @RequestParam(value = "newpassword", required = true) String newpassword)
            throws Exception {

        return     personService.changepassword(oldpassword,newpassword);
    }

    @GetMapping("/person/getPoliceList")
    @ApiParam("管理端获取交警列表")
    public CommonResponse getPoliceList() {
        JSONObject jsonObject=new JSONObject();
        List<PoliceListResponse> policeListResponses=new ArrayList<>();
        personService.findByRole(2l).forEach(a->{
            policeListResponses.add(new PoliceListResponse(a.getId(),a.getName()));
        });
        jsonObject.put("polices",policeListResponses);
        return new CommonResponse(true,jsonObject);
    }


}
