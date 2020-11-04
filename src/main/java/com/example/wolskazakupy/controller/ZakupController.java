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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ZakupController {

    private ZakupService zakupService;

    @Autowired
    public ZakupController(ZakupService zakupService) {
        this.zakupService = zakupService;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Zakup zakup, ModelMap map) {
        Optional<Zakup> myZakup = zakupService.findFirstByNameAndComment(zakup.getName(), zakup.getComment());
        if (myZakup.isPresent()) {
            return "redirect:/?same=yes&zakupId=" + zakup.getId();
        } else zakupService.save(zakup);
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

    @GetMapping("/revert")
    public String revert(@RequestParam Long zakupId) {
        System.out.println(zakupId);
        return "redirect:/update2?zakupId=" + zakupId;
    }

    @PostMapping("/update")
    public String update(@RequestParam Long zakupId, ModelMap map) {
        Zakup zakup = zakupService.findById(zakupId);
        map.put("update", true);
        map.put("zakup", zakup);
        map.put("button", "Update");

        map.put("zakupy", zakupService.findAll().stream().sorted(new Comparator<Zakup>() {
            @Override
            public int compare(Zakup o1, Zakup o2) {
                return (int) (o1.getId() - o2.getId());
            }
        }).collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/update2")
    public String update2(@RequestParam Long zakupId, ModelMap map) {
        Zakup zakup = zakupService.findById(zakupId);
        map.put("update", true);
        map.put("zakup", zakup);
        map.put("button", "Update");

        map.put("zakupy", zakupService.findAll().stream().sorted(new Comparator<Zakup>() {
            @Override
            public int compare(Zakup o1, Zakup o2) {
                return (int) (o1.getId() - o2.getId());
            }
        }).collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/")
    public String findAll(@RequestParam(required = false) String same,
                          @RequestParam(required = false) Long zakupId,
                          ModelMap map) {
        Zakup zakup;
        if (same != null) {
            map.put("same", "You can't save the item, name and comment are the same!");
            zakup = zakupService.findById(zakupId);
            map.put("button", "Update");
            map.put("update", true);
        } else {
            zakup = new Zakup();
            map.put("button", "Save");
        }
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
