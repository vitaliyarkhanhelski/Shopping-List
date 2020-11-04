package com.example.wolskazakupy.controller;

import com.example.wolskazakupy.model.Zakup;
import com.example.wolskazakupy.service.ZakupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class ZakupController {

    private ZakupService zakupService;

    @Autowired
    public ZakupController(ZakupService zakupService) {
        this.zakupService = zakupService;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Zakup zakup) {
        zakupService.save(zakup);
        return "redirect:/";
    }

    @PostMapping("/in-progress")
    public String inProgress(@RequestParam Long zakupId) {
        Zakup zakup = zakupService.findById(zakupId);
        if (zakup.isInProcess()) {
            zakup.setInProcess(false);
        } else {
            zakup.setInProcess(true);
        }
        zakupService.save(zakup);
        return "redirect:";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long zakupId) {
        zakupService.delete(zakupId);
        return "redirect:";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long zakupId, ModelMap map) {
        Zakup zakup = zakupService.findById(zakupId);
        map.put("zakup", zakup);

        map.put("zakupy", zakupService.findAll().stream().sorted(new Comparator<Zakup>() {
            @Override
            public int compare(Zakup o1, Zakup o2) {
                return (int) (o1.getId() - o2.getId());
            }
        }).collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/")
    public String findAll(ModelMap map) {
        Zakup zakup = new Zakup();
        map.put("zakup", zakup);

        if (zakupService.findAll().size() != 0)
            map.put("zakupy", zakupService.findAll().stream().sorted(new Comparator<Zakup>() {
                @Override
                public int compare(Zakup o1, Zakup o2) {
                    return (int) (o1.getId() - o2.getId());
                }
            }).collect(Collectors.toList()));
        else map.put("result", "Stocks are replenished. Nothing is needed");
        return "index";
    }

}
