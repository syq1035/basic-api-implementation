package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  public List<RsEvent> initRsList() {
      List<RsEvent> rslist = new ArrayList<>();
      rslist.add(new RsEvent("第一条事件", "无分类"));
      rslist.add(new RsEvent("第二条事件", "无分类"));
      rslist.add(new RsEvent("第三条事件", "无分类"));
    System.out.println(rslist.toString());
      return rslist;
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getAllRsEvent() {
      return rsList;
  }

}
